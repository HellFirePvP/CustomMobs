package de.hellfirepvp.cmd.cmob;

import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.CustomMobs;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Arrays;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobCreate extends PlayerCmobCommand
{
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
        final List<String> types = NMSReflector.mobTypeProvider.getTypeNames();
        return String.format(LanguageHandler.translate("command.mobypes"), Arrays.toString(types.toArray()));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        final String typeStr = args[1];
        final String name = args[2];
        if (!NMSReflector.mobTypeProvider.getTypeNames().contains(typeStr)) {
            MessageAssist.msgMobTypeDoesntExist((CommandSender)p, typeStr);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (name.contains("##-##")) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.name.invalid"));
            return;
        }
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(name) != null) {
            MessageAssist.msgMobDoesAlreadyExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        final LivingEntity created = NMSReflector.mobTypeProvider.getEntityForName(p.getWorld(), typeStr);
        if (MobFactory.tryCreateCustomMobFromEntity(created, name)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.create.success"), name));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.create.failed"), name));
        }
    }
}
