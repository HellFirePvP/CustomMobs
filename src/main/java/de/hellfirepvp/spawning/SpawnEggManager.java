package de.hellfirepvp.spawning;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnEggManager
 * Created by HellFirePvP
 * Date: 02.07.2016 / 15:38
 */
public final class SpawnEggManager {

    /*public static ItemStack createSpawnEggFor(ICustomMob mob) {
        String name = mob.getName();
        SpawnEgg eggMaterialData = new SpawnEgg(mob.getEntityType());
        ItemStack item = new ItemStack(Material.MONSTER_EGG);
        item.setData(eggMaterialData);
        WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
        NMSReflector.nbtProvider.saveStack(item, tag);
        tag.setString("CustomMobsEggName", name);
        return NMSReflector.nbtProvider.loadStack(tag);
    }

    public static ICustomMob getMobFromEgg(ItemStack stack) {
        if(stack == null || stack.getType() == null || !stack.getType().equals(Material.MONSTER_EGG)) return null;
        WrappedNBTTagCompound stackTag = NMSReflector.nbtProvider.newTagCompound();
        NMSReflector.nbtProvider.saveStack(stack, stackTag);
        if(!stackTag.hasKey("CustomMobsEggName")) return null;
        String mobName = (String) stackTag.getValue("CustomMobsEggName");
        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
        if(mob == null) return null;
        return mob.createApiAdapter();
    }*/

}
