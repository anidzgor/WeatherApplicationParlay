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

import java.util.HashMap;

// ******************************************************************************
/**
 * The OutputFilter class is responsible for filtering messages to an output
 * stream. For each type of message it defines a minimum priority level, above
 * which messsages are filtered out.
 */
public class OutputFilter {

	/** The priority levels for each type. */
	private HashMap itsLevels;

	/** The default to use when a type is not known. */
	private boolean itsDefaultFlag;

	// ------------------------------------------------------------------------------
	/**
	 * Creates an OutputFilter object with an empty type database. aDefaultFlag
	 * indicates whether unknown types must be output or not.
	 * 
	 * @param aDefaultFlag
	 *            output unknown message types if true
	 */
	public OutputFilter(boolean aDefaultFlag) {
		itsLevels = new HashMap();
		itsDefaultFlag = aDefaultFlag;
	}

	// ------------------------------------------------------------------------------
	/**
	 * Adds aType to the database. The minimum level is set to aLevel. If aType
	 * already exists in the database, it is overwritten.
	 * 
	 * @param aType
	 *            the type of message
	 * @param aLevel
	 *            the minimum priority level
	 */
	public void addType(String aType, int aLevel) {
		itsLevels.put(aType, new Integer(aLevel));
	}

	// ------------------------------------------------------------------------------
	/**
	 * Removes aType from the database. If aType does not exist in the database,
	 * nothing is done.
	 * 
	 * @param aType
	 *            the type of message
	 */
	public void removeType(String aType) {
		itsLevels.remove(aType);
	}

	// ------------------------------------------------------------------------------
	/**
	 * Returns whether the message with aType and aLevel must be logged.
	 * 
	 * @param aType
	 *            the type of message
	 * @param aLevel
	 *            the priority level
	 * @return true if the message must be logged, false otherwise
	 */
	public boolean logIt(String aType, int aLevel) {
		Integer level = (Integer) itsLevels.get(aType);
		return level != null ? aLevel <= level.intValue() : itsDefaultFlag;
	}
	// ******************************************************************************
}
