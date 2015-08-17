package mas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import mas.git.GitUtils;

/**
 * @author SCAREX
 *
 */
public class LangCheckRunnable implements Runnable
{
	protected final File folder;
	protected final Locale locale;

	public LangCheckRunnable(File folder, Locale locale) {
		this.folder = folder;
		this.locale = locale;
	}

	@Override
	public void run() {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL("https://raw.githubusercontent.com/SCAREXgaming/MASLang/master/" + this.locale + ".lang").openConnection();
			File f = new File(this.folder, this.locale + ".lang");
			byte[] fh = GitUtils.getGitHash(new FileInputStream(f), f.length());
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				if (!Arrays.equals(fh, IOUtils.hexToByteArray(con.getHeaderField("Etag").substring(1, con.getHeaderField("Etag").length() - 1)))) {
					IOUtils.copyURLToFile(con.getURL(), f);
					System.out.println("Lang file " + this.locale + ".lang is updated");
				}
			} else if (con.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				System.err.println("A custom lang file as been detected, you can send pull requests at https://github.com/SCAREXgaming/MASLang to help");
			} else {
				System.err.println("Unhandled exception for " + this.locale + " : ");
				System.err.println(con.getResponseCode() + " : " + con.getResponseMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Cannot generate hash with SHA1, change your pc !");
		}
	}
}
