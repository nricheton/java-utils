package nricheton.utils.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * This input streams removes invalid XML Characters from the stream. As a
 * result you should be able to read invalid documents.
 * <p>
 * Feel free to submit patchs on this class.
 * 
 * @author Nicolas Richeton
 */
public class XmlCleanupInputStream extends InputStream {

	private static final String ENCODING = "UTF-8";
	private InputStream originalIS;
	private BufferedReader originalReader;
	private byte[] buffer = new byte[0];
	private int position = 0;
	UnsupportedEncodingException error = null;

	public XmlCleanupInputStream(InputStream is) {
		originalIS = is;
		try {
			originalReader = new BufferedReader(new InputStreamReader(originalIS, ENCODING));
		} catch (UnsupportedEncodingException e) {
			error = e;
		}
	}

	@Override
	public int read() throws IOException {
		if (error != null) {
			throw new IOException(error);
		}

		if (buffer == null) {
			return -1;
		}

		while (position >= buffer.length) {
			String temp = originalReader.readLine();
			String temp2 = null;
			if (temp != null) {
				temp2 = temp.replaceAll("[^\\x20-\\x7e]", "");

				buffer = temp2.getBytes(ENCODING);
				position = 0;
			} else {
				buffer = null;
				break;
			}
		}

		if (buffer == null) {
			return -1;
		}

		int result = buffer[position];
		position++;

		return result;
	}

}
