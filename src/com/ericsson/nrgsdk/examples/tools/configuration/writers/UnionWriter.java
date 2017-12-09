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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for parsing complex objects to a string value
 */
public class UnionWriter implements SpecializedObjectWriter {

	private HashMap theUnionInfo = new HashMap();

	private static final UnionInfo NO_UNION = new UnionInfo();

	private static final Class[] NO_PARMS = {};

	private static final Object[] NO_ARGS = {};

	private static class UnionInfo {

		Method discriminator, branches[];

		HashMap constants = new HashMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.ericsson.nrgsdk.examples.tools.log.writers.SpecializedObjectWriter#append(java.lang.Object,
	 *      java.lang.StringBuffer)
	 */
	public boolean append(Object o, StringBuffer s) {
		ObjectWriter ow = new ObjectWriter();
		Class c = o.getClass();
		UnionInfo info = (UnionInfo) theUnionInfo.get(c);
		if (info == null) {
			info = getUnionInfo(c);
			theUnionInfo.put(c, info);
		}
		if (info == NO_UNION) {
			return false;
		}
		s.append("<");
		try {
			Object result = info.discriminator.invoke(o, NO_ARGS);
			s.append((String) info.constants.get(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0, resultCount = 0; i != info.branches.length; i++) {
			try {
				Object result = info.branches[i].invoke(o, NO_ARGS);
				s.append(resultCount++ == 0 ? ": " : ", ");
				s.append(info.branches[i].getName());
				s.append(" = ");
				//ObjectWriter.DEFAULT.append(result, s);
				s.append(ow.print(result));
			} catch (Exception e) {//
			}
		}
		s.append(">");
		return true;
	}

	private UnionInfo getUnionInfo(Class c) {
		try {
			Method discriminator = c.getMethod("discriminator", NO_PARMS);
			Method[] methods = c.getDeclaredMethods();
			ArrayList branches = new ArrayList();
			for (int i = 0; i != methods.length; i++) {
				if (!methods[i].equals(discriminator)
						&& methods[i].getParameterTypes().length == 0
						&& !methods[i].getName().startsWith("_")) {
					branches.add(methods[i]);
				}
			}
			UnionInfo info = new UnionInfo();
			info.discriminator = discriminator;
			info.branches = (Method[]) branches.toArray(new Method[branches
					.size()]);
			Field[] fields = discriminator.getReturnType().getDeclaredFields();
			for (int i = 0; i != fields.length; i++) {
				if (fields[i].getType().equals(discriminator.getReturnType())) {
					String name = fields[i].getName();
					Object value = fields[i].get(null);
					info.constants.put(value, name);
				}
			}
			return info;
		} catch (Exception e) {
			return NO_UNION;
		}
	}
}
