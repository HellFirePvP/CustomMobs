package de.hellfirepvp.cmd.ccmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.command.CommandSender;

/**
 * HellFirePvP@Admin
 * Date: 06.03.2016 / 22:10
 * on Project CustomMobs
 * CommandCCmobRemove
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
    public void execute(CommandSender cs, String[] args) {
        String name = args[1];

        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob == null) {
            cs.sendMessage("A mob named " + args[1] + " doesn't exist.");
            return;
        }

        int killed = CustomMob.killAll(name);
        cs.sendMessage("Killed " + killed + "x " + args[1] + " on the server.");
    }
}
