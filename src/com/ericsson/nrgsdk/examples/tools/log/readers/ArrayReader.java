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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.ericsson.nrgsdk.examples.tools.configuration.ObjectReader;

public class ArrayReader implements SpecializedObjectReader {

	public static final ArrayReader INSTANCE = new ArrayReader();

	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		if (!aTargetClass.isArray()) {
			return NOT_SUPPORTED;
		}
		if (!aIsIndirected) {
			throw new RuntimeException("Indirection not supported");
		}
		Object result = NOT_SUPPORTED;
		if (aTargetClass == byte[].class && prefixExists(aProps, aValue)
				&& !prefixExists(aProps, aValue + ".0")) {
			Object subResult = ObjectReader.DEFAULT.getAny(aProps, aValue,
					aIsIndirected, String.class);
			if (subResult != NOT_SUPPORTED) {
				byte[] byteResult = ((String) subResult).getBytes();
				result = Array.newInstance(byte.class, byteResult.length);
				for (int i = 0; i != byteResult.length; i++) {
					Array.set(result, i, new Byte(byteResult[i]));
				}
			}
		} else {
			List resultList = new ArrayList();
			for (int i = 0; prefixExists(aProps, aValue + "." + i); i++) {
				Object subResult = ObjectReader.DEFAULT.getAny(aProps, aValue
						+ "." + i, aIsIndirected, aTargetClass
						.getComponentType());
				if (subResult != NOT_SUPPORTED) {
					resultList.add(subResult);
				}
			}
			result = Array.newInstance(aTargetClass.getComponentType(),
					resultList.size());
			for (int i = 0; i != resultList.size(); i++) {
				Array.set(result, i, resultList.get(i));
			}
		}
		return result;
	}

	private boolean prefixExists(Properties aProps, String aPrefix) {
		aPrefix = aPrefix.toLowerCase();
		for (Enumeration i = aProps.propertyNames(); i.hasMoreElements();) {
			String key = (String) i.nextElement();
			if (key.startsWith(aPrefix)) {
				return true;
			}
		}
		return false;
	}
}
