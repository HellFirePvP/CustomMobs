package de.hellfirepvp.util;

import java.util.Iterator;
import java.util.Map;

public final class WeightedRandom
{
    public static <T> T getWeightedRandomChoice(final Map<T, Double> items) {
        double totalWeight = 0.0;
        for (final T i : items.keySet()) {
            totalWeight += items.get(i);
        }
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        final Object[] keySet = items.keySet().toArray();
        for (int j = 0; j < items.size(); ++j) {
            random -= items.get(keySet[j]);
            if (random <= 0.0) {
                randomIndex = j;
                break;
            }
        }
        return (T)keySet[randomIndex];
    }
}
