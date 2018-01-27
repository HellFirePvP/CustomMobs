package de.hellfirepvp.api.data;

import java.util.HashMap;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import de.hellfirepvp.lang.LanguageHandler;
import java.util.Map;

public enum EquipmentSlot
{
    MAIN_HAND("main"), 
    OFF_HAND("off"), 
    HELMET("helmet"), 
    CHESTPLATE("chest"), 
    LEGGINGS("leggings"), 
    BOOTS("boots");
    
    private static final Map<String, EquipmentSlot> BY_NAME;
    private final String subKey;
    
    private EquipmentSlot(final String subKey) {
        this.subKey = subKey;
    }
    
    public String getSubKey() {
        return this.subKey;
    }
    
    public static EquipmentSlot getByName(final String name) {
        return EquipmentSlot.BY_NAME.get(name);
    }
    
    public String getUnlocalizedRepresentation() {
        return "equipment." + this.subKey;
    }
    
    public String getLocalizedName() {
        return LanguageHandler.translate(this.getUnlocalizedRepresentation());
    }
    
    public ItemStack getStackFromPlayer(final Player player) {
        final PlayerInventory inv = player.getInventory();
        switch (this) {
            case MAIN_HAND: {
                return inv.getItemInMainHand();
            }
            case OFF_HAND: {
                return inv.getItemInOffHand();
            }
            case HELMET: {
                return inv.getHelmet();
            }
            case CHESTPLATE: {
                return inv.getChestplate();
            }
            case LEGGINGS: {
                return inv.getLeggings();
            }
            case BOOTS: {
                return inv.getBoots();
            }
            default: {
                return null;
            }
        }
    }
    
    public ItemStack getEquipment(final EntityEquipment ee) {
        switch (this) {
            case MAIN_HAND: {
                return ee.getItemInMainHand();
            }
            case OFF_HAND: {
                return ee.getItemInOffHand();
            }
            case HELMET: {
                return ee.getHelmet();
            }
            case CHESTPLATE: {
                return ee.getChestplate();
            }
            case LEGGINGS: {
                return ee.getLeggings();
            }
            case BOOTS: {
                return ee.getBoots();
            }
            default: {
                return null;
            }
        }
    }
    
    public void setEquipment(final ItemStack item, final EntityEquipment ee) {
        switch (this) {
            case MAIN_HAND: {
                ee.setItemInMainHand(item);
                break;
            }
            case OFF_HAND: {
                ee.setItemInOffHand(item);
                break;
            }
            case BOOTS: {
                ee.setBoots(item);
                break;
            }
            case LEGGINGS: {
                ee.setLeggings(item);
                break;
            }
            case CHESTPLATE: {
                ee.setChestplate(item);
                break;
            }
            case HELMET: {
                ee.setHelmet(item);
                break;
            }
        }
    }
    
    static {
        BY_NAME = new HashMap<String, EquipmentSlot>();
        for (final EquipmentSlot es : values()) {
            if (es != null) {
                EquipmentSlot.BY_NAME.put(es.getSubKey(), es);
            }
        }
    }
}
