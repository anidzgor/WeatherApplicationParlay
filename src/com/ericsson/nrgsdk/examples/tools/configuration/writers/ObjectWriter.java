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

package com.ericsson.nrgsdk.examples.tools.configuration.writers;

/**
 * In order to print objects of various types as a string on standard-out a
 * number of writers are created. These writers are put together in this
 * composite writer to create a convinient way of printing objects.
 */
public class ObjectWriter {

	private CompositeWriter aCompositeWriter;

	/**
	 * This is the default constructor. It creates writers to handle: Structs,
	 * IpInterfaces, Arrays, Unions, Enumerations, Objects, Exceptions, Strings
	 * and Null.
	 */
	public ObjectWriter() {
		aCompositeWriter = new CompositeWriter();
		aCompositeWriter.register(new CatchAllWriter());
		aCompositeWriter.register(new StructWriter());
		aCompositeWriter.register(new IpInterfaceWriter());
		aCompositeWriter.register(new ArrayWriter());
		aCompositeWriter.register(new UnionWriter());
		aCompositeWriter.register(new EnumWriter());
		aCompositeWriter.register(new SimpleObjectWriter());
		aCompositeWriter.register(new ExceptionWriter());
		aCompositeWriter.register(new StringWriter());
		aCompositeWriter.register(new NullWriter());
	}

	/**
	 * This method converts objects of different kinds to a String value.
	 * 
	 * @param theObjectToPrint
	 *            the object to convert
	 * @return the text representation
	 */
	public String print(Object theObjectToPrint) {
		StringBuffer aStringBuffer = new StringBuffer();
		aCompositeWriter.append(theObjectToPrint, aStringBuffer);
		return aStringBuffer.toString();
	}
}
