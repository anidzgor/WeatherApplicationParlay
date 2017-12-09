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

import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.ericsson.hosasdk.api.HOSAMonitor;
import com.ericsson.hosasdk.api.TpAddress;
import com.ericsson.hosasdk.api.TpHosaSendMessageError;
import com.ericsson.hosasdk.api.TpHosaSendMessageReport;
import com.ericsson.hosasdk.api.TpHosaUIMessageDeliveryStatus;
import com.ericsson.hosasdk.api.fw.P_UNKNOWN_SERVICE_TYPE;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManager;
import com.ericsson.hosasdk.api.hui.IpHosaUIManager;
import com.ericsson.hosasdk.api.mm.ul.IpUserLocation;
import com.ericsson.hosasdk.api.ui.IpAppUI;
import com.ericsson.hosasdk.api.ui.TpUIEventInfo;
import com.ericsson.hosasdk.api.ui.TpUIEventNotificationInfo;
import com.ericsson.hosasdk.api.ui.TpUIIdentifier;
import com.ericsson.hosasdk.utility.framework.FWproxy;
import com.ericsson.nrgsdk.examples.tools.SDKToolkit;
/**
 * This class implements the logic of the application. It uses processors to
 * interact with Ericsson Network Resource Gateway.
 */
public class Feature{

	private FWproxy itsFramework;

	private IpHosaUIManager itsHosaUIManager;

	private IpUserLocation itsOsaULManager;

	private SMSProcessor itsSMSProcessor;

	private MMSProcessor itsMMSProcessor;

	private LocationProcessor itsLocationProcessor;

	private GUI theGUI;

	private Integer assignmentId;
	
	private Service service;
	private ArrayList<User> allUsers; // lista wszystkich abonentów

	/**
	 * Initializes a new instance, without starting interaction with Ericsson
	 * Network Resource Gateway (see start)
	 * 
	 * @param aGUI
	 *            the GUI of the application
	 */
	public Feature(GUI aGUI) {
		theGUI = aGUI;
		aGUI.setTitle("Weather service");
		aGUI.addTab("Description", getDescription());

	}

	/**
	 * Starts interaction with the Ericsson Network Resource Gateway. Note: this
	 * method is intended to be called at most once.
	 */
	protected void start() {
		HOSAMonitor.addListener(SDKToolkit.LOGGER);
		itsFramework = new FWproxy(Configuration.INSTANCE);
        try
        {
    		itsHosaUIManager = (IpHosaUIManager) itsFramework
    				.obtainSCF("SP_HOSA_USER_INTERACTION");
    		itsOsaULManager = (IpUserLocation) itsFramework
    				.obtainSCF("P_USER_LOCATION");
        }
        catch (P_UNKNOWN_SERVICE_TYPE anException)
        {
            System.err.println("Service not found. Please refer to the Ericsson Network Resource Gateway User Guide for "
                            + "a list of which applications that are able to run on which test tools\n"
                            + anException);
        }
		itsSMSProcessor = new SMSProcessor(itsHosaUIManager, this);
		itsMMSProcessor = new MMSProcessor(itsHosaUIManager, this);
		itsLocationProcessor = new LocationProcessor(itsOsaULManager, this);
		System.out.println("Starting SMS notification");
		assignmentId = new Integer(itsSMSProcessor.startNotifications(Configuration.INSTANCE.getProperty("serviceNumber")));
		
		allUsers = new ArrayList<User>();
	    service = new Service(this);
	}

	/**
	 * Stops interaction with the Ericsson Network Resource Gateway and disposes
	 * of all resources allocated by this instance. Note: this method is
	 * intended to be called at most once.
	 */
	public void stop() {
		System.out.println("Stopping SMS notification");
		if (assignmentId != null) {
			itsSMSProcessor.stopNotifications(assignmentId.intValue());
		}
		assignmentId = null;
		System.out.println("Disposing processor");
		if (itsSMSProcessor != null) {
			itsSMSProcessor.dispose();
		}
		if (itsMMSProcessor != null) {
			itsMMSProcessor.dispose();
		}
		if (itsLocationProcessor != null) {
			itsLocationProcessor.dispose();
		}
		System.out.println("Disposing service manager");
		if (itsHosaUIManager != null) {
			itsFramework.releaseSCF(itsHosaUIManager);
		}
		if (itsOsaULManager != null) {
			itsFramework.releaseSCF(itsOsaULManager);
		}
		System.out.println("Disposing framework");
		if (itsFramework != null) {
			itsFramework.endAccess();
			itsFramework.dispose();
		}
		System.out.println("Stopping Parlay tracing");
		HOSAMonitor.removeListener(SDKToolkit.LOGGER);
		System.exit(0);
	}

