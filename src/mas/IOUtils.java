package mas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IOUtils
{
	public static final char[] HEX_ARRAY = {
			'0', '1', '2',
			'3', '4', '5',
			'6', '7', '8',
			'9', 'A', 'B',
			'C', 'D', 'E',
			'F' };

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[4096];
		int i;
		while ((i = in.read(buffer)) != -1) {
			out.write(buffer, 0, i);
		}
		out.flush();
	}

	public static String inputStreamToString(InputStreamReader in) throws IOException {
		StringBuilder b = new StringBuilder();
		char[] buffer = new char[4096];
		int i;
		while ((i = in.read(buffer)) != -1) {
			b.append(buffer, 0, i);
		}
		return b.toString();
	}

	public static String read(String classpathLoc, String charset) throws IOException {
		InputStream input = IOUtils.class.getResourceAsStream("/" + classpathLoc);
		if (input == null) throw new FileNotFoundException("No file found in classpath: /" + classpathLoc);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(input, out);
		input.close();
		out.close();
		return new String(out.toByteArray(), charset);
	}

	public static void copyURLToFile(URL url, File output) throws IOException {
		InputStream in = url.openStream();
		FileOutputStream out = new FileOutputStream(output);
		IOUtils.copy(in, out);
		out.close();
		in.close();
	}

	public static byte[] getHash(InputStream is, String hash) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(hash);
		byte[] buffer = new byte[4096];
		int len;
		while ((len = is.read(buffer)) != -1) {
			md.update(buffer, 0, len);
		}
		return md.digest();
	}

	public static String byteToHex(byte[] ab) {
		char[] hc = new char[ab.length * 2];
		for (int i = 0; i < ab.length; i++) {
			int b = ab[i] & 0xFF;
			hc[i * 2] = HEX_ARRAY[b >>> 4];
			hc[i * 2 + 1] = HEX_ARRAY[b & 0xF];
		}
		return new String(hc);
	}

	public static byte[] hexToByteArray(String s) {
		byte[] ab = new byte[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {
			ab[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return ab;
	}
}
