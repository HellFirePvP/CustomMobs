package de.hellfirepvp.cmd;

import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * HellFirePvP@Admin
 * Date: 14.05.2015 / 15:58
 * on Project CustomMobs
 * MessageAssist
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
