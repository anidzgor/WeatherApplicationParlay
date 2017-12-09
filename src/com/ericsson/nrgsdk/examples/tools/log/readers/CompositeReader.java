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

package com.ericsson.nrgsdk.examples.tools.log.readers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class CompositeReader implements SpecializedObjectReader {

	private List theReaders = new ArrayList();

	public void register(SpecializedObjectReader aReader) {
		theReaders.add(0, aReader);
	}

	public void unregister(SpecializedObjectReader aReader) {
		theReaders.remove(aReader);
	}

	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		for (Iterator i = theReaders.iterator(); i.hasNext();) {
			SpecializedObjectReader reader = (SpecializedObjectReader) i.next();
			Object result = reader.getAny(aProps, aValue, aIsIndirected,
					aTargetClass);
			if (result != NOT_SUPPORTED) {
				return result;
			}
		}
		return NOT_SUPPORTED;
	}
}
