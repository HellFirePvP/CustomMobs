package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.EquipmentSlot;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.EntityAdapter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobEquip
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
 */
public class CommandCmobEquip extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "equip";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 3;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public int getCustomMobArgumentIndex() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String sectionStr = args[2];
        int section;
        try {
            section = Integer.parseInt(sectionStr);
            if(section < 0 || section > 5) throw new Exception();
        } catch (Exception exc) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int.ranged"), sectionStr, "0", "5"));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        EntityAdapter adapter = cmob.getEntityAdapter();
        EquipmentSlot eq = EquipmentSlot.values()[section];
        ItemStack fromPl = eq.getStackFromPlayer(p);
        ItemStack fromE = adapter.getEquipment(eq);
        adapter.setEquipment(eq, fromPl);
        if(fromPl == null) {
            if(fromE == null) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.equip.nothing"), name, eq.getLocalizedName()));
            } else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.equip.reset"), eq.getLocalizedName(), name));
            }
        } else {
            if(fromE == null) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.equip.success"), eq.getLocalizedName(), name));
            } else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.equip.success.overwritten"), eq.getLocalizedName(), name));
            }
        }
    }
}
