package de.hellfirepvp.event.v1_11_R1;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Collection;
import de.hellfirepvp.data.mob.CustomMob;
import java.util.List;
import java.util.LinkedList;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.nms.RegistryTypeProvider;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import org.bukkit.permissions.Permissible;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.event.GeneralEventListener;
import de.hellfirepvp.cmd.CommandRegistry;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.Listener;

public class AmbigousEventListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTab(final TabCompleteEvent event) {
        String msg = event.getBuffer();
        if (!msg.startsWith("/")) {
            return;
        }
        msg = msg.substring(1);
        final String[] args = msg.split(" ", -1);
        final String command = args[0];
        final CommandRegistry.CommandCategory cat = CommandRegistry.CommandCategory.evaluate(command);
        if (cat == null) {
            return;
        }
        final LinkedList<String> arguments = GeneralEventListener.prepareArgs(args);
        if (!BaseCommand.hasPermissions((Permissible)event.getSender(), "custommobs.cmduse")) {
            return;
        }
        if (arguments.size() == 2) {
            event.getCompletions().clear();
            final String arg = arguments.getLast();
            final List<? extends AbstractCmobCommand> commands = CommandRegistry.getAllRegisteredCommands(cat);
            for (final AbstractCmobCommand cmd : commands) {
                final String cmdStart = cmd.getCommandStart();
                if (cmdStart.toLowerCase().startsWith(arg.toLowerCase()) && BaseCommand.hasPermissions((Permissible)event.getSender(), BaseCommand.getPermissionNode(cmd))) {
                    event.getCompletions().add(cmdStart);
                }
            }
            return;
        }
        if (cat.equals(CommandRegistry.CommandCategory.CNBT) && arguments.size() == 4) {
            event.getCompletions().clear();
            final String suggestedCmob = arguments.get(2);
            final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(suggestedCmob);
            if (cmob == null) {
                return;
            }
            final String existing = arguments.getLast();
            String type = (String)cmob.getDataSnapshot().getValue("id");
            type = ((RegistryTypeProvider)NMSReflector.mobTypeProvider).tryTranslateRegistryNameToName(type);
            final Collection<String> entries = NBTRegister.getRegister().getEntries(type);
            entries.stream().filter(entry -> entry.toLowerCase().startsWith(existing.toLowerCase())).forEach(entry -> event.getCompletions().add(entry));
        }
        if (arguments.size() > 2) {
            final String cmdStart2 = arguments.get(1);
            final CommandRegistry.CommandRegisterKey key = new CommandRegistry.CommandRegisterKey(cat, cmdStart2);
            final AbstractCmobCommand cmd2 = CommandRegistry.getCommand(key);
            if (cmd2 == null) {
                return;
            }
            final int[] indexes = cmd2.getCustomMobArgumentIndex();
            if (indexes.length == 0) {
                return;
            }
            for (final int index : indexes) {
                if (index + 1 == arguments.size()) {
                    event.getCompletions().clear();
                    final String arg2 = arguments.getLast().toLowerCase();
                    final Collection<CustomMob> mobs = CustomMobs.instance.getMobDataHolder().getAllLoadedMobs();
                    mobs.stream().filter(mob -> mob.getMobFileName().toLowerCase().startsWith(arg2)).forEach(mob -> event.getCompletions().add(mob.getMobFileName()));
                }
            }
        }
    }
}
