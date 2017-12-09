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

package com.ericsson.nrgsdk.examples.tools.configuration;

/**
 * An exception that can be used to indicate that a particular parameter is
 * provided with an syntactically incorrect value.
 */
public class BadSyntaxException extends RuntimeException {

	/**
	 * @param aParm
	 *            the name of the parameter
	 */
	public BadSyntaxException(String aParm) {
		super("Incorrect value for parameter '" + aParm + "'.");
	}

	/**
	 * @param aParm
	 *            the name of the parameter
	 * @param aValue
	 *            the value
	 */
	public BadSyntaxException(String aParm, String aValue) {
		super("Incorrect value for parameter '" + aParm + "' ('" + aValue
				+ "').");
	}
}
