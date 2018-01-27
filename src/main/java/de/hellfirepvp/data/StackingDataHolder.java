package de.hellfirepvp.data;

import java.util.Iterator;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.write.StackingDataWriter;
import de.hellfirepvp.file.read.StackingDataReader;
import java.util.LinkedList;
import java.util.List;

public class StackingDataHolder
{
    private List<MobStack> stacks;
    
    public StackingDataHolder() {
        this.stacks = new LinkedList<MobStack>();
    }
    
    public void load() {
        StackingDataWriter.write(this.stacks = StackingDataReader.readStackingData());
    }
    
    public boolean canAddStack(final MobStack newStack) {
        return !discoverCircularStacking(this.stacks, newStack);
    }
    
    public void uncheckedAddStack(final MobStack newStack) {
        this.stacks.add(newStack);
        StackingDataWriter.write(this.stacks);
        this.load();
    }
    
    public CustomMob getMountingMob(final CustomMob mounted) {
        final String name = this.getMountingMobName(mounted);
        if (name == null) {
            return null;
        }
        return CustomMobs.instance.getMobDataHolder().getCustomMob(name);
    }
    
    public void unStack(final CustomMob mounted) {
        final MobStack stack = getStackIfExists(this.stacks, mounted.getMobFileName());
        this.stacks.remove(stack);
        StackingDataWriter.write(this.stacks);
        this.load();
    }
    
    public String getMountingMobName(final CustomMob mounted) {
        return this.getMountingMobName(mounted.getMobFileName());
    }
    
    public String getMountingMobName(final String mounted) {
        final MobStack stack = getStackIfExists(this.stacks, mounted);
        if (stack == null) {
            return null;
        }
        return stack.mounting;
    }
    
    public static boolean discoverCircularStacking(final List<MobStack> stacking, final MobStack newStack) {
        final String searching = newStack.mounted;
        String searchingFrom = newStack.mounting;
        while (!searchingFrom.equals(searching)) {
            final MobStack mobStack = getStackIfExists(stacking, searchingFrom);
            if (mobStack != null) {
                searchingFrom = mobStack.mounting;
            }
            else {
                searchingFrom = null;
            }
            if (searchingFrom == null) {
                return false;
            }
        }
        return true;
    }
    
    public static MobStack getStackIfExists(final List<MobStack> stacks, final String sMounted) {
        for (final MobStack st : stacks) {
            if (st.mounted.equals(sMounted)) {
                return st;
            }
        }
        return null;
    }
    
    public static class MobStack
    {
        public final String mounted;
        public final String mounting;
        
        public MobStack(final String mounted, final String mounting) {
            this.mounted = mounted;
            this.mounting = mounting;
        }
    }
}
