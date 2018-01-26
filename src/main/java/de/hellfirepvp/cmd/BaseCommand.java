package de.hellfirepvp.cmd;

import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permissible;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class BaseCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        final CommandRegistry.CommandCategory category = CommandRegistry.CommandCategory.evaluate(cmd.getName().toLowerCase());
        if (category == null) {
            return true;
        }
        if (!category.allowsConsole && !(cs instanceof Player)) {
            CustomMobs.logger.info(LanguageHandler.translate(LibLanguageOutput.NO_CONSOLE));
            return true;
        }
        if (!hasPermissions((Permissible)cs, "custommobs.cmduse")) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " '" + "custommobs.cmduse" + "'");
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.AQUA + String.format(LibLanguageOutput.CMD_DESCRIPTION_HEADER, "HellFirePvP"));
            final List<? extends AbstractCmobCommand> commands = CommandRegistry.getAllRegisteredCommands(category);
            for (final AbstractCmobCommand cmobCmd : commands) {
                cs.sendMessage(forgeDescriptionString(cmobCmd));
            }
            return true;
        }
        final String commandIdentifier = args[0];
        final CommandRegistry.CommandRegisterKey key = new CommandRegistry.CommandRegisterKey(category, commandIdentifier);
        final AbstractCmobCommand exec = CommandRegistry.getCommand(key);
        if (exec == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.ERR_NO_SUCH_COMMAND));
            return true;
        }
        final String perm = getPermissionNode(exec);
        if (!hasPermissions((Permissible)cs, perm)) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + String.format(LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED), perm));
            return true;
        }
        if (exec.hasFixedLength()) {
            if (exec.getFixedArgLength() != args.length) {
                cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.ERR_CMD_INVALID_ARGUMENTS));
                sendPlayerDescription(cs, exec, true);
                return true;
            }
        }
        else if (args.length < exec.getMinArgLength()) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.ERR_CMD_NOT_ENOUGH_ARGUMENTS));
            sendPlayerDescription(cs, exec, true);
            return true;
        }
        if (category.allowsConsole) {
            exec.execute(cs, args);
        }
        else {
            ((PlayerCmobCommand)exec).execute((Player)cs, args);
        }
        return true;
    }
    
    public static boolean hasPermissions(final Permissible perm, final String explicitCommandPermission) {
        return perm.hasPermission(explicitCommandPermission) || perm.isOp() || perm.hasPermission("custommobs.*");
    }
    
    public static String getPermissionNode(final AbstractCmobCommand command) {
        return getPermissionNode(command.category, command.getCommandStart());
    }
    
    public static String getPermissionNode(final String commandCat, final String cmdStart) {
        return String.format("custommobs.%s.%s", commandCat, cmdStart);
    }
    
    private static String forgeDescriptionString(final AbstractCmobCommand cmobCommand) {
        return ChatColor.GOLD + LanguageHandler.translate(cmobCommand.getInputDescriptionKey()) + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + LanguageHandler.translate(cmobCommand.getUsageDescriptionKey());
    }
    
    public static void sendPlayerDescription(final CommandSender cs, final AbstractCmobCommand cmobCommand, final boolean additional) {
        cs.sendMessage(LibLanguageOutput.PREFIX + forgeDescriptionString(cmobCommand));
        if (additional) {
            final String key = cmobCommand.getAdditionalInformationKey();
            if (LanguageHandler.canTranslate(key)) {
                cs.sendMessage(ChatColor.GREEN + LanguageHandler.translate(key));
            }
        }
        if (cmobCommand.getLocalizedSpecialMessage() != null) {
            cs.sendMessage(ChatColor.GREEN + cmobCommand.getLocalizedSpecialMessage());
        }
    }
}
