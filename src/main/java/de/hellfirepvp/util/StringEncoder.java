package de.hellfirepvp.util;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: StringEncoder
 * Created by HellFirePvP
 * Date: 01.09.2016 / 20:33
 */
public class StringEncoder {

    public static final char ENC_CHAR = '\u00A7';
    public static final String SPLIT_STR = String.valueOf(ENC_CHAR) + String.valueOf(ENC_CHAR);

    public static String encode(String displayName, String message) {
        StringBuilder strBuilder = new StringBuilder(displayName);
        strBuilder.append(ENC_CHAR).append(ENC_CHAR);
        for(char c : message.toCharArray()) {
            strBuilder.append(ENC_CHAR).append(c);
        }
        return strBuilder.toString();
    }

    public static String decode(String encodedDisplayName) {
        String[] split = encodedDisplayName.split(SPLIT_STR);
        if(split.length == 2) {
            String res = split[1];
            return res.replace(String.valueOf(ENC_CHAR), "");
        }
        return null;
    }
}
