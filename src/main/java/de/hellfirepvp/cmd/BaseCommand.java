package de.hellfirepvp.cmd;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibMisc;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.List;

import static de.hellfirepvp.lib.LibLanguageOutput.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: BaseCommand
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 3:59
 */
public class BaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        CommandRegistry.CommandCategory category = CommandRegistry.CommandCategory.evaluate(cmd.getName().toLowerCase());
        if(category == null) return true; //Uhm. nope.
        if(!category.allowsConsole) {
            if(!(cs instanceof Player)) {
                CustomMobs.logger.info(LanguageHandler.translate(NO_CONSOLE));
                return true;
            }
        }

        if(!hasPermissions(cs, LibMisc.PERM_USE)) {
            cs.sendMessage(PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(NO_PERMISSION));
            cs.sendMessage(PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(PERMISSION_REQUIRED) + " '" + LibMisc.PERM_USE + "'");
            return true;
        }

        if(args.length == 0) {
            cs.sendMessage(PREFIX + ChatColor.AQUA + String.format(CMD_DESCRIPTION_HEADER, "HellFirePvP"));
            List<? extends AbstractCmobCommand> commands = CommandRegistry.getAllRegisteredCommands(category);
            for(AbstractCmobCommand cmobCmd : commands) {
                cs.sendMessage(forgeDescriptionString(cmobCmd));
            }
            return true;
        }

        String commandIdentifier = args[0];
        CommandRegistry.CommandRegisterKey key = new CommandRegistry.CommandRegisterKey(category, commandIdentifier);
        AbstractCmobCommand exec = CommandRegistry.getCommand(key);

        if(exec == null) {
            cs.sendMessage(PREFIX + ChatColor.RED + LanguageHandler.translate(ERR_NO_SUCH_COMMAND));
            return true;
        }

        String perm = getPermissionNode(exec);

        if(!hasPermissions(cs, perm)) {
            cs.sendMessage(PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(NO_PERMISSION));
            cs.sendMessage(PREFIX + ChatColor.DARK_RED + String.format(LanguageHandler.translate(PERMISSION_REQUIRED), perm));
            return true;
        }

        if(exec.hasFixedLength()) {
            if(exec.getFixedArgLength() != args.length) {
                cs.sendMessage(PREFIX + ChatColor.RED + LanguageHandler.translate(ERR_CMD_INVALID_ARGUMENTS));
                sendPlayerDescription(cs, exec, true);
                return true;
            }
        } else {
            if(args.length < exec.getMinArgLength()) {
                cs.sendMessage(PREFIX + ChatColor.RED + LanguageHandler.translate(ERR_CMD_NOT_ENOUGH_ARGUMENTS));
                sendPlayerDescription(cs, exec, true);
                return true;
            }
        }

        if(category.allowsConsole) {
            exec.execute(cs, args);
        } else {
            ((PlayerCmobCommand) exec).execute((Player) cs, args);
        }
        return true;
    }

    public static boolean hasPermissions(Permissible perm, String explicitCommandPermission) {
        return perm.hasPermission(explicitCommandPermission) || perm.isOp() || perm.hasPermission(LibMisc.PERM_ALL);
    }

    public static String getPermissionNode(AbstractCmobCommand command) {
        return getPermissionNode(command.category, command.getCommandStart());
    }

    public static String getPermissionNode(String commandCat, String cmdStart) {
        return String.format("custommobs.%s.%s", commandCat, cmdStart);
    }

    private static String forgeDescriptionString(AbstractCmobCommand cmobCommand) {
        return ChatColor.GOLD + LanguageHandler.translate(cmobCommand.getInputDescriptionKey()) + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + LanguageHandler.translate(cmobCommand.getUsageDescriptionKey());
    }

    public static void sendPlayerDescription(CommandSender cs, AbstractCmobCommand cmobCommand, boolean additional) {
        cs.sendMessage(PREFIX + forgeDescriptionString(cmobCommand));

        if(additional) {
            String key = cmobCommand.getAdditionalInformationKey();
            if(LanguageHandler.canTranslate(key)) {
                cs.sendMessage(ChatColor.GREEN + LanguageHandler.translate(key));
            }
        }
        if(cmobCommand.getLocalizedSpecialMessage() != null) {
            cs.sendMessage(ChatColor.GREEN + cmobCommand.getLocalizedSpecialMessage());
        }
    }

}
