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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * This class is responsible for initializing, starting, stopping and
 * terminating the application.
 */
public class Plotter {

	private BufferedImage bi;

	private Graphics2D g;

	public Plotter(int aWidth, int aHeight) {
		bi = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_3BYTE_BGR);
		g = bi.createGraphics();
	}

	public void drawImage(Image anImage, int x, int y, ImageObserver anObserver) {
		g.drawImage(anImage, x, y, anObserver);
	}

	public byte[] jpegEncoding() throws Exception {
		ByteArrayOutputStream f = new ByteArrayOutputStream();
		JPEGEncodeParam parms = JPEGCodec.getDefaultJPEGEncodeParam(bi);
		parms.setQuality(0.5f, true);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(f, parms);
		encoder.encode(bi);
		byte[] r = f.toByteArray();
		f.close();
		return r;
	}

	public DataSource createDataSource() throws Exception {
		return new RawDataSource("image/jpeg", jpegEncoding());
	}
}

class RawDataSource implements DataSource {

	String type;

	byte[] buffer;

	public RawDataSource(String type, byte[] buffer) {
		this.type = type;
		this.buffer = buffer;
	}

	public String getContentType() {
		return type;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(buffer);
	}

	public String getName() {
		return toString();
	}

	public OutputStream getOutputStream() {
		throw new RuntimeException("Not supported");
	}
}
