package mas;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * This class is used to translate the program.
 * 
 * @author SCAREX
 */
public class MASLang
{
	public static File langFolder;
	private static HashMap<String, String> langMap;
	private static HashMap<String, String> enMap;

	public static void load(File folder) {
		if (!(langFolder = new File(folder, "lang")).exists()) langFolder.mkdirs();
		if (!(new File(langFolder, "en_US.lang")).exists()) MASLang.forceLangDownload(folder);
		MASLang.parseLangFiles();
	}

	private static void parseLangFiles() {
		enMap = MASLang.parseFile(new File(langFolder, "en_US.lang"));
		if (new File(langFolder, Locale.getDefault() + ".lang").exists()) langMap = parseFile(new File(langFolder, Locale.getDefault() + ".lang"));
	}

	public static HashMap<String, String> parseFile(File f) {
		HashMap<String, String> map = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String l;
			while ((l = br.readLine()) != null) {
				int i;
				if (l.length() > 0 && l.charAt(0) != '#' && (i = l.indexOf('=')) != -1) {
					map.put(l.substring(0, i), l.substring(i + 1));
				}
			}
		} catch (Exception e) {
			System.err.println("Cannot parse file : " + f.getName());
			e.printStackTrace();
		}
		return map;
	}

	public static String translate(String key) {
		return langMap.containsKey(key) ? langMap.get(key) : enMap.get(key);
	}

	public static String translate(String key, Object ... params) {
		return String.format(langMap.containsKey(key) ? langMap.get(key) : enMap.get(key));
	}

	public static void forceLangDownload(File folder) {
		long time = System.currentTimeMillis();
		try {
			JFrame window = new JFrame("Downloading lang files");
			window.setSize(400, 200);
			window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			window.setLayout(new BorderLayout());
			JProgressBar bar = new JProgressBar();
			bar.setStringPainted(true);
			window.add(bar, BorderLayout.CENTER);

			window.setVisible(true);

			bar.setString("Getting file list...");
			Tree t = getLangTree();
			bar.setMaximum(t.fileList.size());
			bar.setString("Downloading " + t.fileList.size() + " files");
			Iterator<HashMap<String, String>> ite = t.fileList.iterator();
			while (ite.hasNext()) {
				HashMap<String, String> map = ite.next();
				if (map.get("path").endsWith(".lang")) {
					bar.setString("Dwonloading : " + map.get("path"));
					IOUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/SCAREXgaming/MASLang/master/" + map.get("path")), new File(langFolder, map.get("path")));
					bar.setValue(bar.getValue() + 1);
				}
			}
			window.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Updating lang time : " + (System.currentTimeMillis() - time));
	}

	private static Tree getLangTree() throws IOException {
		URLConnection con = (HttpsURLConnection) (new URL("https://api.github.com/repos/SCAREXgaming/MASLang/git/trees/master")).openConnection();
		InputStream is = con.getInputStream();
		String s = IOUtils.inputStreamToString(new InputStreamReader(is, "UTF-8"));

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Tree.class, new Tree.TreeDeserializer());
		Gson gson = builder.create();
		Tree t = gson.fromJson(s, Tree.class);
		return t;
	}

	public static class Tree
	{
		public ArrayList<HashMap<String, String>> fileList;

		public Tree(ArrayList<HashMap<String, String>> list) {
			this.fileList = list;
		}

		public static class TreeDeserializer implements JsonDeserializer<Tree>
		{
			@Override
			public Tree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				JsonArray ja = json.getAsJsonObject().getAsJsonArray("tree");
				ArrayList<HashMap<String, String>> list = new ArrayList<>();
				Iterator<JsonElement> ite = ja.iterator();
				while (ite.hasNext()) {
					JsonObject e = ite.next().getAsJsonObject();
					HashMap<String, String> map = new HashMap<>();
					Iterator<Entry<String, JsonElement>> ite1 = e.entrySet().iterator();
					while (ite1.hasNext()) {
						Entry<String, JsonElement> entry = ite1.next();
						map.put(entry.getKey(), entry.getValue().getAsString());
					}
					list.add(map);
				}
				return new Tree(list);
			}
		}
	}
}
