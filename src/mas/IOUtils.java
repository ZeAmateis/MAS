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

public class IOUtils {

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
        if (input == null)
            throw new FileNotFoundException("No file found in classpath: /" + classpathLoc);
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
}
