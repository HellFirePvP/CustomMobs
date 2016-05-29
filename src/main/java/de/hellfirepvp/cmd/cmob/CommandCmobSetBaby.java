package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobSetBaby
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:07
 */
public class CommandCmobSetBaby extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String boolBaby = args[2];

        boolean baby;
        try {
            if(boolBaby.equalsIgnoreCase("true")) {
                baby = true;
            } else if(boolBaby.equalsIgnoreCase("false")) {
                baby = false;
            } else {
                throw new Exception();
            }
        } catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue(p, args[2]);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        if(!(cmob.getEntityAdapter().getAdapterEntity().get() instanceof Ageable)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.setbaby.type"), cmob.getEntityAdapter().getEntityType().getName()));
            return;
        }

        if(baby) {
            cmob.getEntityAdapter().setBaby();
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.setbaby.baby"), name));
        } else {
            cmob.getEntityAdapter().setAdult();
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.setbaby.adult"), name));
        }
    }

    @Override
    public int getCustomMobArgumentIndex() {
        return 2;
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
