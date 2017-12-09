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

package com.ericsson.nrgsdk.examples.tools.log;

import java.util.LinkedList;
import java.util.ListIterator;

import com.ericsson.nrgsdk.examples.tools.SDKToolkit;
import com.ericsson.nrgsdk.examples.tools.configuration.writers.ObjectWriter;

// ******************************************************************************
/**
 * The Output class is responsible for outputting messages to the registered
 * output streams.
 */
public class Output {

	/** The writer to use for printing object contents. */
	//private SpecializedObjectWriter itsWriter;
	ObjectWriter itsWriter;

	/** The registered output streams. */
	private LinkedList itsStreams;

	// ------------------------------------------------------------------------------
	/** Creates an Output object without any output streams. */
	public Output() {
		//itsWriter = (SpecializedObjectWriter)
		// SDKToolkit.OBJECTWRITER;
		itsWriter = SDKToolkit.OBJECTWRITER;
		itsStreams = new LinkedList();
	}

	// ------------------------------------------------------------------------------
	/**
	 * Adds aStream to the list of output streams. aFilter is used to decide
	 * whether a message must be written to the stream or not.
	 * 
	 * @param aStream
	 *            the output stream to register
	 * @param aFilter
	 *            the filter to use (null value result in no filtering)
	 */
	public void addStream(OutputStream aStream, OutputFilter aFilter) {
		itsStreams.add(new Entry(aStream, aFilter));
	}

	// ------------------------------------------------------------------------------
	/**
	 * Removes aStream from the list of output streams.
	 * 
	 * @param aStream
	 *            the output stream to unregister
	 */
	public void removeStream(OutputStream aStream) {
		itsStreams.remove(new Entry(aStream, null));
	}

	// ------------------------------------------------------------------------------
	/**
	 * Performs output for a messages of type aType with priority aLevel and
	 * aData. Filtering is performed for each stream.
	 * 
	 * @param aType
	 *            the type of message (typically three capitals)
	 * @param aLevel
	 *            the priority level (1=highest, typically 5=lowest)
	 * @param aData
	 *            the object to write
	 */
	public void process(String aType, int aLevel, Object aData) {
		process(aType, aLevel, "", aData);
	}

	// ------------------------------------------------------------------------------
	/**
	 * Performs output for a messages of type aType with priority aLevel and
	 * aData. Filtering is performed for each stream.
	 * 
	 * @param aType
	 *            the type of message (typically three capitals)
	 * @param aLevel
	 *            the priority level (1=highest, typically 5=lowest)
	 * @param aLabel
	 *            a label prepend
	 * @param aData
	 *            the object to write
	 */
	public void process(String aType, int aLevel, String aLabel, Object aData) {
		StringBuffer message = new StringBuffer(aLabel);
		//itsWriter.append(aData, message);
		itsWriter.print(message);
		ListIterator current = itsStreams.listIterator(0);
		// Limit length of aType to four characters justify right
		String prnType = limitString(aType, 4);
		while (current.hasNext()) {
			Entry entry = (Entry) current.next();
			if (entry.filter.logIt(aType, aLevel)) {
				entry.stream.output(prnType, aLevel, message.toString());
			}
		}
	}

	// ------------------------------------------------------------------------------
	/**
	 * Indicates if filtering is enabled for a particular type and level.
	 * 
	 * @param aType
	 *            the type of message (typically three capitals)
	 * @param aLevel
	 *            the priority level (1=highest, typically 5=lowest)
	 */
	public boolean isActive(String aType, int aLevel) {
		ListIterator current = itsStreams.listIterator(0);
		while (current.hasNext()) {
			Entry entry = (Entry) current.next();
			if (entry.filter.logIt(aType, aLevel)) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------------
	/**
	 * Indicates if filtering is enabled for a particular type.
	 * 
	 * @param aType
	 *            the type of message (typically three capitals)
	 */
	public boolean isActive(String aType) {
		return isActive(aType, 1);
	}

	// ==============================================================================
	/**
	 * The Entry class is a record that combines a stream with a filter.
	 */
	private class Entry {

		/** The output stream. */
		OutputStream stream;

		/** The associated filter. */
		OutputFilter filter; // ------------------------------------------------------------------------------

		/**
		 * Creates an entry for aStream and aFilter.
		 * 
		 * @param aStream
		 *            the output stream
		 * @param aFilter
		 *            the associated filter
		 */
		Entry(OutputStream aStream, OutputFilter aFilter) {
			stream = aStream;
			filter = aFilter;
		}

		// ------------------------------------------------------------------------------
		/**
		 * Returns whether anObject applies to the same stream as self.
		 * 
		 * @param anObject
		 *            the object to compare to
		 * @return true if anObject applies to the same stream, false otherwise
		 */
		public boolean equals(Object anObject) {
			try {
				Entry entry = (Entry) anObject;
				return stream.equals(entry.stream);
			} catch (Exception anException) {
				return false;
			}
		}
	}

	// ------------------------------------------------------------------------------
	/**
	 * <code>limitString</code> method is limiting aText to aMaxLength and if
	 * necessary prepanding space from left to fit aMaxLength
	 * 
	 * @return a <code>String</code> value of new formated string
	 */
	private String limitString(String aText, int aMaxLength) {
		int lengthOfFilling = aMaxLength - aText.length();
		StringBuffer resultBuffer = new StringBuffer(aText);
		for (int i = 0; i < lengthOfFilling; i++) {
			resultBuffer.insert(0, ' ');
		}
		resultBuffer.setLength(aMaxLength);
		return new String(resultBuffer);
	}
	// ******************************************************************************
}
