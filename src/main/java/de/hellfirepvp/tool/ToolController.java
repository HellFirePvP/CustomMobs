package de.hellfirepvp.tool;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.chat.ChatController;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ToolController
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public class ToolController implements ChatController.ChatHandle {

    private Map<String, LivingEntity> usingTool = new HashMap<>();

    public void leftClickedMob(Player player, LivingEntity entity) {
        usingTool.put(player.getName(), entity);
    }

    public void flush(Player player) {
        usingTool.remove(player.getName());
    }

    @Override
    public boolean needsToHandle(Player player) {
        return usingTool.containsKey(player.getName());
    }

    @Override
    public void handle(Player player, String in) {
        player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_ATTEMPT), in));

        LivingEntity entity = usingTool.get(player.getName());
        CustomMobs.instance.getToolController().flush(player);

        if(MobFactory.tryCreateCustomMobFromEntity(entity, in)) {
            player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_SAVED), in));
        } else {
            player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_ERROR), in));
        }
    }
}
