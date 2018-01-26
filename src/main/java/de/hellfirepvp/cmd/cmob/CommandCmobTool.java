package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobTool
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:07
 */
public class CommandCmobTool extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "tool";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 1;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        ItemStack tool = CustomMobsTool.getTool();

        Inventory inv = p.getInventory();
        ItemStack[] contents = inv.getContents();
        for(ItemStack stack : contents) {
            if(tool.isSimilar(stack)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.tool.failed"));
                return;
            }
        }

        p.getInventory().addItem(tool);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("command.cmob.tool.success"));
    }
}
