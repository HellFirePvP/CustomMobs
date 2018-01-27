package de.hellfirepvp.lib;

import org.bukkit.ChatColor;

public class LibLanguageOutput
{
    public static final String PREFIX;
    public static String NO_CONSOLE;
    public static String NO_PERMISSION;
    public static String PERMISSION_REQUIRED;
    public static String CMD_DESCRIPTION_HEADER;
    public static String ERR_NO_SUCH_MOB;
    public static String ERR_MOB_EXISTS;
    public static String ERR_NO_SUCH_MOBTYPE;
    public static String ERR_NO_SUCH_POTIONTYPE;
    public static String ERR_IO_EXCEPTION;
    public static String ERR_NO_SUCH_COMMAND;
    public static String ERR_CMD_NOT_ENOUGH_ARGUMENTS;
    public static String ERR_CMD_INVALID_ARGUMENTS;
    public static String TOOL_DISPLAY;
    public static String TOOL_LEFTCLICK;
    public static String TOOL_LEFTCLICK_LINE1;
    public static String TOOL_LEFTCLICK_LINE2;
    public static String TOOL_LEFTCLICK_LINE3;
    public static String TOOL_LEFTCLICK_LINE4;
    public static String TOOL_RIGHTCLICK;
    public static String TOOL_RIGHTCLICK_LINE1;
    public static String TOOL_RIGHTCLICK_LINE2;
    public static String TOOL_RIGHTCLICK_LINE3;
    public static String TOOL_RIGHTCLICK_LINE4;
    public static String TOOL_INTERACT_ALREADY_SAVED;
    public static String TOOL_INTERACT_REQUEST_NAME;
    public static String TOOL_INTERACT_CHAT_ATTEMPT;
    public static String TOOL_INTERACT_CHAT_SAVED;
    public static String TOOL_INTERACT_CHAT_ERROR;
    
    static {
        PREFIX = ChatColor.DARK_AQUA + "[- " + ChatColor.GOLD + "CustomMobs" + ChatColor.DARK_AQUA + " -] " + ChatColor.RESET;
        LibLanguageOutput.NO_CONSOLE = "command.error.noconsole";
        LibLanguageOutput.NO_PERMISSION = "command.error.nopermission";
        LibLanguageOutput.PERMISSION_REQUIRED = "command.error.requiredpermission";
        LibLanguageOutput.CMD_DESCRIPTION_HEADER = "<<- CustomMobs by %s ->>";
        LibLanguageOutput.ERR_NO_SUCH_MOB = "command.error.nomob";
        LibLanguageOutput.ERR_MOB_EXISTS = "command.error.mobexists";
        LibLanguageOutput.ERR_NO_SUCH_MOBTYPE = "command.error.notype.mob";
        LibLanguageOutput.ERR_NO_SUCH_POTIONTYPE = "command.error.notype.potion";
        LibLanguageOutput.ERR_IO_EXCEPTION = "command.error.file";
        LibLanguageOutput.ERR_NO_SUCH_COMMAND = "command.error.nocommand";
        LibLanguageOutput.ERR_CMD_NOT_ENOUGH_ARGUMENTS = "command.error.missingargs";
        LibLanguageOutput.ERR_CMD_INVALID_ARGUMENTS = "command.error.invalidargs";
        LibLanguageOutput.TOOL_DISPLAY = "tool.display";
        LibLanguageOutput.TOOL_LEFTCLICK = "tool.left";
        LibLanguageOutput.TOOL_LEFTCLICK_LINE1 = "tool.left.line1";
        LibLanguageOutput.TOOL_LEFTCLICK_LINE2 = "tool.left.line2";
        LibLanguageOutput.TOOL_LEFTCLICK_LINE3 = "tool.left.line3";
        LibLanguageOutput.TOOL_LEFTCLICK_LINE4 = "tool.left.line4";
        LibLanguageOutput.TOOL_RIGHTCLICK = "tool.right";
        LibLanguageOutput.TOOL_RIGHTCLICK_LINE1 = "tool.right.line1";
        LibLanguageOutput.TOOL_RIGHTCLICK_LINE2 = "tool.right.line2";
        LibLanguageOutput.TOOL_RIGHTCLICK_LINE3 = "tool.right.line3";
        LibLanguageOutput.TOOL_RIGHTCLICK_LINE4 = "tool.right.line4";
        LibLanguageOutput.TOOL_INTERACT_ALREADY_SAVED = "tool.interact.saved";
        LibLanguageOutput.TOOL_INTERACT_REQUEST_NAME = "tool.interact.save.entername";
        LibLanguageOutput.TOOL_INTERACT_CHAT_ATTEMPT = "tool.interact.save.start";
        LibLanguageOutput.TOOL_INTERACT_CHAT_SAVED = "tool.interact.save.success";
        LibLanguageOutput.TOOL_INTERACT_CHAT_ERROR = "tool.interact.save.error";
    }
}
