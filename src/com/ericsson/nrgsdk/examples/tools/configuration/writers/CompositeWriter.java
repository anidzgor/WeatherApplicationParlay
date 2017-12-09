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

/**
 * Class for putting all writers in a list so that the proper writer is used to
 * print the content of a specific object.
 */
public class CompositeWriter implements SpecializedObjectWriter {

	private SpecializedObjectWriter[] theWriters = new SpecializedObjectWriter[0];

	/**
	 * @param aWriter
	 *            the writer to add to the collection, this writer is later used
	 *            to parse the content of the object to a String value
	 */
	public void register(SpecializedObjectWriter aWriter) {
		unregister(aWriter);
		SpecializedObjectWriter[] newWriters = new SpecializedObjectWriter[theWriters.length + 1];
		System.arraycopy(theWriters, 0, newWriters, 1, theWriters.length);
		newWriters[0] = aWriter;
		theWriters = newWriters;
	}

	/**
	 * @param aWriter
	 *            the writer to remove from the collection of object writers.
	 *            This type of object which the writer represents will no longer
	 *            be parsed.
	 */
	public void unregister(SpecializedObjectWriter aWriter) {
		for (int i = 0; i < theWriters.length; i++) {
			if (theWriters[i] == aWriter) {
				SpecializedObjectWriter[] newWriters = new SpecializedObjectWriter[theWriters.length - 1];
				System.arraycopy(theWriters, 0, newWriters, 0, i);
				System.arraycopy(theWriters, i + 1, newWriters, 0,
						newWriters.length - i);
				theWriters = newWriters;
				i--;
			}
		}
	}

	/**
	 * 
	 * @param anOldWriter
	 *            the writer that shall be replaced
	 * @param aNewWriter
	 *            the new writer that replaces the old
	 */
	public void replace(SpecializedObjectWriter anOldWriter,
			SpecializedObjectWriter aNewWriter) {
		for (int i = 0; i < theWriters.length; i++) {
			if (theWriters[i] == anOldWriter) {
				theWriters[i] = aNewWriter;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.ericsson.nrgsdk.examples.tools.log.writers.SpecializedObjectWriter#append(java.lang.Object,
	 *      java.lang.StringBuffer)
	 */
	public boolean append(Object o, StringBuffer s) {
		for (int i = 0; i < theWriters.length; i++) {
			if (theWriters[i].append(o, s)) {
				return true;
			}
		}
		return false;
	}
}
