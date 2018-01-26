package de.hellfirepvp.api.data.nbt;

import java.util.HashMap;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import java.util.Map;

public enum NBTTagType
{
    BYTE(1, "B", (NBTEntryParser)NBTEntryParser.BYTE_PARSER), 
    SHORT(2, "S", (NBTEntryParser)NBTEntryParser.SHORT_PARSER), 
    INT(3, "I", (NBTEntryParser)NBTEntryParser.INT_PARSER), 
    LONG(4, "L", (NBTEntryParser)NBTEntryParser.LONG_PARSER), 
    FLOAT(5, "F", (NBTEntryParser)NBTEntryParser.FLOAT_PARSER), 
    DOUBLE(6, "D", (NBTEntryParser)NBTEntryParser.DOUBLE_PARSER), 
    BYTE_ARRAY(7, "BA", (NBTEntryParser)NBTEntryParser.BYTE_ARRAY_PARSER), 
    INT_ARRAY(11, "IA", (NBTEntryParser)NBTEntryParser.INT_ARRAY_PARSER), 
    STRING(8, "STR", (NBTEntryParser)NBTEntryParser.STRING_PARSER), 
    TAG_LIST(9, "TL"), 
    TAG_COMPOUND(10, "T");
    
    private static final Map<Integer, NBTTagType> BY_ID;
    private static final Map<String, NBTTagType> BY_IDENTIFIER;
    private final int typeId;
    private final String identifier;
    private final NBTEntryParser parser;
    
    private NBTTagType(final int typeId, final String identifier) {
        this(typeId, identifier, null);
    }
    
    private NBTTagType(final int typeId, final String identifier, final NBTEntryParser parser) {
        this.typeId = typeId;
        this.identifier = identifier;
        this.parser = parser;
    }
    
    public int getTypeId() {
        return this.typeId;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public NBTEntryParser<?> getParser() {
        return (NBTEntryParser<?>)this.parser;
    }
    
    public static NBTTagType getById(final Integer id) {
        return NBTTagType.BY_ID.get(id);
    }
    
    public static NBTTagType getByIdentifier(final String identifier) {
        return NBTTagType.BY_IDENTIFIER.get(identifier.toLowerCase());
    }
    
    static {
        BY_ID = new HashMap<Integer, NBTTagType>();
        BY_IDENTIFIER = new HashMap<String, NBTTagType>();
        for (final NBTTagType type : values()) {
            NBTTagType.BY_ID.put(type.typeId, type);
            NBTTagType.BY_IDENTIFIER.put(type.identifier.toLowerCase(), type);
        }
    }
}
