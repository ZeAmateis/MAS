package mas.utils;

import java.util.ArrayList;

/**
 * @author SCAREX
 *
 */
public class StringUtils
{
    public static String[] charSplit(String s, char c) {
        ArrayList<String> as = new ArrayList<String>();
        int i = 0;
        while ((i = s.indexOf(c)) > 0) {
            as.add(s.substring(0, i));
            s = s.substring(i + 1);
        }
        as.add(s.substring(i + 1));
        return as.toArray(new String[0]);
    }
    
    public static String[] charSplitTrim(String s, char c) {
        ArrayList<String> as = new ArrayList<String>();
        int i = 0;
        while ((i = s.indexOf(c)) > 0) {
            as.add(s.substring(0, i).trim());
            s = s.substring(i + 1);
        }
        as.add(s.substring(i + 1).trim());
        return as.toArray(new String[0]);
    }
}
