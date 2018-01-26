package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobCreate
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
 */
public class CommandCmobCreate extends PlayerCmobCommand {

    @Override
    public String getCommandStart() {
        return "create";
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

    @Override
    public String getLocalizedSpecialMessage() {
        List<String> types = NMSReflector.mobTypeProvider.getTypeNames();
        return String.format(LanguageHandler.translate("command.mobypes"), Arrays.toString(types.toArray()));
    }

    @Override
    public void execute(Player p, String[] args) {
        String typeStr = args[1];
        String name = args[2];
        if(!NMSReflector.mobTypeProvider.getTypeNames().contains(typeStr)) {
            MessageAssist.msgMobTypeDoesntExist(p, typeStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        if(name.contains("##-##")) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.name.invalid"));
            return;
        }
        if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) != null) {
            MessageAssist.msgMobDoesAlreadyExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }
        LivingEntity created = NMSReflector.mobTypeProvider.getEntityForName(p.getWorld(), typeStr);
        if(MobFactory.tryCreateCustomMobFromEntity(created, name)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.create.success"), name));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.create.failed"), name));
        }
    }
}
