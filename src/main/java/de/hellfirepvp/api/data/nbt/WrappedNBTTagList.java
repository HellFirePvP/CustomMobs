package de.hellfirepvp.api.data.nbt;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WrappedNBTTagList
 * Created by HellFirePvP
 * Date: 24.05.2016 / 13:19
 */
public interface WrappedNBTTagList extends Iterable<Object> {

    public Object getRawNMSTagList();

    public boolean appendItemStack(ItemStack stack);

    public boolean appendNewElement(Object element);

    public boolean appendTagCompound(WrappedNBTTagCompound compound);

    public boolean appendTagList(WrappedNBTTagList list);

    public boolean hasElementType();

    public NBTTagType getElementType();

    public NullableIndexedElementIterator<Object> getElementIterator(boolean unmodifiable);

    public NullableIndexedElementIterator<Object> getElementIterator();

    @Nullable
    public Object getElementAtIndex(int index);

    public WrappedNBTTagList unmodifiable();

    public int size();

}
