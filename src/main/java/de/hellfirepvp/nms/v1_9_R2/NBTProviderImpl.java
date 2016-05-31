package de.hellfirepvp.nms.v1_9_R2;

import de.hellfirepvp.api.data.nbt.NullableIndexedElementIterator;
import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.data.nbt.IndexedIterator;
import de.hellfirepvp.data.nbt.base.NBTProvider;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.data.nbt.base.UnmodWrappedNBTTagCompound;
import de.hellfirepvp.data.nbt.base.UnmodWrappedNBTTagList;
import de.hellfirepvp.nms.NMSReflector;
import net.minecraft.server.v1_9_R2.NBTBase;
import net.minecraft.server.v1_9_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_9_R2.NBTTagByte;
import net.minecraft.server.v1_9_R2.NBTTagByteArray;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagDouble;
import net.minecraft.server.v1_9_R2.NBTTagEnd;
import net.minecraft.server.v1_9_R2.NBTTagFloat;
import net.minecraft.server.v1_9_R2.NBTTagInt;
import net.minecraft.server.v1_9_R2.NBTTagIntArray;
import net.minecraft.server.v1_9_R2.NBTTagList;
import net.minecraft.server.v1_9_R2.NBTTagLong;
import net.minecraft.server.v1_9_R2.NBTTagShort;
import net.minecraft.server.v1_9_R2.NBTTagString;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTProviderImpl
 * Created by HellFirePvP
 * Date: 24.05.2016 / 13:11
 */
public class NBTProviderImpl implements NBTProvider {

    @Override
    public void saveStack(ItemStack stack, WrappedNBTTagCompound target) {
        if(stack == null || target == null) return;
        CraftItemStack.asNMSCopy(stack).save((NBTTagCompound) target.getRawNMSTagCompound());
    }

