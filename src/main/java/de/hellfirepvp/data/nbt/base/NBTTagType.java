package de.hellfirepvp.data.nbt.base;

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

    BYTE(1),
    SHORT(2),
    INT(3),
    LONG(4),
    FLOAT(5),
    DOUBLE(6),
    BYTE_ARRAY(7),
    INT_ARRAY(11),
    STRING(8),
    TAG_LIST(9),
    TAG_COMPOUND(10);

    private static final Map<Integer, NBTTagType> BY_ID = new HashMap<>();

    private final int typeId;

    private NBTTagType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public static NBTTagType getById(Integer id) {
        return BY_ID.get(id);
    }

    static {
        for (NBTTagType type : values()) {
            BY_ID.put(type.typeId, type);
        }
    }

}
