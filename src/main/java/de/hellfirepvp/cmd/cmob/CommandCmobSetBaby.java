package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 06.03.2016 / 13:13
 * on Project CustomMobs
 * CommandCmobSetBaby
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
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "You can't set the age of a '" + cmob.getEntityAdapter().getEntityType().getName() + "'-type mob.");
            return;
        }

        if(baby) {
            cmob.getEntityAdapter().setBaby();
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + name + " is now a baby-mob.");
        } else {
            cmob.getEntityAdapter().setAdult();
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + name + " is now an adult mob.");
        }
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
