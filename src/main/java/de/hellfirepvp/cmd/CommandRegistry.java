package de.hellfirepvp.cmd;

import de.hellfirepvp.cmd.cai.CommandCaiLeash;
import de.hellfirepvp.cmd.ccmob.CommandCCmobRemove;
import de.hellfirepvp.cmd.ccmob.CommandCCmobSpawn;
import de.hellfirepvp.cmd.cconfig.CommandCconfigAdd;
import de.hellfirepvp.cmd.cconfig.CommandCconfigList;
import de.hellfirepvp.cmd.cconfig.CommandCconfigRemove;
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
 * HellFirePvP@Admin
 * Date: 24.03.2015 / 12:23
 * on Project CustomMobs
 * CommandRegistry
 */
public class CommandRegistry {

    private static Map<CommandCategory, LinkedList<AbstractCmobCommand>> commands = new HashMap<>();

    public static AbstractCmobCommand getCommand(CommandCategory category, String commandIdentifier) {
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
        }
    }

    public static List<AbstractCmobCommand> getAllRegisteredCommands(CommandCategory category) {
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

        LinkedList<AbstractCmobCommand> cconfigCommands = new LinkedList<>();
        cconfigCommands.add(new CommandCconfigAdd()   .setCategory(CommandCategory.CCONFIG));
        cconfigCommands.add(new CommandCconfigRemove().setCategory(CommandCategory.CCONFIG));
        cconfigCommands.add(new CommandCconfigList()  .setCategory(CommandCategory.CCONFIG));
        commands.put(CommandCategory.CCONFIG, cconfigCommands);

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

        LinkedList<AbstractCmobCommand> caiCommands = new LinkedList<>();
        caiCommands.add(new CommandCaiLeash().setCategory(CommandCategory.CAI));
        commands.put(CommandCategory.CAI, caiCommands);
    }

    public static enum CommandCategory {

        CMOB("cmob"),
        CCMOB(true, "ccmob"),
        CCONFIG("cconfig"),
        CRESPAWN("crespawn"),
        CCONTROL(true, "ccontrol"),
        CAI("cai");

        public final boolean allowsConsole;
        public final String name;

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
            cmdName = cmdName.toLowerCase();
            for(CommandCategory cat : values()) {
                if(cat == null) continue;

                if(cat.name.equalsIgnoreCase(cmdName)) return cat;
            }
            return CMOB;
        }

    }

}
