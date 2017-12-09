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

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.AbstractAction;

/**
 * This class is responsible for initializing, starting, stopping and
 * terminating the application.
 */
public class Main {

	private Feature itsFeature;

    /**
     * Method called by the JVM to launch the application.
     * @param args not used
     * @throws Exception 
     */
	public static void main(String[] args) throws Exception {
		new Main();
	}

    /**
     * Creates a new instance of <code>Main</code>.
     * @throws IOException when no configuration file is found, usally the configuration and 
     * additional resources is located under ./resources If this location cannot be used the
     * application will start to look for a configuration file located in the file system named as:
     * (packagename).ini for example if the application is of package com.ericsson.nrgsdk.examples.applications.callrouter
     * the searched ini file will be named callrouter.ini
     */
	public Main() throws IOException {
		Configuration.INSTANCE.load(this);
		GUI gui = new GUI();
		gui.addButton(new AbstractAction("Start") {

			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		itsFeature = new Feature(gui);
		gui.addButton(new AbstractAction("Stop") {

			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		gui.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				terminate();
			}
		});
		gui.showCentered();
	}

	/**
	 * Starts interaction with Ericsson Network Resource Gateway.
	 */
	public void start() {
		itsFeature.start();
	}

	/**
	 * Stops interaction with Ericsson Network Resource Gateway.
	 */
	public void stop() {
		itsFeature.stop();
	}

	/**
	 * Terminates the application.
	 */
	public void terminate() {
		System.exit(0);
	}
}
