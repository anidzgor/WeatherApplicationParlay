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

import java.util.Properties;

public class PrimitiveReader implements SpecializedObjectReader {

	public static final PrimitiveReader INSTANCE = new PrimitiveReader();

	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		Object result = NOT_SUPPORTED;
		if (aTargetClass == boolean.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			result = new Boolean(value);
		} else if (aTargetClass == float.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "0";
			}
			result = new Float(value);
		} else if (aTargetClass == double.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "0";
			}
			result = new Double(value);
		} else if (aTargetClass == int.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "0";
			}
			result = new Integer(value);
		} else if (aTargetClass == byte.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "0";
			}
			result = new Byte(value);
		} else if (aTargetClass == long.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "0";
			}
			result = new Long(value);
		} else if (aTargetClass == short.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "0";
			}
			result = new Short(value);
		} else if (aTargetClass == char.class) {
			String value = aIsIndirected ? aProps.getProperty(aValue) : aValue;
			if (value == null || value.length() == 0) {
				value = "\0";
			}
			result = new Character(value.charAt(0));
		}
		return result;
	}
}
