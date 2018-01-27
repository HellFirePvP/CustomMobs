package de.hellfirepvp.cmd.cmob;

import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.data.mob.EntityAdapter;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.api.data.EquipmentSlot;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobEquip extends PlayerCmobCommand
{
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
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String sectionStr = args[2];
        int section;
        try {
            section = Integer.parseInt(sectionStr);
            if (section < 0 || section > 5) {
                throw new Exception();
            }
        }
        catch (Exception exc) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int.ranged"), sectionStr, "0", "5"));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final EntityAdapter adapter = cmob.getEntityAdapter();
        final EquipmentSlot eq = EquipmentSlot.values()[section];
        final ItemStack fromPl = eq.getStackFromPlayer(p);
        final ItemStack fromE = adapter.getEquipment(eq);
        adapter.setEquipment(eq, fromPl);
        if (fromPl == null) {
            if (fromE == null) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.equip.nothing"), name, eq.getLocalizedName()));
            }
            else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.equip.reset"), eq.getLocalizedName(), name));
            }
        }
        else if (fromE == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.equip.success"), eq.getLocalizedName(), name));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.equip.success.overwritten"), eq.getLocalizedName(), name));
        }
    }
}
