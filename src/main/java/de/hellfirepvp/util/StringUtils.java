package de.hellfirepvp.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

public class StringUtils
{
    public static Set<String> replaceAtAll(final Set<String> col, final String oldStr, final String newStr) {
        final Set<String> s = new HashSet<String>();
        for (String str : col) {
            str = str.replace(oldStr, newStr);
            s.add(str);
        }
        return s;
    }
    
    public static <T> String connectWithSeperator(final Collection<T> set, final String seperator, final ToStringRunnable<T> run) {
        final StringBuilder builder = new StringBuilder();
        int counter = 0;
        for (final T key : set) {
            if (key == null) {
                continue;
            }
            builder.append(run.toString(key));
            if (++counter >= set.size()) {
                continue;
            }
            builder.append(seperator);
        }
        return builder.toString();
    }
    
    public static String connectWithSeperator(final Collection<String> set, final String seperator) {
        return connectWithSeperator(set, seperator, new ToStringRunnable<String>() {
            @Override
            public String toString(final String val) {
                return val;
            }
        });
    }
    
    public abstract static class ToStringRunnable<T>
    {
        public abstract String toString(final T p0);
    }
}
