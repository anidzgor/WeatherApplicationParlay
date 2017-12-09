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

package com.ericsson.nrgsdk.examples.tools;

import com.ericsson.hosasdk.api.TpAddress;
import com.ericsson.hosasdk.api.TpAddressPlan;
import com.ericsson.hosasdk.api.TpAddressPresentation;
import com.ericsson.hosasdk.api.TpAddressRange;
import com.ericsson.hosasdk.api.TpAddressScreening;
import com.ericsson.nrgsdk.examples.tools.configuration.writers.ObjectWriter;
import com.ericsson.nrgsdk.examples.tools.log.DefaultTracer;

/**
 * Utility functions for building example applications for the Ericsson Network
 * Resource Gateway
 */
public class SDKToolkit {

	/**
	 * @param aNumber
	 *            A String from which a TpAddressPlan is created. Numbers with
	 *            <code>sip:</code> or <code>tel:</code> will be specified
	 *            as <code>P_ADDRESS_PLAN_SIP</code>. All number strings will
	 *            generate a <code>P_ADDRESS_PLAN_E164</code> while all others
	 *            will generate a <code>P_ADDRESS_PLAN_UNDEFINED</code>.
	 * @return A default TpAddress, based on the number provided.
	 *         P_ADDRESS_PRESENTATION_UNDEFINED will be
	 *         used as TpAddressPresentation  as well as a TpAddressScreening will be set to
	 *         P_ADDRESS_SCREENING_UNDEFINED
	 */
	public static TpAddress createTpAddress(String aNumber) {
		return new TpAddress(determineAddressPlan(aNumber), aNumber, // address
				"", // name
				TpAddressPresentation.P_ADDRESS_PRESENTATION_UNDEFINED,
				TpAddressScreening.P_ADDRESS_SCREENING_UNDEFINED, ""); // subaddress
	}

	/**
	 * 
	 * @param aNumberOrNumberRange
	 *            A String from which a TpAddressPlan is created. Numbers with
	 *            <code>sip:</code> or <code>tel:</code> will be specified as
	 *            <code>P_ADDRESS_PLAN_SIP</code>. All number strings will
	 *            generate a <code>P_ADDRESS_PLAN_E164</code> while
	 *            all others will generate a <code>P_ADDRESS_PLAN_UNDEFINED</code>.
	 * @return A TpAddressPlan, based on an address string format.
	 */
	private static TpAddressPlan determineAddressPlan(
			String aNumberOrNumberRange) {
		if (aNumberOrNumberRange.toLowerCase().indexOf("sip:") != -1)
			return TpAddressPlan.P_ADDRESS_PLAN_SIP;
		if (aNumberOrNumberRange.toLowerCase().indexOf("tel:") != -1)
			return TpAddressPlan.P_ADDRESS_PLAN_SIP;
		if(allNumbers(aNumberOrNumberRange))
			return TpAddressPlan.P_ADDRESS_PLAN_E164;
		else
			return TpAddressPlan.P_ADDRESS_PLAN_UNDEFINED;
	}
	
	/**
	 * Checks if the supplied string is a number
	 * @param anAddress the number to check
	 * @return true if the supplied string is all numbers, else false if 
	 * the string cannot be parsed
	 */
	private static boolean allNumbers(String anAddress) {
		try {
			Long.parseLong(anAddress);
			return true;

		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * @param aNumberRange
	 *            A String from which a TpAddressPlan is created. Numbers with
	 *            <code>sip:</code> or <code>tel:</code> will be specified
	 *            as <code>P_ADDRESS_PLAN_SIP</code>. All number strings will
	 *            generate a <code>P_ADDRESS_PLAN_E164</code> while all others
	 *            will generate a <code>P_ADDRESS_PLAN_UNDEFINED</code>.
	 * @return A default TpAddressRange, based on an address string range.
	 */
	public static TpAddressRange createTpAddressRange(String aNumberRange) {
		return new TpAddressRange(determineAddressPlan(aNumberRange),
				aNumberRange, // address
				"", // name
				""); // subaddress
	}

	/**
	 * Collects all input and output sent between the application and the
	 * Ericsson Network Resource Gateway.
	 * 
	 * @see com.ericsson.nrgsdk.examples.tools.log.DefaultTracer
	 */
	public static final DefaultTracer LOGGER = new DefaultTracer();

	/**
	 * This instance is used to convert objects to String values according a
	 * predefined template.
	 * 
	 * @see com.ericsson.nrgsdk.examples.tools.configuration.writers.ObjectWriter
	 */
	public static final ObjectWriter OBJECTWRITER = new ObjectWriter();
}
