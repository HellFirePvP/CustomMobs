package de.hellfirepvp.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: StringUtils
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public class StringUtils {

    public static Set<String> replaceAtAll(Set<String> col, String oldStr, String newStr) {
        Set<String> s = new HashSet<>();
        for(String str : col) {
            str = str.replace(oldStr, newStr);
            s.add(str);
        }
        return s;
    }

    public static <T> String connectWithSeperator(Collection<T> set, String seperator, ToStringRunnable<T> run) {
        StringBuilder builder = new StringBuilder();
        int counter = 0;
        for (T key : set) {
            if(key == null) continue;
            builder.append(run.toString(key));
            counter++;
            if (counter < set.size()) {
                builder.append(seperator);
            }
        }
        return builder.toString();
    }

    public static String connectWithSeperator(Collection<String> set, String seperator) {
        return connectWithSeperator(set, seperator, new ToStringRunnable<String>() {
            @Override
            public String toString(String val) {
                return val;
            }
        });
    }

    public abstract static class ToStringRunnable<T> {

        public abstract String toString(T val);

    }

}
