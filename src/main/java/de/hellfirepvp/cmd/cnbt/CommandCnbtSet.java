package de.hellfirepvp.cmd.cnbt;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.mob.CustomMob;
import java.text.ParseException;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.nms.RegistryTypeProvider;
import de.hellfirepvp.util.SupportedVersions;
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

public class CommandCnbtSet extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String mobName = args[1];
        final String entryStr = args[2];
        final String valueStr = args[3];
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, mobName);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        String type = (String)cmob.getDataSnapshot().getValue("id");
        if (type == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.set.notype"), mobName));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1)) {
            type = ((RegistryTypeProvider)NMSReflector.mobTypeProvider).tryTranslateRegistryNameToName(type);
        }
        final NBTEntryParser<?> parser = NBTRegister.getRegister().getParserFor(type, entryStr);
        if (parser == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.set.notsupported"), entryStr, type));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        Object value;
        try {
            value = parser.parse(valueStr);
        }
        catch (ParseException e) {
            parser.sendParseErrorMessage((CommandSender)p, valueStr);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (cmob.getDataAdapter().saveTagWithPair(entryStr, value)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cnbt.set.success"), entryStr, value.toString(), mobName));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.set.failed"), value.toString()));
        }
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "set";
    }
    
    @Override
    public boolean hasFixedLength() {
        return true;
    }
    
    @Override
    public int getFixedArgLength() {
        return 4;
    }
    
    @Override
    public int getMinArgLength() {
        return 0;
    }
}
