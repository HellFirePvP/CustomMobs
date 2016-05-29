package de.hellfirepvp.cmd;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.cai.CommandCaiLeash;
import de.hellfirepvp.cmd.ccmob.CommandCCmobRemove;
import de.hellfirepvp.cmd.ccmob.CommandCCmobSpawn;
import de.hellfirepvp.cmd.cnbt.CommandCnbtSet;
import de.hellfirepvp.cmd.cspawn.CommandCspawnAdd;
import de.hellfirepvp.cmd.cspawn.CommandCspawnList;
import de.hellfirepvp.cmd.cspawn.CommandCspawnRemove;
import de.hellfirepvp.cmd.cmob.CommandCmobBurn;
import de.hellfirepvp.cmd.cmob.CommandCmobCmd;
import de.hellfirepvp.cmd.cmob.CommandCmobCreate;
import de.hellfirepvp.cmd.cmob.CommandCmobDelete;
import de.hellfirepvp.cmd.cmob.CommandCmobDrop;
import de.hellfirepvp.cmd.cmob.CommandCmobEquip;
import de.hellfirepvp.cmd.cmob.CommandCmobExp;
import de.hellfirepvp.cmd.cmob.CommandCmobFireProof;
import de.hellfirepvp.cmd.cmob.CommandCmobHealth;
import de.hellfirepvp.cmd.cmob.CommandCmobLimit;
import de.hellfirepvp.cmd.cmob.CommandCmobList;
import de.hellfirepvp.cmd.cmob.CommandCmobName;
import de.hellfirepvp.cmd.cmob.CommandCmobRemove;
import de.hellfirepvp.cmd.cmob.CommandCmobResetPotion;
import de.hellfirepvp.cmd.cmob.CommandCmobSetBaby;
import de.hellfirepvp.cmd.cmob.CommandCmobSetPotion;
import de.hellfirepvp.cmd.cmob.CommandCmobSpawn;
import de.hellfirepvp.cmd.cmob.CommandCmobSpawner;
import de.hellfirepvp.cmd.cmob.CommandCmobTool;
import de.hellfirepvp.cmd.crespawn.CommandCrespawnAdd;
import de.hellfirepvp.cmd.crespawn.CommandCrespawnAddLoc;
import de.hellfirepvp.cmd.crespawn.CommandCrespawnRemove;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandRegistry
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 3:59
 */
public class CommandRegistry {

    private static Map<CommandCategory, LinkedList<? extends AbstractCmobCommand>> commands = new HashMap<>();

    public static AbstractCmobCommand getCommand(CommandRegisterKey key) {
        return getCommand(key.getCategory(), key.getCommandStart());
    }

    private static AbstractCmobCommand getCommand(CommandCategory category, String commandIdentifier) {
        if(!commands.containsKey(category)) return null;
        for(AbstractCmobCommand cmd : commands.get(category)) {
            if(cmd.getCommandStart().equalsIgnoreCase(commandIdentifier)) return cmd;
        }
        return null;
    }

    public static void initializeCommands() {
        BaseCommand handler = new BaseCommand();
        for (CommandCategory cat : CommandCategory.values()) {
            Bukkit.getServer().getPluginCommand(cat.name.toLowerCase()).setExecutor(handler);
            CustomMobs.logger.info("Registered command \"/" + cat.name + "\"!");
        }
    }

    public static List<? extends AbstractCmobCommand> getAllRegisteredCommands(CommandCategory category) {
        return commands.get(category);
    }

