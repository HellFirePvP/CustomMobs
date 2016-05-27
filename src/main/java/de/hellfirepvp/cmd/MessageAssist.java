package de.hellfirepvp.cmd;

import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MessageAssist
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public final class MessageAssist {

    private MessageAssist() {}

    public static void msgIOException(CommandSender cs) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.ERR_IO_EXCEPTION));
    }

    public static void msgMobDoesAlreadyExist(CommandSender cs, String mobName) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_MOB_EXISTS), mobName));
    }

    public static void msgMobDoesntExist(CommandSender cs, String mobName) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_MOB), mobName));
    }

    public static void msgMobTypeDoesntExist(CommandSender cs, String mobName) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_MOBTYPE), mobName));
    }

    public static void msgPotionTypeDoesntExist(CommandSender cs, String potionTypeStr) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_POTIONTYPE), potionTypeStr));
    }

    public static void msgShouldBeAIntNumber(CommandSender cs, String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int"), strNoNumber));
    }

    public static void msgShouldBeAIntNumberOrInfinite(CommandSender cs, String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int.infinite"), strNoNumber));
    }

    public static void msgShouldBeAFloatNumber(CommandSender cs, String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.float"), strNoNumber));
    }

    public static void msgShouldBeAFloatNumberNormalized(CommandSender cs, String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.float.norm"), strNoNumber));
    }

    public static void msgShouldBeABooleanValue(CommandSender cs, String strNoBoolean) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.bool"), strNoBoolean));
    }

}
