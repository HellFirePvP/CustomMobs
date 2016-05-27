package de.hellfirepvp.tool;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * HellFirePvP@Admin
 * Date: 13.05.2015 / 14:44
 * on Project CustomMobs
 * ToolController
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
