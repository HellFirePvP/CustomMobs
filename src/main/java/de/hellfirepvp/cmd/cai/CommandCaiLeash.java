package de.hellfirepvp.cmd.cai;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCaiLeash
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public class CommandCaiLeash extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String range = args[2];

        if(range.equalsIgnoreCase("remove")) {
            if(LeashManager.removeLeashSetting(name)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cai.leash.success"), name));
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("command.cai.leash.remove.info"));
            } else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cai.leash.noleash"), name));
            }
            return;
        }

        double maxRange;
        try {
            maxRange = Double.parseDouble(range);
        } catch (Exception e) {
            MessageAssist.msgShouldBeAFloatNumber(p, range);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            return;
        }

        if(LeashManager.shouldBeLeashed(name)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cai.leash.exists"), name));
            return;
        }

        LeashManager.addNewLeashSetting(name, maxRange);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cai.leash.success"), name, String.valueOf(maxRange)));
    }

    @Override
    public String getCommandStart() {
        return "leash";
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
