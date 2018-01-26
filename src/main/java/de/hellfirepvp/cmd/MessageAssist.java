package de.hellfirepvp.cmd;

import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.command.CommandSender;

public final class MessageAssist
{
    public static void msgIOException(final CommandSender cs) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.ERR_IO_EXCEPTION));
    }
    
    public static void msgMobDoesAlreadyExist(final CommandSender cs, final String mobName) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_MOB_EXISTS), mobName));
    }
    
    public static void msgMobDoesntExist(final CommandSender cs, final String mobName) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_MOB), mobName));
    }
    
    public static void msgMobTypeDoesntExist(final CommandSender cs, final String mobName) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_MOBTYPE), mobName));
    }
    
    public static void msgPotionTypeDoesntExist(final CommandSender cs, final String potionTypeStr) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_POTIONTYPE), potionTypeStr));
    }
    
    public static void msgShouldBeAIntNumber(final CommandSender cs, final String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int"), strNoNumber));
    }
    
    public static void msgShouldBeAIntNumberOrInfinite(final CommandSender cs, final String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int.infinite"), strNoNumber));
    }
    
    public static void msgShouldBeAIntNumberRanged(final CommandSender cs, final String strNoNumber, final String lowerBoundIncl, final String higherBoundIncl) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.int.ranged"), strNoNumber, lowerBoundIncl, higherBoundIncl));
    }
    
    public static void msgShouldBeAFloatNumber(final CommandSender cs, final String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.float"), strNoNumber));
    }
    
    public static void msgShouldBeAFloatNumberNormalized(final CommandSender cs, final String strNoNumber) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.float.norm"), strNoNumber));
    }
    
    public static void msgShouldBeAFloatNumberRanged(final CommandSender cs, final String strNoNumber, final String lowerBoundIncl, final String higherBoundIncl) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.float.ranged"), strNoNumber, lowerBoundIncl, higherBoundIncl));
    }
    
    public static void msgShouldBeABooleanValue(final CommandSender cs, final String strNoBoolean) {
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.number.bool"), strNoBoolean));
    }
}
