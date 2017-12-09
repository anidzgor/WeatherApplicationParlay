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
import com.ericsson.hosasdk.api.TpAddressRange;
import com.ericsson.hosasdk.api.TpHosaDeliveryTime;
import com.ericsson.hosasdk.api.TpHosaMessage;
import com.ericsson.hosasdk.api.TpHosaSendMessageError;
import com.ericsson.hosasdk.api.TpHosaSendMessageReport;
import com.ericsson.hosasdk.api.TpHosaTerminatingAddressList;
import com.ericsson.hosasdk.api.TpHosaUIMessageDeliveryType;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManager;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManagerAdapter;
import com.ericsson.hosasdk.api.hui.IpHosaUIManager;
import com.ericsson.hosasdk.api.ui.IpAppUI;
import com.ericsson.hosasdk.api.ui.P_UI_RESPONSE_REQUIRED;
import com.ericsson.hosasdk.api.ui.TpUIEventCriteria;
import com.ericsson.hosasdk.api.ui.TpUIEventInfo;
import com.ericsson.hosasdk.api.ui.TpUIEventNotificationInfo;
import com.ericsson.hosasdk.api.ui.TpUIIdentifier;
import com.ericsson.nrgsdk.examples.tools.SDKToolkit;

/**
 * This class is responsible for:
 * <ul>
 * <li>Sending an SMS message.</li>
 * <li>Logging success or failure of sending an SMS message.</li>
 * <li>Forwarding a status (error or succes) when an SMS message is received to
 * the Feature.</li>
 * </ul>
 */
public class SMSProcessor extends IpAppHosaUIManagerAdapter implements
		IpAppHosaUIManager {

	private IpHosaUIManager itsHosaUIManager;

	private Feature itsParent;

	/**
	 * @param aHosaUIManager
	 *            manager used to talk to the Ericsson Network Resource Gateway
	 * @param aParent
	 *            the Parent to which this class can callback to
	 */
	public SMSProcessor(IpHosaUIManager aHosaUIManager, Feature aParent) {
		itsHosaUIManager = aHosaUIManager;
		itsParent = aParent;
	}

	/**
	 * @param aDestinationAddress
	 *            destination address where a notification will be created
	 * @return an assignment ID of the created notification.
	 */
	public int startNotifications(String aDestinationAddress) {
		IpAppHosaUIManager appHosaUIManager = this;
		TpAddressRange originatingAddress = null;
		TpAddressRange destinationAddress = SDKToolkit
				.createTpAddressRange(aDestinationAddress);
		String serviceCode = "00";
		TpUIEventCriteria criteria = new TpUIEventCriteria(originatingAddress,
				destinationAddress, serviceCode);
		int assignmentId = itsHosaUIManager.createNotification(
				appHosaUIManager, criteria);
		return assignmentId;

	}

	/**
	 * @param anAssignmentId
	 *            assignment from the notification that has to be stopped
	 */
	public void stopNotifications(int anAssignmentId) {
		itsHosaUIManager.destroyNotification(anAssignmentId);
	}

	/**
	 * Called by the Ericsson Network Resource Gateway when a notification is
	 * received.
	 * 
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 * @deprecated See Programmers' Guide
	 */
	
	public IpAppUI reportNotification(TpUIIdentifier anUserInteraction,
			TpUIEventInfo anEventInfo, int anAssignmentID) {
		String sender = anEventInfo.OriginatingAddress.AddrString;
		String receiver = anEventInfo.DestinationAddress.AddrString;
		String messageContent = anEventInfo.DataString;
		itsParent.smsReceived(sender, receiver, messageContent);
		return null;
	}

	/**
	 * Called by the Ericsson Network Resource Gateway when a notification is
	 * received.
	 * 
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 */
	public IpAppUI reportEventNotification(TpUIIdentifier anUserInteraction,
			TpUIEventNotificationInfo anEventInfo, int anAssignmentID) {
		String sender = anEventInfo.OriginatingAddress.AddrString;
		String receiver = anEventInfo.DestinationAddress.AddrString;
		String messageContent = new String(anEventInfo.UIEventData);
		itsParent.smsReceived(sender, receiver, messageContent);
		return null;
	}

	/**
	 * Send an SMS.
	 * 
	 * @param aSender
	 *            sender of the SMS
	 * @param aReceiver
	 *            receiver of the SMS
	 * @param aMessageContent
	 *            textual content of the SMS
	 */
	protected void sendSMS(String aSender, String aReceiver,
			String aMessageContent) {
		IpAppHosaUIManager appHosaUIManager = this;
		TpHosaUIMessageDeliveryType deliveryType = TpHosaUIMessageDeliveryType.P_HUI_SMS;
		// Create a dummy delivery time (send immediately)
		TpHosaDeliveryTime deliveryTime = new TpHosaDeliveryTime();
		deliveryTime.Dummy((short) 0);
		TpAddress originatingAddress = SDKToolkit.createTpAddress(aSender);
		TpAddress destinationAddress = SDKToolkit.createTpAddress(aReceiver);
		TpHosaTerminatingAddressList recipients = new TpHosaTerminatingAddressList();
		recipients.ToAddressList = new TpAddress[] { destinationAddress };
		TpHosaMessage message = new TpHosaMessage();
		message.Text(aMessageContent);
		// Send message
		itsHosaUIManager.hosaSendMessageReq(appHosaUIManager, // callback
				originatingAddress, recipients, null, // subject (ignored // for SMS)
				message, deliveryType, Configuration.INSTANCE.getBillingInformation(), // billingID (operator // defined)
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
		System.out.println("\nError sending the SMS to "
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
		System.out.println("\nSMS Message sent to "
				+ aResponseList[0].UserAddress.AddrString);
	}
}
