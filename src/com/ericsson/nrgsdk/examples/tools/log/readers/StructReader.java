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
import java.util.Properties;

import com.ericsson.hosasdk.api.SDKException;
import com.ericsson.nrgsdk.examples.tools.configuration.ObjectReader;

public class StructReader implements SpecializedObjectReader {

	public static final StructReader INSTANCE = new StructReader();

	private static Class[] NO_PARMS = new Class[0];

	private static Object[] NO_ARGS = new Object[0];

	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		if (!aIsIndirected) {
			throw new SDKException("Indirection not supported");
		}
		Field[] fields = aTargetClass.getDeclaredFields();
		Object result;
		try {
			java.lang.reflect.Constructor cons = aTargetClass
					.getConstructor(NO_PARMS);
			cons.setAccessible(true);
			result = cons.newInstance(NO_ARGS);
		} catch (Exception e) {
			throw new SDKException("No public default constructor for "
					+ aTargetClass.getName());
		}
		for (int i = 0; i != fields.length; i++) {
			String subValue = aValue;
			if (subValue.length() > 0) {
				subValue += '.';
			}
			subValue += fields[i].getName();
			Object subResult = ObjectReader.DEFAULT.getAny(aProps, subValue,
					aIsIndirected, fields[i].getType());
			if (subResult != null && subResult != NOT_SUPPORTED) {
				try {
					fields[i].setAccessible(true);
					fields[i].set(result, subResult);
				} catch (Exception e) {
					throw new SDKException("Cannot set field "
							+ fields[i].getName() + " in "
							+ aTargetClass.getName());
				}
			}
		}
		return result;
	}
}
