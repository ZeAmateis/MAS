package mas.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * This class loads content from Github.
 * 
 * @author SCAREX
 *
 */
public class GitUtils
{
    public static <T, E> HashMap<T, E> convertEntrySetToHashMap(Set<Entry<T, E>> set) {
        HashMap<T, E> map = new HashMap<T, E>();
        for (Entry<T, E> e : set) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    public static byte[] getGitHash(InputStream is, long length, GitType type) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update((type.name().toLowerCase() + " " + length + "\0").getBytes());
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }
        return md.digest();
    }

    public static class GitContent
    {
        public final Set<Entry<String, JsonElement>> content;

        public GitContent(Set<Entry<String, JsonElement>> content) {
            this.content = content;
        }
    }

    public static class GitContentDeserializer implements JsonDeserializer<GitContent>
    {
        @Override
        public GitContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new GitContent(json.getAsJsonObject().entrySet());
        }
    }

    public static enum GitType
    {
        BLOB, TREE;
    }
}
