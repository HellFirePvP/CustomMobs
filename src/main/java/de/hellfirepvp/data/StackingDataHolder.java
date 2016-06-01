package de.hellfirepvp.data;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.read.StackingDataReader;
import de.hellfirepvp.file.write.StackingDataWriter;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: StackingDataHolder
 * Created by HellFirePvP
 * Date: 31.05.2016 / 19:12
 */
public class StackingDataHolder {

    private List<MobStack> stacks = new LinkedList<>();

    public void load() {
        stacks = StackingDataReader.readStackingData();
        StackingDataWriter.write(stacks); //Update file.
    }

    public boolean canAddStack(MobStack newStack) {
        return !discoverCircularStacking(stacks, newStack);
    }

    public void uncheckedAddStack(MobStack newStack) {
        this.stacks.add(newStack);
        StackingDataWriter.write(stacks);
        load();
    }

    public CustomMob getMountingMob(CustomMob mounted) {
        String name = getMountingMobName(mounted);
        if(name == null) return null;
        return CustomMobs.instance.getMobDataHolder().getCustomMob(name);
    }

    public void unStack(CustomMob mounted) {
        MobStack stack = getStackIfExists(stacks, mounted.getMobFileName());
        stacks.remove(stack);
        StackingDataWriter.write(stacks);
        load();
    }

    public String getMountingMobName(CustomMob mounted) {
        return getMountingMobName(mounted.getMobFileName());
    }

    public String getMountingMobName(String mounted) {
        MobStack stack = getStackIfExists(stacks, mounted);
        if(stack == null) return null;
        return stack.mounting;
    }

    //True, if we found a circle.
    public static boolean discoverCircularStacking(List<StackingDataHolder.MobStack> stacking, StackingDataHolder.MobStack newStack) {
        String searching = newStack.mounted;
        String searchingFrom = newStack.mounting;
        do {
            if(searchingFrom.equals(searching)) return true; //Circle.
            StackingDataHolder.MobStack mobStack = getStackIfExists(stacking, searchingFrom);
            if(mobStack != null) {
                searchingFrom = mobStack.mounting;
            } else {
                searchingFrom = null;
            }
        } while (searchingFrom != null);
        return false;
    }

    public static StackingDataHolder.MobStack getStackIfExists(List<StackingDataHolder.MobStack> stacks, String sMounted) {
        for (StackingDataHolder.MobStack st : stacks) {
            if(st.mounted.equals(sMounted)) return st;
        }
        return null;
    }

    public static class MobStack {

        public final String mounted, mounting;

        public MobStack(String mounted, String mounting) {
            this.mounted = mounted;
            this.mounting = mounting;
        }
    }

}
