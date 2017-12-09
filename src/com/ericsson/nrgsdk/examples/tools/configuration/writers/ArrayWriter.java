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

import java.lang.reflect.Array;

/**
 * Class for parsing arrays to a string value
 */
public class ArrayWriter implements SpecializedObjectWriter {

	//public ArrayWriter INSTANCE = new ArrayWriter();
	private String ARRAY_ELEMENT_SEPARATOR = ", ";

	private int MAX_ARRAY_LENGTH = 15;

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.ericsson.nrgsdk.examples.tools.log.writers.SpecializedObjectWriter#append(java.lang.Object,
	 *      java.lang.StringBuffer)
	 */
	public boolean append(Object o, StringBuffer s) {
		if (!o.getClass().isArray()) {
			return false;
		}
		s.append("[");
		int n = Array.getLength(o);
		int m = Math.min(n, MAX_ARRAY_LENGTH);
		ObjectWriter ow = new ObjectWriter();
		for (int i = 0; i != m; i++) {
			if (i != 0) {
				s.append(ARRAY_ELEMENT_SEPARATOR);
			}
			s.append(ow.print(Array.get(o, i)));
		}
		if (n > m) {
			s.append(ARRAY_ELEMENT_SEPARATOR + "...");
		}
		s.append("]");
		return true;
	}
}
