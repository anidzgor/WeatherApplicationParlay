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

import com.ericsson.hosasdk.api.TpAddress;
import com.ericsson.hosasdk.api.TpHosaDeliveryTime;
import com.ericsson.hosasdk.api.TpHosaMessage;
import com.ericsson.hosasdk.api.TpHosaSendMessageError;
import com.ericsson.hosasdk.api.TpHosaSendMessageReport;
import com.ericsson.hosasdk.api.TpHosaTerminatingAddressList;
import com.ericsson.hosasdk.api.TpHosaUIMessageDeliveryType;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManager;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManagerAdapter;
import com.ericsson.hosasdk.api.hui.IpHosaUIManager;
import com.ericsson.hosasdk.api.ui.P_UI_RESPONSE_REQUIRED;
import com.ericsson.nrgsdk.examples.tools.SDKToolkit;

/**
 * This class is responsible for:
 * <ul>
 * <li>Sending an MMS message.</li>
 * <li>Logging success or failure of sending an MMS message.</li>
 * </ul>
 */
public class MMSProcessor extends IpAppHosaUIManagerAdapter implements
		IpAppHosaUIManager {

	private IpHosaUIManager itsHosaUIManager;

	/**
	 * @param aHosaUIManager
	 *            manager used to talk to the Ericsson Network Resource Gateway
	 * @param aParent
	 *            the Parent to which this class can callback to
	 */
	public MMSProcessor(IpHosaUIManager aHosaUIManager, Feature aParent) {
		itsHosaUIManager = aHosaUIManager;
	}

	/**
	 * Send an MMS.
	 * 
	 * @param aSender
	 *            sender of the MMS
	 * @param aReceiver
	 *            receiver of the MMS
	 * @param aMessageContent
	 *            message as a byte array
	 * @param aMessageSubject
	 *            subject of the MMS
	 */
	protected void sendMMS(String aSender, String aReceiver,
			byte[] aMessageContent, String aMessageSubject) {
		IpAppHosaUIManager appHosaUIManager = this;
		TpHosaUIMessageDeliveryType deliveryType = TpHosaUIMessageDeliveryType.P_HUI_MMS;
		// Create a dummy delivery time (send immediately)
		TpHosaDeliveryTime deliveryTime = new TpHosaDeliveryTime();
		deliveryTime.Dummy((short) 0);
		TpAddress originatingAddress = SDKToolkit.createTpAddress(aSender);
		TpAddress destinationAddress = SDKToolkit.createTpAddress(aReceiver);
		TpHosaTerminatingAddressList recipients = new TpHosaTerminatingAddressList();
		recipients.ToAddressList = new TpAddress[] { destinationAddress };
		TpHosaMessage message = new TpHosaMessage();
		message.BinaryData(aMessageContent);
		// Send message
		itsHosaUIManager.hosaSendMessageReq(appHosaUIManager, // callback
				originatingAddress, recipients, aMessageSubject, message,
				deliveryType, Configuration.INSTANCE.getBillingInformation(), // billingID
				// (operator
				// defined)
				P_UI_RESPONSE_REQUIRED.value, false, // deliveryNotificationRequested
				deliveryTime, // deliveryTime (not applicable)
				""); // validityTime (not applicable)
	}

	/**
	 * Called by the Ericsson Network Resource Gateway when something went wrong
	 * sending the message.
	 * 
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 */
	public void hosaSendMessageErr(int anAssignmentID,
			TpHosaSendMessageError[] anErrorList) {
		System.out.println("\nError sending the MMS to "
				+ anErrorList[0].UserAddress.AddrString + "(ErrorCode "
				+ anErrorList[0].Error.value() + ")");
	}

	/**
	 * Called by the Ericsson Network Resource Gateway when sending the message
	 * is a succes.
	 * 
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 */
	public void hosaSendMessageRes(int anAssignmentID,
			TpHosaSendMessageReport[] aResponseList) {
		System.out.println("\nMMS Message sent to "
				+ aResponseList[0].UserAddress.AddrString);
	}
}
