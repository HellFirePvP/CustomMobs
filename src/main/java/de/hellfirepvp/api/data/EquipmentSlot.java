package de.hellfirepvp.api.data;

import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EquipmentSlot
 * Created by HellFirePvP
 * Date: 27.05.2016 / 01:23
 */
public enum EquipmentSlot {

    MAIN_HAND("main"),
    OFF_HAND("off"),
    HELMET("helmet"),
    CHESTPLATE("chest"),
    LEGGINGS("leggings"),
    BOOTS("boots");

    private static final Map<String, EquipmentSlot> BY_NAME = new HashMap<>();

    private final String subKey;

    private EquipmentSlot(String subKey) {
        this.subKey = subKey;
    }

    public String getSubKey() {
        return subKey;
    }

    public static EquipmentSlot getByName(String name) {
        return BY_NAME.get(name);
    }

    static {
        for (EquipmentSlot es : values()) {
            if(es == null) continue;
            BY_NAME.put(es.getSubKey(), es);
        }
    }

    public String getUnlocalizedRepresentation() {
        return "equipment." + subKey;
    }

    public String getLocalizedName() {
        return LanguageHandler.translate(getUnlocalizedRepresentation());
    }

    public ItemStack getStackFromPlayer(Player player) {
        PlayerInventory inv = player.getInventory();
        switch (this) {
            case MAIN_HAND:
                return inv.getItemInMainHand();
            case OFF_HAND:
                return inv.getItemInOffHand();
            case HELMET:
                return inv.getHelmet();
            case CHESTPLATE:
                return inv.getChestplate();
            case LEGGINGS:
                return inv.getLeggings();
            case BOOTS:
                return inv.getBoots();
        }
        return null;
    }

    public ItemStack getEquipment(EntityEquipment ee) {
        switch (this) {
            case MAIN_HAND:
                return ee.getItemInMainHand();
            case OFF_HAND:
                return ee.getItemInOffHand();
            case HELMET:
                return ee.getHelmet();
            case CHESTPLATE:
                return ee.getChestplate();
            case LEGGINGS:
                return ee.getLeggings();
            case BOOTS:
                return ee.getBoots();
        }
        return null;
    }

    public void setEquipment(ItemStack item, EntityEquipment ee) {
        switch (this) {
            case MAIN_HAND:
                ee.setItemInMainHand(item);
                break;
            case OFF_HAND:
                ee.setItemInOffHand(item);
                break;
            case BOOTS:
                ee.setBoots(item);
                break;
            case LEGGINGS:
                ee.setLeggings(item);
                break;
            case CHESTPLATE:
                ee.setChestplate(item);
                break;
            case HELMET:
                ee.setHelmet(item);
                break;
        }
    }

}
