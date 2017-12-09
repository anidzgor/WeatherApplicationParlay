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

import java.io.PrintWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.ericsson.hosasdk.api.SDKException;
import com.ericsson.hosasdk.api.SDKHOSAException;

/**
 * Class for parsing JAVA exceptions to a string value
 */
public class ExceptionWriter implements SpecializedObjectWriter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.ericsson.nrgsdk.examples.tools.log.writers.SpecializedObjectWriter#append(java.lang.Object,
	 *      java.lang.StringBuffer)
	 */
	public boolean append(Object o, StringBuffer s) {
		ObjectWriter ow = new ObjectWriter();
		StructWriter sw = new StructWriter();
		if (!(o instanceof Throwable)) {
			return false;
		}
		if (o instanceof SDKException) {
			SDKException e = (SDKException) o;
			printStackTrace(e, s);
			if (e instanceof SDKHOSAException) {
				Field[] myFields = getFields(e.getClass());
				for (int i = 0; i != myFields.length; i++) {
					if (accept(myFields[i])) {
						try {
							String name = myFields[i].getName();
							Object myValue = myFields[i].get(e);
							if (name.equals("theThrowable")) {
								if (myValue != null) {
									s.append(name);
									s.append(" = ");
									//ObjectWriter.DEFAULT.append(myValue.toString(),
									// s);
									s.append(ow.print(myValue.toString()));
								}
							} else {
								s.append(name);
								s.append(" = ");
								s.append(ow.print(myValue.toString()));
							}
							s.append("\n");
						} catch (Exception ex) {
							//void
						}
					}
				}
				sw.append(e, s);
			}
		} else {
			printStackTrace((Throwable) o, s);
			sw.append(o, s);
		}
		return true;
	}

	private Field[] getFields(Class aClass) {
		Field[] myFields = aClass.getDeclaredFields();
		AccessibleObject.setAccessible(myFields, true);
		return myFields;
	}

	private void printStackTrace(Throwable e, StringBuffer s) {
		java.io.StringWriter writer = new java.io.StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		s.append(writer.toString());
	}

	private static final int NOT_ACCEPTED_MODIFIERS = Modifier.STATIC
			| Modifier.FINAL;

	protected boolean accept(Field f) {
		int m = f.getModifiers();
		return (m & NOT_ACCEPTED_MODIFIERS) == 0;
	}
}
