package de.hellfirepvp.util;

import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WeightedRandom
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public final class WeightedRandom {

    public static <T> T getWeightedRandomChoice(Map<T, Double> items) {
        double totalWeight = 0.0d;
        for (T i : items.keySet()) {
            totalWeight += items.get(i);
        }
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        Object[] keySet = items.keySet().toArray();
        for (int i = 0; i < items.size(); ++i) {
            random -= items.get(keySet[i]);
            if (random <= 0.0d) {
                randomIndex = i;
                break;
            }
        }
        return (T) keySet[randomIndex];
    }

}