	/**
	 * Invoked by the SMSProcessor, when a notification is received.
	 * @throws Exception 
	 */
	protected void smsReceived(String aSender, String aReceiver,
			String aMessageContent) {
		System.out.println("Odebrano SMS-a o treœci: " + aMessageContent);		
		
		User user = checkList(aSender);		
		//Rejestracja u¿ytkownika
		if (aMessageContent.toLowerCase().equals("rejestracja") && user == null ) {
			user = new User(aSender, itsLocationProcessor);
			service.addUser(user);
			System.out.println("Dodano u¿ytkownika o numerze: " + user.getNumer());
			itsSMSProcessor.sendSMS(Configuration.INSTANCE.getProperty("serviceNumber"), aSender, "Jestes nowym uzytkownikiem serwisu");
		} else if(aMessageContent.toLowerCase().equals("rejestracja") && user != null) {
			itsSMSProcessor.sendSMS(Configuration.INSTANCE.getProperty("serviceNumber"), aSender, "Nie musisz sie rejestrowac, jestes juz czlonkiem serwisu");
		}
		
		//Sprawdzenie pogody przez u¿ytkownika zarejestrowanego w serwisie
		if (aMessageContent.toLowerCase().equals("pogoda") && user != null ) {
			user.checkLocalization();			
		}
	
		//Zapisanie sie do subskrypcji pogodowej
		if (aMessageContent.toLowerCase().equals("subskrypcja") && user != null ) {
			user.start();			
		}		
		
		//Zapisanie sie do subskrypcji pogodowej
		if (aMessageContent.toLowerCase().equals("odsubskrybuj") && user != null ) {
			user.stop();			
		}	
	}

	private User checkList(String numer)
	{
		for (User a : service.getUserOfService())
			if (a.getNumer().equalsIgnoreCase(numer))
				return a;
		
		return null;	
	}

	public void locationReceived(String user, float latitude, float longitude) {
		try {
			
			//Map
			ImageIcon map = Configuration.INSTANCE.getImage("map.gif");
			int wm = map.getIconWidth();
			int hm = map.getIconHeight();
			
			//Phone
			ImageIcon phone = Configuration.INSTANCE.getImage("phone.png");
			int wp = phone.getIconWidth();
			int hp = phone.getIconHeight();
			
			if (latitude < 0) {
				latitude = 0;
			}
			if (latitude > 1) {
				latitude = 1;
			}
			if (longitude < 0) {
				longitude = 0;
			}
			if (longitude > 1) {
				longitude = 1;
			}
			
			int x = (int) (latitude * wm - wp / 2);
			int y = (int) (longitude * hm - hp / 2);
			Plotter plotter = new Plotter(wm, hm);
			plotter.drawImage(map.getImage(), 0, 0, theGUI);
			plotter.drawImage(phone.getImage(), x, y, theGUI);
			
			MMSMessageContent messageContent = new MMSMessageContent();
			messageContent.addMedia(plotter.createDataSource());
			itsMMSProcessor.sendMMS(Configuration.INSTANCE.getProperty("serviceNumber"), user, messageContent
					.getBinaryContent(), "Current location");

			if(latitude < 0.5 && longitude < 0.5) {
				System.out.println("S³onecznie");
			}
			else if(latitude >= 0.5 && longitude < 0.5) {
				System.out.println("Pada");
			}
			else if(latitude < 0.5 && longitude >= 0.5) {
				System.out.println("Burza");
			}
			else if(latitude >= 0.5 && longitude >= 0.5) {
				System.out.println("Œnieg");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return a descriptive text that explains the application and its
	 *         configuration.
	 */
	private String getDescription() {
		String s = "Nacisnij START, aby sie polaczyc z symulatorem";
		s += "\n";
		s += "Uzytkownik moze wysylac SMS na numer " + Configuration.INSTANCE.getProperty("serviceNumber") + " z nastepujacymi poleceniami ";
		s += "\n-------------------------------------------\n";
		
		s += "\"rejestracja\" pozwala uzytkownikowi na zarejestrowanie sie w serwisie pogodowym \n";
		s += "\"pogoda\" pozwala uzytkownikowi na sprawdzenie w swoim rejonie pogody \n";
		s += "\"subskrypcja\" pozwala uzytkownikowi na subskrypcje pogody \n";
		s += "\"odsubskrybuj\" pozwala uzytkownikowi na zrezygnowanie z dostawania powiadomieñ o pogodzie \n";

		s += "\n-------------------------------------------\n";
		s += "Nacisnij STOP, aby zatrzymac aplikacje.\n";
		return s;
	}


}
