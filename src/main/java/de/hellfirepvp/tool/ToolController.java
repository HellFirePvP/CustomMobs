package de.hellfirepvp.tool;

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
public class ToolController {

    private Map<String, LivingEntity> usingTool = new HashMap<>();

    public void leftClickedMob(Player player, LivingEntity entity) {
        usingTool.put(player.getName(), entity);
    }

    public boolean clickedRecently(Player p) {
        return usingTool.containsKey(p.getName());
    }

    public LivingEntity getClicked(Player player) {
        return usingTool.get(player.getName());
    }

    public void flush(Player player) {
        usingTool.remove(player.getName());
    }

}
