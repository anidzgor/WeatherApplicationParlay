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

package com.ericsson.nrgsdk.examples.applications.whereami;

import java.io.IOException;
import com.ericsson.nrgsdk.examples.tools.configuration.NestedProperties;
import com.ericsson.nrgsdk.examples.tools.configuration.NotFoundException;

/**
 * This class is responsible for parsing the configuration file and presenting
 * the configuration data using convenient data types.
 */
public class Configuration extends NestedProperties {

    /** The singleton instance that can be accessed by all users. */
	public static final Configuration INSTANCE = new Configuration();

	private static String itsBillingInformation;

	/**
	 * Private constructor to prevent multiple instances.
	 */
	private Configuration() {
		//void
	}

	/**
	 * Initializes an instance.
	 * 
	 * @param aSource
	 *            the object who's package will determine the configuration file
	 *            name
	 * @exception IOException
	 *                if the file could not be loaded
	 * @exception IOException
	 *                if an expected configuration could not be found
	 */
	public void load(Object aSource) throws IOException {
		super.load(aSource);
		itsBillingInformation = loadString("billingInformation");
	}

	private String loadString(String aParm) {
		String s = getProperty(aParm);
		if (s == null) {
			throw new NotFoundException(aParm);
		}
		return s;
	}

	public String getBillingInformation() {
		return itsBillingInformation;
	}

	/**
	 * A convenience class for grouping Subscriber-releated information
	 */
	public static class Subscriber {

		public String phoneNumber;

		public String[] friends;

		public String contentDir = "content/";

		public Integer assignmentId;
	}
}
