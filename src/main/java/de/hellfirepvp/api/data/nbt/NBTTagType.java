package de.hellfirepvp.api.data.nbt;

import de.hellfirepvp.data.nbt.NBTEntryParser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTTagType
 * Created by HellFirePvP
 * Date: 24.05.2016 / 13:22
 */
public enum NBTTagType {

    BYTE(1, "B", NBTEntryParser.BYTE_PARSER),
    SHORT(2, "S", NBTEntryParser.SHORT_PARSER),
    INT(3, "I", NBTEntryParser.INT_PARSER),
    LONG(4, "L", NBTEntryParser.LONG_PARSER),
    FLOAT(5, "F", NBTEntryParser.FLOAT_PARSER),
    DOUBLE(6, "D", NBTEntryParser.DOUBLE_PARSER),
    BYTE_ARRAY(7, "BA", NBTEntryParser.BYTE_ARRAY_PARSER),
    INT_ARRAY(11, "IA", NBTEntryParser.INT_ARRAY_PARSER),
    STRING(8, "STR", NBTEntryParser.STRING_PARSER),

    TAG_LIST(9, "TL"),
    TAG_COMPOUND(10, "T");

    private static final Map<Integer, NBTTagType> BY_ID = new HashMap<>();
    private static final Map<String, NBTTagType> BY_IDENTIFIER = new HashMap<>();

    private final int typeId;
    private final String identifier;
    private final NBTEntryParser parser;

    private NBTTagType(int typeId, String identifier) {
        this(typeId, identifier, null);
    }

    private NBTTagType(int typeId, String identifier, NBTEntryParser parser) {
        this.typeId = typeId;
        this.identifier = identifier;
        this.parser = parser;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public NBTEntryParser<?> getParser() {
        return parser;
    }

    public static NBTTagType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static NBTTagType getByIdentifier(String identifier) {
        return BY_IDENTIFIER.get(identifier.toLowerCase());
    }

    static {
        for (NBTTagType type : values()) {
            BY_ID.put(type.typeId, type);
            BY_IDENTIFIER.put(type.identifier.toLowerCase(), type);
        }
    }

}
