package de.hellfirepvp.cmd.cnbt;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.ParseException;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCnbtSet
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:30
 */
public class CommandCnbtSet extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String mobName = args[1];
        String entryStr = args[2];
        String valueStr = args[3];

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, mobName);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        String type = (String) cmob.getDataSnapshot().getValue("id");
        if(type == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.set.notype"), mobName));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        NBTEntryParser<?> parser = NBTRegister.getRegister().getParserFor(type, entryStr);
        if(parser == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.set.notsupported"), entryStr, type));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        Object value;
        try {
            value = parser.parse(valueStr);
        } catch (ParseException e) {
            parser.sendParseErrorMessage(p, valueStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        if(cmob.getDataAdapter().saveTagWithPair(entryStr, value)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cnbt.set.success"), entryStr, value.toString(), mobName));
        } else {
            //This is something that's prevented by NBTParsers. but well, you never know...
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.set.failed"), value.toString()));
        }
    }

    @Override
    public int getCustomMobArgumentIndex() {
        return 2;
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
