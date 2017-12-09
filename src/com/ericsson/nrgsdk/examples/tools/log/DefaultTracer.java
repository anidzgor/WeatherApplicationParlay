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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.hosasdk.api.HOSAListener;
import com.ericsson.nrgsdk.examples.tools.SDKToolkit;

/**
 * This class creates a logging mechanism that collects the input and output of
 * Parlay applications using the Ericsson HOSA interfaces. Invocation received
 * from the gateway will be marked as <code>Received</code> while requests
 * directed towards the gateway from the application will be marked as
 * <code>Sending</code> in the log.
 *  
 */
public class DefaultTracer implements HOSAListener {

	private DateFormat DATE_FORMAT;

	private String OUTPUT = "Sending";

	private String INPUT = "Received";

	private Map theClassNameCache = new HashMap();

	/**
	 * Constructor for the class creating a default timestamp as "HH:mm:ss" and
	 */
	public DefaultTracer() {
		this("HH:mm:ss");
	}

	/**
	 * Constructor for the class creating a default timestamp as specified in
	 * the String parsed as a SimpleDateFormat
	 * 
	 * @param aDateFormat
	 *            the SimpleDateFormat to be used like e.g. "HH:mm:ss"
	 */
	public DefaultTracer(String aDateFormat) {
		this(new SimpleDateFormat("HH:mm:ss"));
	}

	/**
	 * Constructor for the class creating a default timestamp as specified in
	 * the SimpleDateFormat object
	 * 
	 * @param aDateFormat
	 *            the SimpleDateFormat to be used
	 */
	public DefaultTracer(DateFormat aDateFormat) {
		System.out.println("Starting HOSA Tracing");
		DATE_FORMAT = aDateFormat;
	}

	private String getTime() {
		return ("[" + DATE_FORMAT.format(new Date()) + "]");
	}

	private void printCheck(String aKind, String aMethod, Object aTarget,
			Object[] aArgument) {
		System.out.println("\n" + getTime() + " " + aKind + " "
				+ getObjectName(aTarget) + "." + aMethod
				+ getArguments(aArgument));
	}

	private String getObjectName(Object aObject) {
		Class aClass = aObject.getClass();
		String aClassName = (String) theClassNameCache.get(aClass);
		if (aClassName == null) {
			aClassName = aClass.toString();
			aClassName = aClassName.substring(aClassName.lastIndexOf(".") + 1);
			int implPos = aClassName.lastIndexOf("Impl");
			if (implPos > 0) {
				aClassName = aClassName.substring(0, implPos);
			}
			theClassNameCache.put(aClass, aClassName);
		}
		return aClassName;
	}

	private String getArguments(Object[] aArguments) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < aArguments.length; i++) {
			result.append("\n\t" + (i + 1) + ": type:"
					+ getObjectName(aArguments[i]) + " value:"
					+ SDKToolkit.OBJECTWRITER.print(aArguments[i]));
		}
		return result.toString();
	}

	/**
	 * @see com.ericsson.hosasdk.api.HOSAListener#receivedRequest(java.lang.Object,
	 *      java.lang.String, java.lang.Object[])
	 */
	public void receivedRequest(Object aTarget, String aMethod,
			Object[] aArguments) {
		printCheck(INPUT, aMethod, aTarget, aArguments);
	}

	/**
	 * @see com.ericsson.hosasdk.api.HOSAListener#sendingRequest(java.lang.Object,
	 *      java.lang.String, java.lang.Object[])
	 */
	public void sendingRequest(Object aTarget, String aMethod,
			Object[] aArguments) {
		printCheck(OUTPUT, aMethod, aTarget, aArguments);
	}

	/**
	 * @see com.ericsson.hosasdk.api.HOSAListener#receivedResponse(java.lang.Object,
	 *      java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public void receivedResponse(Object aTarget, String aMethod,
			Object[] aArguments, Object aResult) {
		if (aResult instanceof Exception) {
			printCheck(INPUT, aMethod, aTarget, aArguments);
		}
	}

	/**
	 * @see com.ericsson.hosasdk.api.HOSAListener#sendingResponse(java.lang.Object,
	 *      java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public void sendingResponse(Object aTarget, String aMethod,
			Object[] aArguments, Object aResult) {
		if (aResult instanceof Exception) {
			printCheck(OUTPUT, aMethod, aTarget, aArguments);
		}
	}
}
