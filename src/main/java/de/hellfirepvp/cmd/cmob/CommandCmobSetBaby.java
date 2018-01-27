package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobSetBaby extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String boolBaby = args[2];
        boolean baby;
        try {
            if (boolBaby.equalsIgnoreCase("true")) {
                baby = true;
            }
            else {
                if (!boolBaby.equalsIgnoreCase("false")) {
                    throw new Exception();
                }
                baby = false;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue((CommandSender)p, args[2]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final LivingEntity dummyInstance = cmob.getEntityAdapter().getAdapterEntity().get();
        if (!(dummyInstance instanceof Ageable) && !(dummyInstance instanceof Zombie)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.setbaby.type"), cmob.getEntityAdapter().getEntityType().getName()));
            return;
        }
        if (baby) {
            cmob.getEntityAdapter().setBaby();
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.setbaby.baby"), name));
        }
        else {
            cmob.getEntityAdapter().setAdult();
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.setbaby.adult"), name));
        }
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "setbaby";
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
}
