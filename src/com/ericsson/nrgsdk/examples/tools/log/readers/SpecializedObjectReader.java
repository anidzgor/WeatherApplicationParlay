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

import java.util.Properties;

import com.ericsson.hosasdk.impl.NamedObject;

/**
 * A parser that is capable of converting the textual representation of an
 * object into an <code>Object</code>.
 */
public interface SpecializedObjectReader {

	/**
	 * The return value that indicates that <code>SpecializedObjectReader</code>
	 * does not support a specified class.
	 */
	public static final Object NOT_SUPPORTED = new NamedObject("NOT_SUPPORTED");

	/**
	 * Converts the textual representation of an object to an
	 * <code>Object</code>.
	 * 
	 * @param aProperties
	 *            The properties that map parameter names to the values (used in
	 *            case of indirection).
	 * @param aValue
	 *            The parameter of which the value is to be converted (in case
	 *            of indirection) or the value that is to be converted (in case
	 *            of no indirection).
	 * @param aIsIndirected
	 *            Indicates if indirection applies.
	 * @param aTargetClass
	 *            The expected return type.
	 * @return The object that is represented by <code>aValue</code>, or
	 *         {@link #NOT_SUPPORTED}is this SpecializedWriter is not intended
	 *         to convert to instances of <code>aTargetClass</code>.
	 */
	public Object getAny(Properties aProperties, String aValue,
			boolean aIsIndirected, Class aTargetClass);
}
