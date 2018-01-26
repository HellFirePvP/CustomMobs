package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobUnStack
 * Created by HellFirePvP
 * Date: 01.06.2016 / 18:14
 */
public class CommandCmobUnStack extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String mounted = args[1];

        CustomMob cmobMounted = CustomMobs.instance.getMobDataHolder().getCustomMob(mounted);
        if(cmobMounted == null) {
            MessageAssist.msgMobDoesntExist(p, mounted);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMob mounting = CustomMobs.instance.getStackingData().getMountingMob(cmobMounted);
        if(mounting == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.unstack.none"), cmobMounted.getMobFileName()));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMobs.instance.getStackingData().unStack(cmobMounted);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.unstack.success"), cmobMounted.getMobFileName(), mounting.getMobFileName()));
    }

    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }

    @Override
    public String getCommandStart() {
        return "unstack";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 2;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }
}
