/*
 * **************************************************************************
 * *                                                                        *
 * * Ericsson hereby grants to the user a royalty-free, irrevocable,        *
 * * worldwide, nonexclusive, paid-up license to copy, display, perform,    *
 * * prepare and have prepared derivative works based upon the source code  *
 * * in this sample application, and distribute the sample source code and  *
 * * derivative works thereof and to grant others the foregoing rights.     *
 * *                                                                        *
 * * ERICSSON DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,        *
 * * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.       *
 * * IN NO EVENT SHALL ERICSSON BE LIABLE FOR ANY SPECIAL, INDIRECT OR      *
 * * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS    *
 * * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE  *
 * * OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE *
 * * OR PERFORMANCE OF THIS SOFTWARE.                                       *
 * *                                                                        *
 * **************************************************************************
 */

package com.ericsson.nrgsdk.examples.tools.log.readers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import com.ericsson.hosasdk.api.SDKException;
import com.ericsson.nrgsdk.examples.tools.configuration.ObjectReader;

public class TpHosaReader implements SpecializedObjectReader {

	public static final TpHosaReader INSTANCE = new TpHosaReader();

	private StructReader theStructReader = new StructReader();

	private static final Class[] NO_PARMS = new Class[0];

	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		if (!aTargetClass.getName().startsWith("com.ericsson.hosasdk.api.")) {
			return NOT_SUPPORTED;
		}
		Object result;
		if (hasMethod(aTargetClass, "discriminator", NO_PARMS)) {
			result = getUnion(aProps, aValue, aIsIndirected, aTargetClass);
		} else if (hasMethod(aTargetClass, "value", NO_PARMS)) {
			result = getEnum(aProps, aValue, aIsIndirected, aTargetClass);
		} else {
			result = getStruct(aProps, aValue, aIsIndirected, aTargetClass);
		}
		if (result == NOT_SUPPORTED) {
			throw new SDKException("Unexpected HOSA type (" + aValue + ")");
		}
		return result;
	}

	private boolean hasMethod(Class aClass, String aName, Class[] aParms) {
		try {
			aClass.getMethod(aName, aParms);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Object getStruct(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		return theStructReader.getAny(aProps, aValue, aIsIndirected,
				aTargetClass);
	}

	private Object getEnum(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
		if (value == null) {
			throw new SDKException("Not specified: " + aValue);
		}
		String fieldName = value.toUpperCase();
		if (!fieldName.startsWith("P_")) {
			fieldName = "P_" + fieldName;
		}
		try {
			Field field = aTargetClass.getDeclaredField(fieldName);
			return field.get(null);
		} catch (Exception e) {
			throw new SDKException(fieldName + " does not exist in "
					+ aTargetClass.getName());
		}
	}

	private Object getUnion(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
		if (aValue == null && aIsIndirected) {
			throw new RuntimeException(aValue + "may not be null");
		}
		String initName, subValue;
		int split = value.indexOf(":");
		boolean indirected;
		if (split != -1) {
			initName = value.substring(0, split).trim();
			subValue = value.substring(split + 1).trim();
			indirected = false;
		} else if (aIsIndirected) {
			initName = value;
			subValue = aValue + ".value";
			indirected = true;
		} else {
			throw new RuntimeException("Not supported");
		}
		return getUnion(aProps, initName, subValue, indirected, aTargetClass);
	}

	private Object getUnion(Properties aProps, String aBranch, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		Method init = null;
		Method[] methods = aTargetClass.getDeclaredMethods();
		for (int i = 0; init == null && i != methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(aBranch)
					&& methods[i].getReturnType() == void.class) {
				init = methods[i];
			}
		}
		if (init == null) {
			throw new RuntimeException("Initializer " + aBranch
					+ " not found in " + aTargetClass.getName());
		}
		Class[] parms = init.getParameterTypes();
		if (parms.length != 1) {
			throw new SDKException("Complex initializer " + aBranch
					+ " not supported");
		}
		Object[] args = { ObjectReader.DEFAULT.getAny(aProps, aValue,
				aIsIndirected, parms[0]) };
		Object result;
		try {
			result = aTargetClass.newInstance();
			init.invoke(result, args);
		} catch (Exception e) {
			throw new SDKException("Cannot create " + aTargetClass.getName());
		}
		return result;
	}
}
