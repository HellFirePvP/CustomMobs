package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.StackingDataHolder;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobStack extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String mounted = args[1];
        final String mounting = args[2];
        final CustomMob cmobMounted = CustomMobs.instance.getMobDataHolder().getCustomMob(mounted);
        if (cmobMounted == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, mounted);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob cmobMounting = CustomMobs.instance.getMobDataHolder().getCustomMob(mounting);
        if (cmobMounting == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, mounting);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (cmobMounted.getMobFileName().equals(cmobMounting.getMobFileName())) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.stack.same"));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final StackingDataHolder.MobStack st = new StackingDataHolder.MobStack(cmobMounted.getMobFileName(), cmobMounting.getMobFileName());
        final StackingDataHolder instance = CustomMobs.instance.getStackingData();
        if (!instance.canAddStack(st)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.stack.circle"), cmobMounting.getMobFileName(), cmobMounted.getMobFileName()));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        instance.uncheckedAddStack(st);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate("command.cmob.stack.success"), cmobMounted.getMobFileName(), cmobMounting.getMobFileName()));
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