    static {
        LinkedList<AbstractCmobCommand> cmobCommands = new LinkedList<>();
        cmobCommands.add(new CommandCmobBurn()       .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobCmd()        .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobCreate()     .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobDelete()     .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobDrop()       .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobEquip()      .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobExp()        .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobFireProof()  .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobHealth()     .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobLimit()      .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobList()       .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobName()       .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobRemove()     .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobResetPotion().setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSetBaby()    .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSetPotion()  .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSpawn()      .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobSpawner()    .setCategory(CommandCategory.CMOB));
        cmobCommands.add(new CommandCmobTool()       .setCategory(CommandCategory.CMOB));
        commands.put(CommandCategory.CMOB, cmobCommands);

        LinkedList<AbstractCmobCommand> ccmobCommands = new LinkedList<>();
        ccmobCommands.add(new CommandCCmobRemove().setCategory(CommandCategory.CCMOB));
        ccmobCommands.add(new CommandCCmobSpawn() .setCategory(CommandCategory.CCMOB));
        commands.put(CommandCategory.CCMOB, ccmobCommands);

        LinkedList<AbstractCmobCommand> cspawnCommands = new LinkedList<>();
        cspawnCommands.add(new CommandCspawnAdd()   .setCategory(CommandCategory.CSPAWN));
        cspawnCommands.add(new CommandCspawnRemove().setCategory(CommandCategory.CSPAWN));
        cspawnCommands.add(new CommandCspawnList()  .setCategory(CommandCategory.CSPAWN));
        commands.put(CommandCategory.CSPAWN, cspawnCommands);

        LinkedList<AbstractCmobCommand> crespawnCommands = new LinkedList<>();
        crespawnCommands.add(new CommandCrespawnAdd()   .setCategory(CommandCategory.CRESPAWN));
        crespawnCommands.add(new CommandCrespawnAddLoc().setCategory(CommandCategory.CRESPAWN));
        crespawnCommands.add(new CommandCrespawnRemove().setCategory(CommandCategory.CRESPAWN));
        commands.put(CommandCategory.CRESPAWN, crespawnCommands);

        LinkedList<AbstractCmobCommand> ccontrolCommands = new LinkedList<>();
        //if(CustomMobs.instance.getConfigHandler().useFullControl()) {
        //ccontrolCommands.add(new CommandCcontrolList().setCategory(CommandCategory.CCONTROL)); TODO add again later.
        //}
        commands.put(CommandCategory.CCONTROL, ccontrolCommands);

        LinkedList<AbstractCmobCommand> cnbtCommands = new LinkedList<>();
        cnbtCommands.add(new CommandCnbtSet().setCategory(CommandCategory.CNBT));
        commands.put(CommandCategory.CNBT, cnbtCommands);

        LinkedList<AbstractCmobCommand> caiCommands = new LinkedList<>();
        caiCommands.add(new CommandCaiLeash().setCategory(CommandCategory.CAI));
        commands.put(CommandCategory.CAI, caiCommands);
    }

    public static class CommandRegisterKey {

        private final CommandCategory category;
        private final String commandStart;

        public CommandRegisterKey(CommandCategory category, String commandStart) {
            this.category = category;
            this.commandStart = commandStart;
        }

        public CommandCategory getCategory() {
            return category;
        }

        public String getCommandStart() {
            return commandStart;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CommandRegisterKey that = (CommandRegisterKey) o;
            return category == that.category &&
                    !(commandStart != null ? !commandStart.equals(that.commandStart) : that.commandStart != null);
        }

        @Override
        public int hashCode() {
            int result = category != null ? category.hashCode() : 0;
            result = 31 * result + (commandStart != null ? commandStart.hashCode() : 0);
            return result;
        }
    }

    public static enum CommandCategory {

        CMOB("cmob"),
        CCMOB(true, "ccmob"),
        CSPAWN("cspawn"),
        CRESPAWN("crespawn"),
        CCONTROL(true, "ccontrol"),
        CAI("cai"),
        CNBT("cnbt");

        public final boolean allowsConsole;
        private final String name;

        private CommandCategory(String name) {
            this(false, name);
        }

        private CommandCategory(boolean allowsConsole, String name) {
            this.allowsConsole = allowsConsole;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static CommandCategory evaluate(String cmdName) {
            if(cmdName.toLowerCase().startsWith("custommobs:")) cmdName = cmdName.substring(11);
            cmdName = cmdName.toLowerCase();
            for(CommandCategory cat : values()) {
                if(cat == null) continue;

                if(cat.name.equalsIgnoreCase(cmdName)) return cat;
            }
            return null;
        }

    }

}
