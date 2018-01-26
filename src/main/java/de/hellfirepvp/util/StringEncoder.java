package de.hellfirepvp.util;

public class StringEncoder
{
    public static final char ENC_CHAR = '\u00A7';
    public static final String SPLIT_STR;
    
    public static String encode(final String displayName, final String message) {
        final StringBuilder strBuilder = new StringBuilder(displayName);
        strBuilder.append(ENC_CHAR).append(ENC_CHAR);
        for (final char c : message.toCharArray()) {
            strBuilder.append(ENC_CHAR).append(c);
        }
        return strBuilder.toString();
    }
    
    public static String decode(final String encodedDisplayName) {
        final String[] split = encodedDisplayName.split(StringEncoder.SPLIT_STR);
        if (split.length == 2) {
            final String res = split[1];
            return res.replace(String.valueOf(SPLIT_STR), "");
        }
        return null;
    }
    
    static {
        SPLIT_STR = String.valueOf(ENC_CHAR) + String.valueOf(ENC_CHAR);
    }
}
