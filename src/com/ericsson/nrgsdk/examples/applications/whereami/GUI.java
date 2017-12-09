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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * This class defines a simple graphical user interface. Users can add (tab)
 * pages and buttons.
 */
public class GUI extends JFrame {

	private JTabbedPane itsTabs = new JTabbedPane();

	private JPanel itsButtons = new JPanel();

	/**
	 * Creates a frame containing placeholders for (tab) pages and buttons.
	 */
	public GUI() {
		// construct the contents
		itsButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
		JPanel contents = new JPanel();
		contents.setLayout(new BorderLayout());
		contents.add(itsTabs, BorderLayout.CENTER);
		contents.add(itsButtons, BorderLayout.SOUTH);
		contents.setBorder(new EmptyBorder(8, 8, 0, 8));
		// add the contents to the frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contents, BorderLayout.CENTER);
		// the class that opens the window should close it as well
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Centers and shows the window
	 */
	public void showCentered() {
		pack();
		Dimension guiSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - guiSize.width) / 2,
				(screenSize.height - guiSize.height) / 2);
		setVisible(true);
	}

	/**
	 * Adds a tab page with a given title and contents.
	 * 
	 * @param aTitle
	 *            the name of the tab page
	 * @param aComp
	 *            the contents of the tab page (will be embedded in a
	 *            JScrollPane)
	 */
	public void addTab(String aTitle, Component aComp) {
		itsTabs.add(aTitle, new JScrollPane(aComp));
	}

	/**
	 * Adds a tab page with a given title and contents.
	 * 
	 * @param aTitle
	 *            the name of the tab page
	 * @param aText
	 *            the contents of the tab page
	 */
	public void addTab(String aTitle, String aText) {
		JTextArea text = new JTextArea(18, 50);
		text.setText(aText);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		text.setBorder(new EmptyBorder(4, 8, 4, 8));
		addTab(aTitle, text);
	}

	/**
	 * Adds a button.
	 * 
	 * @param anAction
	 *            the action to perform when the button is clicked
	 */
	public void addButton(Action anAction) {
		itsButtons.add(new JButton(anAction));
	}
}
