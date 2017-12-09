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

import java.lang.reflect.Field;
import java.util.Properties;

import com.ericsson.hosasdk.api.SDKException;
import com.ericsson.hosasdk.api.TpAddressPlan;
import com.ericsson.hosasdk.api.TpAddressRange;

public class TpAddressRangeReader implements SpecializedObjectReader {

	public static final TpAddressRangeReader INSTANCE = new TpAddressRangeReader();

	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		if (aTargetClass != TpAddressRange.class) {
			return NOT_SUPPORTED;
		}
		String userString = aIsIndirected ? aProps.getProperty(aValue) : aValue;
		if (userString == null) {
			return null;
		}
		String[] tokens = { "", "", "", "" };
		int k = 0;
		for (int i = 0, j = 0; i != userString.length(); j = i + 1, k++) {
			i = userString.indexOf(",", j);
			if (i == -1) {
				i = userString.length();
			}
			String token = userString.substring(j, i).trim();
			if (k >= tokens.length) {
				throw new SDKException(
						"User configuration at line: '"
								+ userString
								+ "' is invalid. Need to specify at most six parameters");
			}
			tokens[k] = token;
		}
		return new TpAddressRange((TpAddressPlan) getField(tokens[1],
				TpAddressPlan.class, TpAddressPlan.P_ADDRESS_PLAN_E164),
				tokens[0], tokens[2], tokens[3]);
	}

	private static Object getField(String aFieldName, Class aSourceClass,
			Object aDefault) {
		if (aFieldName == null || aFieldName.length() == 0) {
			return aDefault;
		}
		try {
			Field myField = aSourceClass.getField(aFieldName);
			return myField.get(null);
		} catch (Exception e) {
			throw new SDKException("'" + aFieldName + "' is not a field of "
					+ aSourceClass.getName());
		}
	}
}
