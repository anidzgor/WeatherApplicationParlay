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

import java.text.MessageFormat;

// ******************************************************************************
/**
 * The ConsoleOutput is responsible for writing output messages to the console
 * (standard error output).
 */
public class ConsoleOutput implements OutputStream {

	/** The format for outputting messages. */
	private static final MessageFormat itsFormat = new MessageFormat(
			"{0} [{1}]: {2}\n");

	// ------------------------------------------------------------------------------
	/**
	 * Creates a console output object. The object will output to the standard
	 * error stream
	 */
	public ConsoleOutput() {// empty
	}

	// ------------------------------------------------------------------------------
	/** @see OutputStream#output(String,int,String) */
	public synchronized void output(String aType, int aLevel, String aMessage) {
		Object[] items = { aType, new Integer(aLevel), aMessage };
		System.err.print(itsFormat.format((Object) items));
		System.err.flush();
	}
	// ******************************************************************************
}
