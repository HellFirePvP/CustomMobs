package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import javax.annotation.Nullable;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;

public interface APIWrappedNBTTagCompound extends WrappedNBTTagCompound
{
    @Nullable
    APIWrappedNBTTagCompound getTagCompound(final String p0);
    
    @Nullable
    APIWrappedNBTTagList getTagList(final String p0, final NBTTagType p1);
    
    APIWrappedNBTTagCompound createOrGetSubTag(final String p0);
    
    APIWrappedNBTTagList createOrGetSubList(final String p0, final NBTTagType p1);
}
