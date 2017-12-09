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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Class for parsing structs to a string value
 */
public class StructWriter implements SpecializedObjectWriter {

	private String fieldElementSeparator = ", ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.ericsson.nrgsdk.examples.tools.log.writers.SpecializedObjectWriter#append(java.lang.Object,
	 *      java.lang.StringBuffer)
	 */
	public boolean append(Object o, StringBuffer s) {
		if (!accept(o.getClass())) {
			return false;
		}
		try {
			int myStart = s.length();
			int myLastIndex = 0;
			int mySize = 0;
			Class myClass = o.getClass();
			ObjectWriter ow = new ObjectWriter();
			while (myClass != null && accept(myClass)) {
				Field[] myFields = getFields(myClass);
				for (int i = 0; i != myFields.length; i++) {
					if (accept(myFields[i])) {
						if (mySize > 0) {
							s.append(fieldElementSeparator);
						}
						Object myValue = myFields[i].get(o);
						String name = myFields[i].getName();
						while (name.charAt(0) == '_') {
							name = name.substring(1);
						}
						s.append(name);
						s.append(" = ");
						myLastIndex = s.length();
						s.append(ow.print(myValue));
						//ObjectWriter.DEFAULT.append(myValue, s);
						mySize++;
					}
				}
				myClass = myClass.getSuperclass();
			}
			if (mySize > 1) {
				s.insert(myStart, '(');
				s.append(')');
			} else if (mySize == 1) {
				s.delete(myStart, myLastIndex);
			}
		} catch (Exception e) {//
		}
		return true;
	}

	Field[] getFields(Class aClass) {
		Field[] myFields = aClass.getDeclaredFields();
		AccessibleObject.setAccessible(myFields, true);
		return myFields;
	}

	private static final int NOT_ACCEPTED_MODIFIERS = Modifier.STATIC
			| Modifier.FINAL;

	protected boolean accept(Field f) {
		int m = f.getModifiers();
		return (m & NOT_ACCEPTED_MODIFIERS) == 0;
	}

	protected boolean accept(Class c) {
		return !c.getName().startsWith("java.");
	}
}
