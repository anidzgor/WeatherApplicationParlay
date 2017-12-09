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

package com.ericsson.nrgsdk.examples.tools.configuration.writers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class for parsing enumerations to a string value
 */
public class EnumWriter implements SpecializedObjectWriter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.ericsson.nrgsdk.examples.tools.log.writers.SpecializedObjectWriter#append(java.lang.Object,
	 *      java.lang.StringBuffer)
	 */
	public boolean append(Object o, StringBuffer s) {
		Class clazz = o.getClass();
		Method valueMethod;
		try {
			valueMethod = clazz.getMethod("value", null);
		} catch (NoSuchMethodException ex) {
			return false;
		}
		Field[] fields = clazz.getFields();
		int enumIndex = 0;
		try {
			Integer ix = (Integer) valueMethod.invoke(o, null);
			enumIndex = ix.intValue();
		} catch (Exception ex) {
			s.append(o.toString());
			return true;
		}
		// Check if one of fields match resultObject
		int filedValue = 0;
		for (int i = 0; i < fields.length; i++) {
			try {
				filedValue = fields[i].getInt(o);
			} catch (Exception ex) {
				continue;
			}
			if (filedValue == enumIndex) {
				StringBuffer temp = new StringBuffer(fields[i].getName());
				if (temp.charAt(0) == '_') {
					temp.deleteCharAt(0);
				}
				s.append(temp);
				return true;
			}
		}
		s.append(enumIndex);
		return true;
	}
}
