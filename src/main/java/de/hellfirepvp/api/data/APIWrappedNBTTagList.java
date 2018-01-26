package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;

public interface APIWrappedNBTTagList extends WrappedNBTTagList
{
    APIWrappedNBTTagCompound appendNewTagCompound() throws UnsupportedNBTTypeException;
    
    APIWrappedNBTTagList appendNewTagList() throws UnsupportedNBTTypeException;
}
