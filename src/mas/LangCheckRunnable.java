package mas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import mas.git.GitUtils;
import mas.git.GitUtils.GitContent;
import mas.git.GitUtils.GitContentDeserializer;

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
			HttpURLConnection con = (HttpURLConnection) new URL("https://api.github.com/repos/SCAREXgaming/MASLang/contents/" + locale + ".lang").openConnection();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				GsonBuilder builder = new GsonBuilder();
				builder.registerTypeAdapter(GitContent.class, new GitContentDeserializer());
				Gson gson = builder.create();
				GitContent gc = gson.fromJson(IOUtils.inputStreamToString(new InputStreamReader(con.getInputStream(), "UTF-8")), GitContent.class);
				HashMap<String, JsonElement> map = GitUtils.convertEntrySetToHashMap(gc.content);
				File f = new File(this.folder, this.locale + ".lang");
				byte[] fh = GitUtils.getGitHash(new FileInputStream(f), f.length());
				if (!Arrays.equals(IOUtils.hexToByteArray(map.get("sha").getAsString()), fh)) IOUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/SCAREXgaming/MASLang/master/" + this.locale + ".lang"), f);
			} else {
				System.err.println("A custom lang file as been detected, you can send pull requests at https://github.com/SCAREXgaming/MASLang to help");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Cannot generate hash with SHA1, change your pc !");
		}
	}
}
