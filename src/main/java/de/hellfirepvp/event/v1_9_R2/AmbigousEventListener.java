package de.hellfirepvp.event.v1_9_R2;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.CommandRegistry;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.event.GeneralEventListener;
import de.hellfirepvp.lib.LibMisc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EventListener
 * Created by HellFirePvP
 * Date: 26.06.2016 / 20:42
 */
public class AmbigousEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTab(TabCompleteEvent event) {
        String msg = event.getBuffer();
        if(!msg.startsWith("/")) return; //Not a command. Not our business.
        msg = msg.substring(1);
        String[] args = msg.split(" ", -1);
        String command = args[0];
        CommandRegistry.CommandCategory cat = CommandRegistry.CommandCategory.evaluate(command);
        if(cat == null) return; //Not our command. ?
        LinkedList<String> arguments = GeneralEventListener.prepareArgs(args);
        if(!BaseCommand.hasPermissions(event.getSender(), LibMisc.PERM_USE)) return;

        if(arguments.size() == 2) {
            event.getCompletions().clear();
            String arg = arguments.getLast();
            List<? extends AbstractCmobCommand> commands = CommandRegistry.getAllRegisteredCommands(cat);
            for (AbstractCmobCommand cmd : commands) {
                String cmdStart = cmd.getCommandStart();
                if(cmdStart.toLowerCase().startsWith(arg.toLowerCase()) && BaseCommand.hasPermissions(event.getSender(), BaseCommand.getPermissionNode(cmd))) {
                    event.getCompletions().add(cmdStart);
                }
            }
            return;
        }
        if(cat.equals(CommandRegistry.CommandCategory.CNBT) && arguments.size() == 4) {
            event.getCompletions().clear();
            String suggestedCmob = arguments.get(2);
            CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(suggestedCmob);
            if(cmob == null) return;
            String existing = arguments.getLast();
            Collection<String> entries = NBTRegister.getRegister().getEntries((String) cmob.getDataSnapshot().getValue("id"));
            entries.stream().filter(entry -> entry.toLowerCase().startsWith(existing.toLowerCase())).forEach(entry -> event.getCompletions().add(entry));
        }
        if(arguments.size() > 2) {
            String cmdStart = arguments.get(1);
            CommandRegistry.CommandRegisterKey key = new CommandRegistry.CommandRegisterKey(cat, cmdStart);
            AbstractCmobCommand cmd = CommandRegistry.getCommand(key);
            if(cmd == null) return;
            int[] indexes = cmd.getCustomMobArgumentIndex();
            if(indexes.length == 0) return;
            for (int index : indexes) {
                if(index + 1 != arguments.size()) continue;
                event.getCompletions().clear();
                String arg = arguments.getLast().toLowerCase();
                Collection<CustomMob> mobs = CustomMobs.instance.getMobDataHolder().getAllLoadedMobs();
                mobs.stream().filter(mob -> mob.getMobFileName().toLowerCase().startsWith(arg)).forEach(mob -> event.getCompletions().add(mob.getMobFileName()));
            }
        }
    }

}
