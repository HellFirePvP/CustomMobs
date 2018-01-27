package de.hellfirepvp.tool;

import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import java.util.Map;
import de.hellfirepvp.chat.ChatController;

public class ToolController implements ChatController.ChatHandle
{
    private Map<String, LivingEntity> usingTool;
    
    public ToolController() {
        this.usingTool = new HashMap<String, LivingEntity>();
    }
    
    public void leftClickedMob(final Player player, final LivingEntity entity) {
        this.usingTool.put(player.getName(), entity);
    }
    
    public void flush(final Player player) {
        this.usingTool.remove(player.getName());
    }
    
    @Override
    public boolean needsToHandle(final Player player) {
        return this.usingTool.containsKey(player.getName());
    }
    
    @Override
    public void handle(final Player player, final String in) {
        player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_ATTEMPT), in));
        final LivingEntity entity = this.usingTool.get(player.getName());
        CustomMobs.instance.getToolController().flush(player);
        if (MobFactory.tryCreateCustomMobFromEntity(entity, in)) {
            player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_SAVED), in));
        }
        else {
            player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_ERROR), in));
        }
    }
}
