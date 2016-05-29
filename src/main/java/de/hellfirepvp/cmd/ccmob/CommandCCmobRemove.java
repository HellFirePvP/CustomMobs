package de.hellfirepvp.cmd.ccmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.command.CommandSender;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCCmobRemove
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCCmobRemove extends AbstractCmobCommand {

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
    public int getCustomMobArgumentIndex() {
        return 2;
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String name = args[1];

        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob == null) {
            MessageAssist.msgMobDoesntExist(cs, args[1]);
            return;
        }

        int killed = CustomMob.killAll(name);
        cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.remove.killed"), String.valueOf(killed), args[1]));
    }
}