    @Override
    public ItemStack loadStack(WrappedNBTTagCompound savedStack) {
        if(savedStack == null) return null;
        return CraftItemStack.asBukkitCopy(
                net.minecraft.server.v1_9_R2.ItemStack.createStack(
                        (NBTTagCompound) savedStack.getRawNMSTagCompound()));
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
    public void write(OutputStream stream, WrappedNBTTagCompound tag) throws IOException {
        NBTCompressedStreamTools.a((NBTTagCompound) tag.getRawNMSTagCompound(), stream);
    }

    @Override
    public WrappedNBTTagCompound read(InputStream stream) throws IOException {
        return new TagCompoundImpl(NBTCompressedStreamTools.a(stream));
    }

    public static class TagListImpl implements WrappedNBTTagList {

        private NBTTagList parentList;

        public TagListImpl(NBTTagList parentList) {
            this.parentList = parentList;
        }

        @Override
        public Object getRawNMSTagList() {
            return parentList;
        }

        @Override
        public boolean appendItemStack(ItemStack stack) {
            if(stack == null) return false;
            if(hasType() && NBTTagType.TAG_COMPOUND != getElementType()) return false;

            WrappedNBTTagCompound cmp = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(stack, cmp);
            parentList.add((NBTBase) cmp.getRawNMSTagCompound());
            return true;
        }

        @Override
        public boolean appendNewElement(Object element) {
            NBTBase wrapped = wrapValue(element);
            if(wrapped == null) return false;
            NBTTagType wrappedType = NBTTagType.getById((int) wrapped.getTypeId());
            if(hasType() && wrappedType != getElementType()) return false;
            parentList.add(wrapped);
            return true;
        }

        @Override
        public boolean appendTagCompound(WrappedNBTTagCompound compound) {
            if(hasType() && getElementType() != NBTTagType.TAG_COMPOUND) return false;
            parentList.add((NBTBase) compound.getRawNMSTagCompound());
            return true;
        }

        @Override
        public boolean appendTagList(WrappedNBTTagList list) {
            if(hasType() && getElementType() != NBTTagType.TAG_LIST) return false;
            parentList.add((NBTBase) list.getRawNMSTagList());
            return true;
        }

        private boolean hasType() {
            return parentList.d() != 0;
        }

        @Override
        public NBTTagType getElementType() {
            return NBTTagType.getById(parentList.d()); //.d() ^= return type;
        }

        @Override
        public NullableIndexedElementIterator<Object> getElementIterator(boolean unmodifiable) {
            return new ForIntIterator(parentList, unmodifiable);
        }

        @Override
        public NullableIndexedElementIterator<Object> getElementIterator() {
            return getElementIterator(false);
        }

        @Override
        public Object getElementAtIndex(int index) {
            NBTBase element = parentList.h(index);

            Object value = extractValue(element);
            if(value != null) return value;
            if(element instanceof NBTTagEnd) return null;
            if(element instanceof NBTTagCompound) {
                return new TagCompoundImpl((NBTTagCompound) element);
            } else if(element instanceof NBTTagList) {
                return new TagListImpl((NBTTagList) element);
            }
            return element; //Awkward huh...
        }

        @Override
        public WrappedNBTTagList unmodifiable() {
            return new UnmodWrappedNBTTagList(this);
        }

        @Override
        public int size() {
            return parentList.size();
        }

        @Override
        public Iterator<Object> iterator() {
            return getElementIterator();
        }
    }

    public static class ForIntIterator implements NullableIndexedElementIterator<Object> {

        private NBTTagList list;
        private int entryPointer;
        private boolean unmodifiable, removeCalledThisIteration = false;

        public ForIntIterator(NBTTagList list, boolean unmodifiable) {
            this.list = list;
            this.entryPointer = -1;
            this.unmodifiable = unmodifiable;
        }

        @Override
        public boolean hasNext() {
            return (entryPointer + 1) < list.size();
        }

        @Nullable
        @Override
        public Object next() {
            entryPointer++;
            removeCalledThisIteration = false;
            NBTBase element = list.h(entryPointer);
            Object value = extractValue(element);
            if(value != null) return value;

            if(element instanceof NBTTagCompound) {
                WrappedNBTTagCompound cmp = new TagCompoundImpl((NBTTagCompound) element);
                if(unmodifiable) cmp = cmp.unmodifiable();
                return cmp;
            } else if(element instanceof NBTTagList) {
                WrappedNBTTagList list = new TagListImpl((NBTTagList) element);
                if(unmodifiable) list = list.unmodifiable();
                return list;
            }

            return element; //Awkward huh...
        }

        @Override
        public void remove() {
            if(unmodifiable) throw new UnsupportedOperationException("remove (unmodifiable NBTIterator)");
            if(removeCalledThisIteration) throw new IllegalStateException("remove already called for this element");

            list.remove(entryPointer);

            entryPointer--;
            removeCalledThisIteration = true;
        }

        @Override
        public int getCurrentIndex() {
            return entryPointer;
        }
    }

    public static class TagCompoundImpl implements WrappedNBTTagCompound {

        private NBTTagCompound parent;

        private TagCompoundImpl(NBTTagCompound compound) {
            this.parent = compound;
        }

        @Override
        public Object getRawNMSTagCompound() {
            return parent;
        }

        @Override
        public void removeKey(String key) {
            parent.remove(key);
        }

        @Override
        public boolean hasKey(String key) {
            return parent.hasKey(key);
        }

        @Override
        public void setSubTag(String key, WrappedNBTTagCompound subTag) {
            parent.set(key, (NBTTagCompound) subTag.getRawNMSTagCompound());
        }

        @Override
        public void setSubList(String key, WrappedNBTTagList listTag) {
            parent.set(key, (NBTTagList) listTag.getRawNMSTagList());
        }

        @Nullable
        @Override
        public ItemStack getItemStack(String key) {
            if(!hasKey(key)) return null;

            return NMSReflector.nbtProvider.loadStack(getTagCompound(key));
        }

        @Override
        public void setItemStack(String key, ItemStack stack) {
            if(stack == null) return;
            WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(stack, tag);
            parent.set(key, (NBTBase) tag.getRawNMSTagCompound());
        }

        //Can't we do better...?
        @Override
        public void set(String key, Object value) throws UnsupportedNBTTypeException {
            if(value instanceof Integer) {
                parent.setInt(key, (Integer) value);
                return;
            } else if(value instanceof Boolean) {
                parent.setBoolean(key, (Boolean) value);
                return;
            } else if(value instanceof Byte) {
                parent.setByte(key, (Byte) value);
                return;
            } else if(value instanceof Short) {
                parent.setShort(key, (Short) value);
                return;
            } else if(value instanceof Long) {
                parent.setLong(key, (Long) value);
                return;
            } else if(value instanceof Float) {
                parent.setFloat(key, (Float) value);
                return;
            } else if(value instanceof Double) {
                parent.setDouble(key, (Double) value);
                return;
            } else if(value instanceof String) {
                parent.setString(key, (String) value);
                return;
            } else if(value instanceof byte[]) {
                parent.setByteArray(key, (byte[]) value);
                return;
            } else if(value instanceof int[]) {
                parent.setIntArray(key, (int[]) value);
                return;
            }
            throw new UnsupportedNBTTypeException();
        }

        @Override
        public void setInt(String key, int value) {
            parent.setInt(key, value);
        }

        @Override
        public void setByte(String key, byte value) {
            parent.setByte(key, value);
        }

        @Override
        public void setShort(String key, short value) {
            parent.setShort(key, value);
        }

        @Override
        public void setLong(String key, long value) {
            parent.setLong(key, value);
        }

        @Override
        public void setFloat(String key, float value) {
            parent.setFloat(key, value);
        }

        @Override
        public void setDouble(String key, double value) {
            parent.setDouble(key, value);
        }

        @Override
        public void setBoolean(String key, boolean value) {
            parent.setBoolean(key, value);
        }

        @Override
        public void setString(String key, String value) {
            parent.setString(key, value);
        }

        @Override
        public void setIntArray(String key, int[] value) {
            parent.setIntArray(key, value);
        }

        @Override
        public void setByteArray(String key, byte[] value) {
            parent.setByteArray(key, value);
        }

        @Override
        public WrappedNBTTagCompound getTagCompound(String key) {
            NBTTagCompound other = parent.getCompound(key);
            if(other == null) return null;
            return new TagCompoundImpl(other);
        }

        @Override
        public WrappedNBTTagList getTagList(String key, NBTTagType expectedListElements) {
            NBTTagList list = parent.getList(key, expectedListElements.getTypeId());
            if(list == null) return null;
            return new TagListImpl(list);
        }

        @Override
        public Object getValue(String key) {
            return extractValue(parent.get(key));
        }

        @Override
        public WrappedNBTTagCompound unmodifiable() {
            return new UnmodWrappedNBTTagCompound(this);
        }
    }

    protected static NBTBase wrapValue(Object value) {
        if(value == null) return null;
        if(value instanceof Integer) {
            return new NBTTagInt((Integer) value);
        } else if(value instanceof Boolean) {
            return new NBTTagByte(((Boolean) value) ? (byte) 1 : (byte) 0);
        } else if(value instanceof Byte) {
            return new NBTTagByte((Byte) value);
        } else if(value instanceof Short) {
            return new NBTTagShort((Short) value);
        } else if(value instanceof Long) {
            return new NBTTagLong((Long) value);
        } else if(value instanceof Float) {
            return new NBTTagFloat((Float) value);
        } else if(value instanceof Double) {
            return new NBTTagDouble((Double) value);
        } else if(value instanceof String) {
            return new NBTTagString((String) value);
        } else if(value instanceof byte[]) {
            return new NBTTagByteArray((byte[]) value);
        } else if(value instanceof int[]) {
            return new NBTTagIntArray((int[]) value);
        }
        return null;
    }

    protected static Object extractValue(NBTBase nbtBase) {
        if(nbtBase == null) return null;
        switch (nbtBase.getTypeId()) {
            case 1:
                return ((NBTTagByte) nbtBase).f();
            case 2:
                return ((NBTTagShort) nbtBase).e();
            case 3:
                return ((NBTTagInt) nbtBase).d();
            case 4:
                return ((NBTTagLong) nbtBase).c();
            case 5:
                return ((NBTTagFloat) nbtBase).h();
            case 6:
                return ((NBTTagDouble) nbtBase).g();
            case 7:
                return ((NBTTagByteArray) nbtBase).c();
            case 8:
                return ((NBTTagString) nbtBase).a_();
            case 11:
                return ((NBTTagIntArray) nbtBase).c();
        }
        return null;
    }

}
