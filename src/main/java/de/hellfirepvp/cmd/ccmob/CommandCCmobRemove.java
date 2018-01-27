package de.hellfirepvp.cmd.ccmob;

import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.AbstractCmobCommand;

public class CommandCCmobRemove extends AbstractCmobCommand
{
    @Override
    public String getCommandStart() {
        return "remove";
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
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public void execute(final CommandSender cs, final String[] args) {
        final String name = args[1];
        final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (mob == null) {
            MessageAssist.msgMobDoesntExist(cs, args[1]);
            return;
        }
        final int killed = CustomMob.killAll(name);
        cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.remove.killed"), String.valueOf(killed), args[1]));
    }
}
