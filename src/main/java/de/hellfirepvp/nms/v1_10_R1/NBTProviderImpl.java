package de.hellfirepvp.nms.v1_10_R1;

import de.hellfirepvp.data.nbt.base.UnmodWrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import javax.annotation.Nullable;
import java.util.Iterator;
import de.hellfirepvp.data.nbt.base.UnmodWrappedNBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagEnd;
import de.hellfirepvp.api.data.nbt.NullableIndexedElementIterator;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import net.minecraft.server.v1_10_R1.NBTTagIntArray;
import net.minecraft.server.v1_10_R1.NBTTagByteArray;
import net.minecraft.server.v1_10_R1.NBTTagString;
import net.minecraft.server.v1_10_R1.NBTTagDouble;
import net.minecraft.server.v1_10_R1.NBTTagFloat;
import net.minecraft.server.v1_10_R1.NBTTagLong;
import net.minecraft.server.v1_10_R1.NBTTagShort;
import net.minecraft.server.v1_10_R1.NBTTagByte;
import net.minecraft.server.v1_10_R1.NBTTagInt;
import net.minecraft.server.v1_10_R1.NBTBase;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import java.io.OutputStream;
import net.minecraft.server.v1_10_R1.NBTTagList;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.data.nbt.base.NBTProvider;

public class NBTProviderImpl implements NBTProvider
{
    @Override
    public void saveStack(final ItemStack stack, final WrappedNBTTagCompound target) {
        if (stack == null || target == null) {
            return;
        }
        CraftItemStack.asNMSCopy(stack).save((NBTTagCompound)target.getRawNMSTagCompound());
    }
    
