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

package com.ericsson.nrgsdk.examples.tools.configuration;

import java.util.Properties;

import com.ericsson.hosasdk.api.SDKException;
import com.ericsson.nrgsdk.examples.tools.log.Log;
import com.ericsson.nrgsdk.examples.tools.log.readers.ArrayReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.CompositeReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.PrimitiveReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.SpecializedObjectReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.StringReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.StructReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.TpAddressRangeReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.TpAddressReader;
import com.ericsson.nrgsdk.examples.tools.log.readers.TpHosaReader;

/**
 * Class <code>ObjectReader</code> is a place holder for a default instance of
 * a {@link SpecializedObjectReader}.
 * <p>
 * The pre-defined {@link #DEFAULT default instance}is suitable to read all
 * kinds of objects, in particular HOSA data types.
 */
public class ObjectReader extends CompositeReader {

	/**
	 * The default {@link SpecializedObjectReader}.
	 * <p>
	 * This is a
	 * {@link com.ericsson.nrgsdk.examples.tools.log.readers.CompositeReader}
	 * that has registered the following default instances of
	 * {@link com.ericsson.nrgsdk.examples.tools.log.readers.SpecializedObjectReader SpecializedObjectReaders}
	 * (in order of priority):
	 * <p>
	 * <list>
	 * <li>
	 * {@link com.ericsson.nrgsdk.examples.tools.log.readers.PrimitiveReader}
	 * </li>
	 * <li>{@link com.ericsson.nrgsdk.examples.tools.log.readers.StringReader}
	 * </li>
	 * <li>{@link TpAddressRangeReader}</li>
	 * <li>{@link TpAddressReader}</li>
	 * <li>{@link ArrayReader}</li>
	 * <li>{@link TpHosaReader}</li>
	 * <li>{@link StructReader}</li>
	 * </list>
	 * <p>
	 * If needed, users may modify this field..
	 */
	public static CompositeReader DEFAULT = new ObjectReader();
	static {
		DEFAULT.register(StructReader.INSTANCE);
		DEFAULT.register(TpHosaReader.INSTANCE);
		DEFAULT.register(ArrayReader.INSTANCE);
		DEFAULT.register(TpAddressReader.INSTANCE);
		DEFAULT.register(TpAddressRangeReader.INSTANCE);
		DEFAULT.register(StringReader.INSTANCE);
		DEFAULT.register(PrimitiveReader.INSTANCE);
	}

	/**
	 * Uses the {@link #DEFAULT default reader}to converts the textual
	 * representation of an object to an <code>Object</code>.
	 * 
	 * @param aProps
	 *            The properties that map parameter names to the values (used in
	 *            case of indirection).
	 * @param aValue
	 *            The parameter of which the value is to be converted or the
	 *            value that is to be converted (in case of no indirection).
	 * @param aTargetClass
	 *            The expected return type.
	 * @return The object that is represented by <code>aValue</code>.
	 * @throws SDKException
	 *             If <code>aTargetClass</code> is not supported.
	 */
	public static Object getAny(Properties aProps, String aValue,
			Class aTargetClass) {
		Object result = DEFAULT.getAny(aProps, aValue, true, aTargetClass);
		if (result == NOT_SUPPORTED) {
			throw new SDKException("Not supported (" + aTargetClass.getName()
					+ ")");
		}
		return result;
	}

	/**
	 * This method has the same behavior as in
	 * {@link com.ericsson.nrgsdk.examples.tools.log.readers.CompositeReader#getAny CompositeReader}.
	 * In addition, if aTargetClass is not a primitive, String or array type but
	 * has package prefix <code>java</code>,<code>javax</code> or
	 * <code>org.omg</code>,{@link #NOT_SUPPORTED NOT_SUPPORTED}is
	 * returned.
	 */
	public Object getAny(Properties aProps, String aValue,
			boolean aIsIndirected, Class aTargetClass) {
		Log.out.process("LOG", 5, "ObjectReader::getAny(" + aValue + ", "
				+ aTargetClass.getName() + ")");
		Object result = supported(aTargetClass) ? super.getAny(aProps, aValue,
				aIsIndirected, aTargetClass) : NOT_SUPPORTED;
		return result;
	}

	private boolean supported(Class aTargetClass) {
		return !(aTargetClass.getName().startsWith("java.")
				|| aTargetClass.getName().startsWith("javax.") || aTargetClass
				.getName().startsWith("org.omg."))
				|| aTargetClass.isPrimitive()
				|| aTargetClass == String.class
				|| aTargetClass.isArray();
	}
}
