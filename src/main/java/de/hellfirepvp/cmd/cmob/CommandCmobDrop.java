package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.DataAdapter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobDrop
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
 */
public class CommandCmobDrop extends PlayerCmobCommand {

    @Override
    public String getCommandStart() {
        return "drop";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 3;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }

    @Override
    public void execute(Player p, String[] args) {
        args[2] = args[2].replace(",", ".");
        String name = args[1];
        String chanceStr = args[2];
        float chance;
        try {
            chance = Float.parseFloat(chanceStr);
            if(chance < 0.0F) throw new Exception();
            if(chance > 1.0F) throw new Exception();
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAFloatNumberNormalized(p, chanceStr);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        DataAdapter adapter = cmob.getDataAdapter();
        Map<ItemStack, Double> drops = adapter.getItemDrops();
        ItemStack stack = p.getInventory().getItemInMainHand();
        if(stack == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.drop.noitem"));
            BaseCommand.sendPlayerDescription(p, this, true);
        } else {
            for (ItemStack st : drops.keySet()) {
                if(st.getType().equals(stack.getType())) {
                    drops.remove(st);
                    drops.put(stack, (double) chance);
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.success.overwritten"), stack.getType().name()));
                    return;
                }
            }
            drops.put(stack, (double) chance);
            adapter.setDrops(drops);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.success"), stack.getType().name(), String.valueOf(chance)));
        }
    }
}