    @Override
    public ItemStack loadStack(final WrappedNBTTagCompound savedStack) {
        if (savedStack == null) {
            return null;
        }
        return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_10_R1.ItemStack.createStack((NBTTagCompound)savedStack.getRawNMSTagCompound()));
    }
    
    @Override
    public WrappedNBTTagCompound newTagCompound() {
        return new TagCompoundImpl(new NBTTagCompound());
    }
    
    @Override
    public WrappedNBTTagList newTagList() {
        return new TagListImpl(new NBTTagList());
    }
    
    @Override
    public void write(final OutputStream stream, final WrappedNBTTagCompound tag) throws IOException {
        NBTCompressedStreamTools.a((NBTTagCompound)tag.getRawNMSTagCompound(), stream);
    }
    
    @Override
    public WrappedNBTTagCompound read(final InputStream stream) throws IOException {
        return new TagCompoundImpl(NBTCompressedStreamTools.a(stream));
    }
    
    protected static NBTBase wrapValue(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (NBTBase)new NBTTagInt((int)value);
        }
        if (value instanceof Boolean) {
            return (NBTBase)new NBTTagByte((byte)(((boolean)value) ? 1 : 0));
        }
        if (value instanceof Byte) {
            return (NBTBase)new NBTTagByte((byte)value);
        }
        if (value instanceof Short) {
            return (NBTBase)new NBTTagShort((short)value);
        }
        if (value instanceof Long) {
            return (NBTBase)new NBTTagLong((long)value);
        }
        if (value instanceof Float) {
            return (NBTBase)new NBTTagFloat((float)value);
        }
        if (value instanceof Double) {
            return (NBTBase)new NBTTagDouble((double)value);
        }
        if (value instanceof String) {
            return (NBTBase)new NBTTagString((String)value);
        }
        if (value instanceof byte[]) {
            return (NBTBase)new NBTTagByteArray((byte[])value);
        }
        if (value instanceof int[]) {
            return (NBTBase)new NBTTagIntArray((int[])value);
        }
        return null;
    }
    
    protected static Object extractValue(final NBTBase nbtBase) {
        if (nbtBase == null) {
            return null;
        }
        switch (nbtBase.getTypeId()) {
            case 1: {
                return ((NBTTagByte)nbtBase).g();
            }
            case 2: {
                return ((NBTTagShort)nbtBase).f();
            }
            case 3: {
                return ((NBTTagInt)nbtBase).e();
            }
            case 4: {
                return ((NBTTagLong)nbtBase).d();
            }
            case 5: {
                return ((NBTTagFloat)nbtBase).i();
            }
            case 6: {
                return ((NBTTagDouble)nbtBase).h();
            }
            case 7: {
                return ((NBTTagByteArray)nbtBase).c();
            }
            case 8: {
                return ((NBTTagString)nbtBase).c_();
            }
            case 11: {
                return ((NBTTagIntArray)nbtBase).d();
            }
            default: {
                return null;
            }
        }
    }
    
    public static class TagListImpl implements WrappedNBTTagList
    {
        private NBTTagList parentList;
        
        public TagListImpl(final NBTTagList parentList) {
            this.parentList = parentList;
        }
        
        @Override
        public Object getRawNMSTagList() {
            return this.parentList;
        }
        
        @Override
        public boolean appendItemStack(final ItemStack stack) {
            if (stack == null) {
                return false;
            }
            if (this.hasElementType() && NBTTagType.TAG_COMPOUND != this.getElementType()) {
                return false;
            }
            final WrappedNBTTagCompound cmp = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(stack, cmp);
            this.parentList.add((NBTBase)cmp.getRawNMSTagCompound());
            return true;
        }
        
        @Override
        public boolean appendNewElement(final Object element) {
            final NBTBase wrapped = NBTProviderImpl.wrapValue(element);
            if (wrapped == null) {
                return false;
            }
            final NBTTagType wrappedType = NBTTagType.getById((int)wrapped.getTypeId());
            if (this.hasElementType() && wrappedType != this.getElementType()) {
                return false;
            }
            this.parentList.add(wrapped);
            return true;
        }
        
        @Override
        public boolean appendTagCompound(final WrappedNBTTagCompound compound) {
            if (this.hasElementType() && this.getElementType() != NBTTagType.TAG_COMPOUND) {
                return false;
            }
            this.parentList.add((NBTBase)compound.getRawNMSTagCompound());
            return true;
        }
        
        @Override
        public boolean appendTagList(final WrappedNBTTagList list) {
            if (this.hasElementType() && this.getElementType() != NBTTagType.TAG_LIST) {
                return false;
            }
            this.parentList.add((NBTBase)list.getRawNMSTagList());
            return true;
        }
        
        @Override
        public boolean hasElementType() {
            return this.parentList.g() != 0;
        }
        
        @Override
        public NBTTagType getElementType() {
            return NBTTagType.getById(this.parentList.g());
        }
        
        @Override
        public NullableIndexedElementIterator<Object> getElementIterator(final boolean unmodifiable) {
            return new ForIntIterator(this.parentList, unmodifiable);
        }
        
        @Override
        public NullableIndexedElementIterator<Object> getElementIterator() {
            return this.getElementIterator(false);
        }
        
        @Override
        public Object getElementAtIndex(final int index) {
            final NBTBase element = this.parentList.h(index);
            final Object value = NBTProviderImpl.extractValue(element);
            if (value != null) {
                return value;
            }
            if (element instanceof NBTTagEnd) {
                return null;
            }
            if (element instanceof NBTTagCompound) {
                return new TagCompoundImpl((NBTTagCompound)element);
            }
            if (element instanceof NBTTagList) {
                return new TagListImpl((NBTTagList)element);
            }
            return element;
        }
        
        @Override
        public WrappedNBTTagList unmodifiable() {
            return new UnmodWrappedNBTTagList(this);
        }
        
        @Override
        public int size() {
            return this.parentList.size();
        }
        
        @Override
        public Iterator<Object> iterator() {
            return this.getElementIterator();
        }
    }
    
    public static class ForIntIterator implements NullableIndexedElementIterator<Object>
    {
        private NBTTagList list;
        private int entryPointer;
        private boolean unmodifiable;
        private boolean removeCalledThisIteration;
        
        public ForIntIterator(final NBTTagList list, final boolean unmodifiable) {
            this.removeCalledThisIteration = false;
            this.list = list;
            this.entryPointer = -1;
            this.unmodifiable = unmodifiable;
        }
        
        @Override
        public boolean hasNext() {
            return this.entryPointer + 1 < this.list.size();
        }
        
        @Nullable
        @Override
        public Object next() {
            ++this.entryPointer;
            this.removeCalledThisIteration = false;
            final NBTBase element = this.list.h(this.entryPointer);
            final Object value = NBTProviderImpl.extractValue(element);
            if (value != null) {
                return value;
            }
            if (element instanceof NBTTagCompound) {
                WrappedNBTTagCompound cmp = new TagCompoundImpl((NBTTagCompound)element);
                if (this.unmodifiable) {
                    cmp = cmp.unmodifiable();
                }
                return cmp;
            }
            if (element instanceof NBTTagList) {
                WrappedNBTTagList list = new TagListImpl((NBTTagList)element);
                if (this.unmodifiable) {
                    list = list.unmodifiable();
                }
                return list;
            }
            return element;
        }
        
        @Override
        public void remove() {
            if (this.unmodifiable) {
                throw new UnsupportedOperationException("remove (unmodifiable NBTIterator)");
            }
            if (this.removeCalledThisIteration) {
                throw new IllegalStateException("remove already called for this element");
            }
            this.list.remove(this.entryPointer);
            --this.entryPointer;
            this.removeCalledThisIteration = true;
        }
        
        @Override
        public int getCurrentIndex() {
            return this.entryPointer;
        }
    }
    
    public static class TagCompoundImpl implements WrappedNBTTagCompound
    {
        private NBTTagCompound parent;
        
        private TagCompoundImpl(final NBTTagCompound compound) {
            this.parent = compound;
        }
        
        @Override
        public Object getRawNMSTagCompound() {
            return this.parent;
        }
        
        @Override
        public void removeKey(final String key) {
            this.parent.remove(key);
        }
        
        @Override
        public boolean hasKey(final String key) {
            return this.parent.hasKey(key);
        }
        
        @Override
        public void setSubTag(final String key, final WrappedNBTTagCompound subTag) {
            this.parent.set(key, (NBTBase)subTag.getRawNMSTagCompound());
        }
        
        @Override
        public void setSubList(final String key, final WrappedNBTTagList listTag) {
            this.parent.set(key, (NBTBase)listTag.getRawNMSTagList());
        }
        
        @Nullable
        @Override
        public ItemStack getItemStack(final String key) {
            if (!this.hasKey(key)) {
                return null;
            }
            return NMSReflector.nbtProvider.loadStack(this.getTagCompound(key));
        }
        
        @Override
        public void setItemStack(final String key, final ItemStack stack) {
            if (stack == null) {
                return;
            }
            final WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(stack, tag);
            this.parent.set(key, (NBTBase)tag.getRawNMSTagCompound());
        }
        
        @Override
        public void set(final String key, final Object value) throws UnsupportedNBTTypeException {
            if (value instanceof Integer) {
                this.parent.setInt(key, (int)value);
                return;
            }
            if (value instanceof Boolean) {
                this.parent.setBoolean(key, (boolean)value);
                return;
            }
            if (value instanceof Byte) {
                this.parent.setByte(key, (byte)value);
                return;
            }
            if (value instanceof Short) {
                this.parent.setShort(key, (short)value);
                return;
            }
            if (value instanceof Long) {
                this.parent.setLong(key, (long)value);
                return;
            }
            if (value instanceof Float) {
                this.parent.setFloat(key, (float)value);
                return;
            }
            if (value instanceof Double) {
                this.parent.setDouble(key, (double)value);
                return;
            }
            if (value instanceof String) {
                this.parent.setString(key, (String)value);
                return;
            }
            if (value instanceof byte[]) {
                this.parent.setByteArray(key, (byte[])value);
                return;
            }
            if (value instanceof int[]) {
                this.parent.setIntArray(key, (int[])value);
                return;
            }
            throw new UnsupportedNBTTypeException();
        }
        
        @Override
        public void setInt(final String key, final int value) {
            this.parent.setInt(key, value);
        }
        
        @Override
        public void setByte(final String key, final byte value) {
            this.parent.setByte(key, value);
        }
        
        @Override
        public void setShort(final String key, final short value) {
            this.parent.setShort(key, value);
        }
        
        @Override
        public void setLong(final String key, final long value) {
            this.parent.setLong(key, value);
        }
        
        @Override
        public void setFloat(final String key, final float value) {
            this.parent.setFloat(key, value);
        }
        
        @Override
        public void setDouble(final String key, final double value) {
            this.parent.setDouble(key, value);
        }
        
        @Override
        public void setBoolean(final String key, final boolean value) {
            this.parent.setBoolean(key, value);
        }
        
        @Override
        public void setString(final String key, final String value) {
            this.parent.setString(key, value);
        }
        
        @Override
        public void setIntArray(final String key, final int[] value) {
            this.parent.setIntArray(key, value);
        }
        
        @Override
        public void setByteArray(final String key, final byte[] value) {
            this.parent.setByteArray(key, value);
        }
        
        @Override
        public WrappedNBTTagCompound getTagCompound(final String key) {
            if (!this.parent.hasKeyOfType(key, NBTTagType.TAG_COMPOUND.getTypeId())) {
                return null;
            }
            final NBTTagCompound other = this.parent.getCompound(key);
            if (other == null) {
                return null;
            }
            return new TagCompoundImpl(other);
        }
        
        @Override
        public WrappedNBTTagList getTagList(final String key, final NBTTagType expectedListElements) {
            if (!this.parent.hasKeyOfType(key, NBTTagType.TAG_LIST.getTypeId())) {
                return null;
            }
            final NBTTagList list = this.parent.getList(key, expectedListElements.getTypeId());
            if (list == null) {
                return null;
            }
            return new TagListImpl(list);
        }
        
        @Override
        public Object getValue(final String key) {
            return NBTProviderImpl.extractValue(this.parent.get(key));
        }
        
        @Override
        public WrappedNBTTagCompound unmodifiable() {
            return new UnmodWrappedNBTTagCompound(this);
        }
    }
}
