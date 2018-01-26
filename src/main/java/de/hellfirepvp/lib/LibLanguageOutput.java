package de.hellfirepvp.lib;

import org.bukkit.ChatColor;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: LibLanguageOutput
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:02
 */
public class LibLanguageOutput {

    public static final String PREFIX = ChatColor.DARK_AQUA + "[- " + ChatColor.GOLD + "CustomMobs" + ChatColor.DARK_AQUA + " -] " + ChatColor.RESET;

    public static String NO_CONSOLE = "command.error.noconsole";

    public static String NO_PERMISSION = "command.error.nopermission";
    public static String PERMISSION_REQUIRED = "command.error.requiredpermission";

    public static String CMD_DESCRIPTION_HEADER = "<<- CustomMobs by %s ->>";

    public static String ERR_NO_SUCH_MOB = "command.error.nomob";
    public static String ERR_MOB_EXISTS = "command.error.mobexists";
    public static String ERR_NO_SUCH_MOBTYPE = "command.error.notype.mob";
    public static String ERR_NO_SUCH_POTIONTYPE = "command.error.notype.potion";
    public static String ERR_IO_EXCEPTION = "command.error.file";
    public static String ERR_NO_SUCH_COMMAND = "command.error.nocommand";
    public static String ERR_CMD_NOT_ENOUGH_ARGUMENTS = "command.error.missingargs";
    public static String ERR_CMD_INVALID_ARGUMENTS = "command.error.invalidargs";

    public static String TOOL_DISPLAY = "tool.display";
    public static String TOOL_LEFTCLICK = "tool.left";
    public static String TOOL_LEFTCLICK_LINE1 = "tool.left.line1";
    public static String TOOL_LEFTCLICK_LINE2 = "tool.left.line2";
    public static String TOOL_LEFTCLICK_LINE3 = "tool.left.line3";
    public static String TOOL_LEFTCLICK_LINE4 = "tool.left.line4";
    public static String TOOL_RIGHTCLICK = "tool.right";
    public static String TOOL_RIGHTCLICK_LINE1 = "tool.right.line1";
    public static String TOOL_RIGHTCLICK_LINE2 = "tool.right.line2";
    public static String TOOL_RIGHTCLICK_LINE3 = "tool.right.line3";
    public static String TOOL_RIGHTCLICK_LINE4 = "tool.right.line4";

    public static String TOOL_INTERACT_ALREADY_SAVED = "tool.interact.saved";
    public static String TOOL_INTERACT_REQUEST_NAME = "tool.interact.save.entername";
    public static String TOOL_INTERACT_CHAT_ATTEMPT = "tool.interact.save.start";
    public static String TOOL_INTERACT_CHAT_SAVED = "tool.interact.save.success";
    public static String TOOL_INTERACT_CHAT_ERROR = "tool.interact.save.error";

}
