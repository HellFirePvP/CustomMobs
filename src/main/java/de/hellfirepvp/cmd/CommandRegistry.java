package de.hellfirepvp.cmd;

import de.hellfirepvp.cmd.cai.CommandCaiLeash;
import de.hellfirepvp.cmd.cnbt.CommandCnbtSetraw;
import de.hellfirepvp.cmd.cnbt.CommandCnbtSet;
import de.hellfirepvp.cmd.ccontrol.CommandCcontrolList;
import de.hellfirepvp.cmd.crespawn.CommandCrespawnRemove;
import de.hellfirepvp.cmd.crespawn.CommandCrespawnAddLoc;
import de.hellfirepvp.cmd.crespawn.CommandCrespawnAdd;
import de.hellfirepvp.cmd.cspawn.CommandCspawnList;
import de.hellfirepvp.cmd.cspawn.CommandCspawnRemove;
import de.hellfirepvp.cmd.cspawn.CommandCspawnAdd;
import de.hellfirepvp.cmd.ccmob.CommandCCmobSpawn;
import de.hellfirepvp.cmd.ccmob.CommandCCmobRemove;
import java.util.Collections;
import de.hellfirepvp.cmd.cmob.CommandCmobUnStack;
import de.hellfirepvp.cmd.cmob.CommandCmobTool;
import de.hellfirepvp.cmd.cmob.CommandCmobStack;
import de.hellfirepvp.cmd.cmob.CommandCmobSpawner;
import de.hellfirepvp.cmd.cmob.CommandCmobSpawn;
import de.hellfirepvp.cmd.cmob.CommandCmobSetPotion;
import de.hellfirepvp.cmd.cmob.CommandCmobSetBaby;
import de.hellfirepvp.cmd.cmob.CommandCmobResetPotion;
import de.hellfirepvp.cmd.cmob.CommandCmobRemove;
import de.hellfirepvp.cmd.cmob.CommandCmobName;
import de.hellfirepvp.cmd.cmob.CommandCmobList;
import de.hellfirepvp.cmd.cmob.CommandCmobLimit;
import de.hellfirepvp.cmd.cmob.CommandCmobHealth;
import de.hellfirepvp.cmd.cmob.CommandCmobFireProof;
import de.hellfirepvp.cmd.cmob.CommandCmobExp;
import de.hellfirepvp.cmd.cmob.CommandCmobEquip;
import de.hellfirepvp.cmd.cmob.CommandCmobDrops;
import de.hellfirepvp.cmd.cmob.CommandCmobDelete;
import de.hellfirepvp.cmd.cmob.CommandCmobCreate;
import de.hellfirepvp.cmd.cmob.CommandCmobCmd;
import de.hellfirepvp.cmd.cmob.CommandCmobClone;
import de.hellfirepvp.cmd.cmob.CommandCmobBurn;
import java.util.HashMap;
import java.util.List;
import de.hellfirepvp.CustomMobs;
import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CommandRegistry
{
    private static Map<CommandCategory, LinkedList<? extends AbstractCmobCommand>> commands;
    
    public static AbstractCmobCommand getCommand(final CommandRegisterKey key) {
        return getCommand(key.getCategory(), key.getCommandStart());
    }
    
    private static AbstractCmobCommand getCommand(final CommandCategory category, final String commandIdentifier) {
        if (!CommandRegistry.commands.containsKey(category)) {
            return null;
        }
        for (final AbstractCmobCommand cmd : CommandRegistry.commands.get(category)) {
            if (cmd.getCommandStart().equalsIgnoreCase(commandIdentifier)) {
                return cmd;
            }
        }
        return null;
    }
    
    public static void initializeCommands() {
        final BaseCommand handler = new BaseCommand();
        for (final CommandCategory cat : CommandCategory.values()) {
            Bukkit.getServer().getPluginCommand(cat.name.toLowerCase()).setExecutor((CommandExecutor)handler);
            CustomMobs.logger.debug("Registered commands for \"/" + cat.name + "\"!");
        }
    }
    
    public static List<? extends AbstractCmobCommand> getAllRegisteredCommands(final CommandCategory category) {
        return CommandRegistry.commands.get(category);
    }
    
    static {
        CommandRegistry.commands = new HashMap<CommandCategory, LinkedList<? extends AbstractCmobCommand>>();
        final LinkedList<AbstractCmobCommand> cmobCommands = new LinkedList<AbstractCmobCommand>();
        cmobCommands.add(new CommandCmobBurn().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobClone().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobCmd().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobCreate().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobDelete().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobDrops().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobEquip().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobExp().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobFireProof().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobHealth().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobLimit().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobList().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobName().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobRemove().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobResetPotion().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSetBaby().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSetPotion().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSpawn().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSpawner().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobStack().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobTool().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobUnStack().setCategory(CommandCategory.CMOB));
        Collections.sort(cmobCommands, (o1, o2) -> o1.getCommandStart().compareTo(o2.getCommandStart()));
        CommandRegistry.commands.put(CommandCategory.CMOB, cmobCommands);
        final LinkedList<AbstractCmobCommand> ccmobCommands = new LinkedList<AbstractCmobCommand>();
        ccmobCommands.add(new CommandCCmobRemove().setCategory(CommandCategory.CCMOB));
        ccmobCommands.add(new CommandCCmobSpawn().setCategory(CommandCategory.CCMOB));
        Collections.sort(ccmobCommands, (o1, o2) -> o1.getCommandStart().compareTo(o2.getCommandStart()));
        CommandRegistry.commands.put(CommandCategory.CCMOB, ccmobCommands);
        final LinkedList<AbstractCmobCommand> cspawnCommands = new LinkedList<AbstractCmobCommand>();
        cspawnCommands.add(new CommandCspawnAdd().setCategory(CommandCategory.CSPAWN));
        cspawnCommands.add(new CommandCspawnRemove().setCategory(CommandCategory.CSPAWN));
        cspawnCommands.add(new CommandCspawnList().setCategory(CommandCategory.CSPAWN));
        Collections.sort(cspawnCommands, (o1, o2) -> o1.getCommandStart().compareTo(o2.getCommandStart()));
        CommandRegistry.commands.put(CommandCategory.CSPAWN, cspawnCommands);
        final LinkedList<AbstractCmobCommand> crespawnCommands = new LinkedList<AbstractCmobCommand>();
        crespawnCommands.add(new CommandCrespawnAdd().setCategory(CommandCategory.CRESPAWN));
        crespawnCommands.add(new CommandCrespawnAddLoc().setCategory(CommandCategory.CRESPAWN));
        crespawnCommands.add(new CommandCrespawnRemove().setCategory(CommandCategory.CRESPAWN));
        Collections.sort(crespawnCommands, (o1, o2) -> o1.getCommandStart().compareTo(o2.getCommandStart()));
        CommandRegistry.commands.put(CommandCategory.CRESPAWN, crespawnCommands);
        final LinkedList<AbstractCmobCommand> ccontrolCommands = new LinkedList<AbstractCmobCommand>();
        if (CustomMobs.instance.getConfigHandler().useFullControl()) {
            ccontrolCommands.add(new CommandCcontrolList().setCategory(CommandCategory.CCONTROL));
        }
        CommandRegistry.commands.put(CommandCategory.CCONTROL, ccontrolCommands);
        final LinkedList<AbstractCmobCommand> cnbtCommands = new LinkedList<AbstractCmobCommand>();
        cnbtCommands.add(new CommandCnbtSet().setCategory(CommandCategory.CNBT));
        cnbtCommands.add(new CommandCnbtSetraw().setCategory(CommandCategory.CNBT));
        CommandRegistry.commands.put(CommandCategory.CNBT, cnbtCommands);
        final LinkedList<AbstractCmobCommand> caiCommands = new LinkedList<AbstractCmobCommand>();
        caiCommands.add(new CommandCaiLeash().setCategory(CommandCategory.CAI));
        CommandRegistry.commands.put(CommandCategory.CAI, caiCommands);
    }
    
    public static class CommandRegisterKey
    {
        private final CommandCategory category;
        private final String commandStart;
        
        public CommandRegisterKey(final CommandCategory category, final String commandStart) {
            this.category = category;
            this.commandStart = commandStart;
        }
        
        public CommandCategory getCategory() {
            return this.category;
        }
        
        public String getCommandStart() {
            return this.commandStart;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final CommandRegisterKey that = (CommandRegisterKey)o;
            if (this.category == that.category) {
                if (this.commandStart != null) {
                    if (!this.commandStart.equals(that.commandStart)) {
                        return false;
                    }
                }
                else if (that.commandStart != null) {
                    return false;
                }
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = (this.category != null) ? this.category.hashCode() : 0;
            result = 31 * result + ((this.commandStart != null) ? this.commandStart.hashCode() : 0);
            return result;
        }
    }
    
    public enum CommandCategory
    {
        CMOB("cmob"), 
        CCMOB(true, "ccmob"), 
        CSPAWN("cspawn"), 
        CRESPAWN("crespawn"), 
        CCONTROL(true, "ccontrol"), 
        CAI("cai"), 
        CNBT("cnbt");
        
        public final boolean allowsConsole;
        private final String name;
        
        private CommandCategory(final String name) {
            this(false, name);
        }
        
        private CommandCategory(final boolean allowsConsole, final String name) {
            this.allowsConsole = allowsConsole;
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static CommandCategory evaluate(String cmdName) {
            if (cmdName.toLowerCase().startsWith("custommobs:")) {
                cmdName = cmdName.substring(11);
            }
            cmdName = cmdName.toLowerCase();
            for (final CommandCategory cat : values()) {
                if (cat != null) {
                    if (cat.name.equalsIgnoreCase(cmdName)) {
                        return cat;
                    }
                }
            }
            return null;
        }
    }
}
