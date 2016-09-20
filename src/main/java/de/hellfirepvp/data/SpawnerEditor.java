package de.hellfirepvp.data;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.api.data.callback.SpawnerDataCallback;
import de.hellfirepvp.file.write.SpawnerDataWriter;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnerEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:38
 */
public class SpawnerEditor implements ISpawnerEditor {

    @Override
    public SpawnerDataCallback setSpawner(ICustomMob mob, Location location) {
        return setSpawner(mob, location, -1);
    }

    @Override
    public SpawnerDataCallback setSpawner(ICustomMob mob, Location location, Integer delay) {
        return SpawnerDataWriter.setSpawner(location, new SpawnerDataHolder.Spawner(mob, delay, delay > 0));
    }

    @Override
    public SpawnerDataCallback resetSpawner(Location location) {
        return SpawnerDataWriter.resetSpawner(location);
    }

    @Nullable
    @Override
    public SpawnerInfo getSpawner(Location location) {
        SpawnerDataHolder.Spawner s = CustomMobs.instance.getSpawnerDataHolder().getSpawnerAt(location);
        return new SpawnerInfo(location, s);
    }

    @Override
    public Collection<SpawnerInfo> getAllSpawners() {
        Map<Location, SpawnerDataHolder.Spawner> spawnerMap = CustomMobs.instance.getSpawnerDataHolder().getSpawners();
        return spawnerMap.entrySet().stream().map(entry -> new SpawnerInfo(entry.getKey(), entry.getValue())).collect(Collectors.toCollection(LinkedList::new));
    }

}
