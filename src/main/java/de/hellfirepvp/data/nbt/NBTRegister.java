package de.hellfirepvp.data.nbt;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTRegister
 * Created by HellFirePvP
 * Date: 29.05.2016 / 12:40
 */
public class NBTRegister {

    private Map<String, Map<String, NBTEntryParser<?>>> entryRegister = new HashMap<>();

    private static NBTRegister instance = new NBTRegister();
    private NBTRegister() {}

    private NBTRegister getRegister() {
        return instance;
    }

    public NBTEntryParser<?> getParserFor(String mobTypeStr, String suggestedEntry) {
        Map<String, NBTEntryParser<?>> parsers = entryRegister.get(mobTypeStr);
        if(parsers == null) return null;
        return parsers.get(suggestedEntry);
    }

}
