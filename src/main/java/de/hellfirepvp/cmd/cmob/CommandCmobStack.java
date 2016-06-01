package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.StackingDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobStack
 * Created by HellFirePvP
 * Date: 31.05.2016 / 18:44
 */
public class CommandCmobStack extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String mounted = args[1];
        String mounting = args[2];

        CustomMob cmobMounted = CustomMobs.instance.getMobDataHolder().getCustomMob(mounted);
        if(cmobMounted == null) {
            MessageAssist.msgMobDoesntExist(p, mounted);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMob cmobMounting = CustomMobs.instance.getMobDataHolder().getCustomMob(mounting);
        if(cmobMounting == null) {
            MessageAssist.msgMobDoesntExist(p, mounting);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        if(cmobMounted.getMobFileName().equals(cmobMounting.getMobFileName())) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.stack.same"));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        StackingDataHolder.MobStack st = new StackingDataHolder.MobStack(cmobMounted.getMobFileName(), cmobMounting.getMobFileName());
        StackingDataHolder instance = CustomMobs.instance.getStackingData();
        if(!instance.canAddStack(st)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                    String.format(LanguageHandler.translate("command.cmob.stack.circle"),
                            cmobMounting.getMobFileName(), cmobMounted.getMobFileName()));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        instance.uncheckedAddStack(st);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD +
                String.format(LanguageHandler.translate("command.cmob.stack.success"),
                        cmobMounted.getMobFileName(), cmobMounting.getMobFileName()));
    }

    @Override
    public String getCommandStart() {
        return "stack";
    }

    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2, 3 };
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
