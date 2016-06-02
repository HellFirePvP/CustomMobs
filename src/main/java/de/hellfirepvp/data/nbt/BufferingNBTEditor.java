package de.hellfirepvp.data.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.APIWrappedNBTTagCompound;
import de.hellfirepvp.api.data.APIWrappedNBTTagList;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.WatchedNBTEditor;
import de.hellfirepvp.api.data.nbt.NullableIndexedElementIterator;
import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: BufferingNBTEditor
 * Created by HellFirePvP
 * Date: 31.05.2016 / 10:40
 */
public class BufferingNBTEditor implements WatchedNBTEditor {

    private boolean valid = true;
    private final CustomMob parentMob;
    private WrappedNBTTagCompound parentTag;
    private final LinkedList<BufferedEditQuery> executedQueries = new LinkedList<>();

    public BufferingNBTEditor(CustomMob parentMob, WrappedNBTTagCompound parentTag) {
        this.parentMob = parentMob;
        this.parentTag = parentTag;
    }

    @Override
    public void saveAndInvalidateTag() {
        checkValid();
        parentMob.askForSave(this);
        invalidate();
    }

    @Override
    public ICustomMob getOwner() {
        return parentMob.createApiAdapter();
    }

    public void executeQueriesOn(WrappedNBTTagCompound tag) {
        this.parentTag = tag;
        for (BufferedEditQuery q : executedQueries) {
            q.execute(tag);
        }
    }

    public void checkValid() {
        if(!valid) throw new IllegalStateException("Tag no longer valid!");
    }

    public void invalidate() {
        this.valid = false;
    }

    @Override
    public void removeKey(String key) {
        checkValid();
        executedQueries.add((root) -> root.removeKey(key));
    }

    @Override
    public boolean hasKey(String key) {
        checkValid();
        return parentTag.hasKey(key);
    }

    @Override
    public Object getValue(String key) {
        Object returned = parentTag.getValue(key);
        if(returned == null) return null;

        if(returned instanceof WrappedNBTTagCompound) {
            return new WatchedSubTag((WrappedNBTTagCompound) returned,
                    new ExecutionPath(this).appendCopy(ExecutionPath.PathQuery.SUB_TAG, key));
        } else if(returned instanceof WrappedNBTTagList) {
            return new WatchedSubList((WrappedNBTTagList) returned,
                    new ExecutionPath(this).appendCopy(ExecutionPath.PathQuery.SUB_LIST, key, ((WrappedNBTTagList) returned).getElementType()));
        }
        return returned;
    }

    @Override
    public void setInt(String key, int value) {
        checkValid();
        executedQueries.add((root) -> root.setInt(key, value));
    }

    @Override
    public void setByte(String key, byte value) {
        checkValid();
        executedQueries.add((root) -> root.setByte(key, value));
    }

    @Override
    public void setShort(String key, short value) {
        checkValid();
        executedQueries.add((root) -> root.setShort(key, value));
    }

    @Override
    public void setLong(String key, long value) {
        checkValid();
        executedQueries.add((root) -> root.setLong(key, value));
    }

    @Override
    public void setFloat(String key, float value) {
        checkValid();
        executedQueries.add((root) -> root.setFloat(key, value));
    }

    @Override
    public void setDouble(String key, double value) {
        checkValid();
        executedQueries.add((root) -> root.setDouble(key, value));
    }

    @Override
    public void setBoolean(String key, boolean value) {
        checkValid();
        executedQueries.add((root) -> root.setBoolean(key, value));
    }

    @Override
    public void setString(String key, String value) {
        checkValid();
        executedQueries.add((root) -> root.setString(key, value));
    }

    @Override
    public void setIntArray(String key, int[] value) {
        checkValid();
        executedQueries.add((root) -> root.setIntArray(key, value));
    }

    @Override
    public void setByteArray(String key, byte[] value) {
        checkValid();
        executedQueries.add((root) -> root.setByteArray(key, value));
    }

    @Override
    public void setSubTag(String key, WrappedNBTTagCompound subTag) {
        checkValid();
        executedQueries.add((root) -> root.setSubTag(key, subTag));
    }

    @Override
    public void setSubList(String key, WrappedNBTTagList subList) {
        checkValid();
        executedQueries.add((root) -> root.setSubList(key, subList));
    }

    @Override
    public void setItemStack(String key, ItemStack stack) {
        checkValid();
        final ItemStack copyStack = stack.clone();
        executedQueries.add((root) -> NMSReflector.nbtProvider.saveStack(copyStack, root));
    }

    @Override
    public APIWrappedNBTTagCompound createOrGetSubTag(String key) {
        if(hasKey(key)) return getTagCompound(key);

        WatchedSubTag tag = new WatchedSubTag(NMSReflector.nbtProvider.newTagCompound(), new ExecutionPath(this).appendCopy(ExecutionPath.PathQuery.SUB_TAG, key));
        executedQueries.add((root) -> root.setSubTag(key, NMSReflector.nbtProvider.newTagCompound()));
        //parentTag.setSubTag(key, NMSReflector.nbtProvider.newTagCompound());
        return tag;
    }

    @Override
    public APIWrappedNBTTagList createOrGetSubList(String key, NBTTagType expectedElementType) {
        if(hasKey(key)) return getTagList(key, expectedElementType);

        WatchedSubList list = new WatchedSubList(NMSReflector.nbtProvider.newTagList(), new ExecutionPath(this).appendCopy(ExecutionPath.PathQuery.SUB_LIST, key, expectedElementType));
        executedQueries.add((root) -> root.setSubList(key, NMSReflector.nbtProvider.newTagList()));
        //parentTag.setSubList(key, NMSReflector.nbtProvider.newTagList());
        return list;
    }

    @Override
    public APIWrappedNBTTagCompound getTagCompound(String key) {
        checkValid();

        WrappedNBTTagCompound subTag = parentTag.getTagCompound(key);
        if(subTag == null) return null;
        return new WatchedSubTag(subTag, new ExecutionPath(this).appendCopy(ExecutionPath.PathQuery.SUB_TAG, key));
    }

    @Override
    public APIWrappedNBTTagList getTagList(String key, NBTTagType expectedListElements) {
        checkValid();
        WrappedNBTTagList subList = parentTag.getTagList(key, expectedListElements);
        if(subList == null) return null;
        return new WatchedSubList(subList, new ExecutionPath(this).appendCopy(ExecutionPath.PathQuery.SUB_LIST, key, expectedListElements));
    }

    static interface BufferedEditQuery {

        public void execute(WrappedNBTTagCompound rootTag);

    }

    static interface ResultingPathQuery {

        //Returns either WrappedNBTTagCompound or WrappedNBTTagList depending on last PathQuery executed.
        public Object execute(WrappedNBTTagCompound rootTag);

    }

    static interface DeepNbtQuery {

        //Either WrappedNBTTagList or WrappedNBTTagCompound
        public void executeOn(Object deepNBTComplex);

    }

    static class PreBufferedEditQuery implements BufferedEditQuery {

        private ResultingPathQuery prepending;
        private DeepNbtQuery deepNbtQuery;

        PreBufferedEditQuery(ResultingPathQuery prepending, DeepNbtQuery deepQuery) {
            this.prepending = prepending;
            this.deepNbtQuery = deepQuery;
        }

        @Override
        public void execute(WrappedNBTTagCompound rootTag) {
            Object pre = prepending.execute(rootTag);
            if(pre == null) {
                CustomMobs.logger.warning("Could not resolve API-NBTQueryPath completely.");
                return;
            }
            deepNbtQuery.executeOn(pre);
        }

    }

    static class ExecutionPath {

        private LinkedList<ExecutionDetails> path = new LinkedList<>();
        private BufferingNBTEditor root;

        ExecutionPath(BufferingNBTEditor root) {
            this.root = root;
        }

        private ExecutionPath appendCopy(PathQuery query, String key, Object... additionalValues) {
            ExecutionPath deeper = new ExecutionPath(root);
            deeper.path = Lists.newLinkedList(path);
            deeper.path.addLast(new ExecutionDetails(query, key, additionalValues));
            return deeper;
        }

        private void appendEdit(DeepNbtQuery deepNbtQuery) {
            root.executedQueries.add(resolveToQuery(deepNbtQuery));
        }

        BufferedEditQuery resolveToQuery(DeepNbtQuery deepNbtQuery) {
            return new PreBufferedEditQuery(buildQueryFromPath(), deepNbtQuery);
        }

        private ResultingPathQuery buildQueryFromPath() {
            return (rootTag) -> {
                Object nbt = root;
                for (ExecutionDetails details : path) {
                    PathQuery query = details.query;
                    switch (query) {
                        case SUB_TAG:
                            if(nbt == root) {
                                Object next = rootTag.getTagCompound(details.key);
                                if(next == null) {
                                    CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                    CustomMobs.logger.warning("Failed at: Root->getSubTag(" + details.key + ")");
                                    CustomMobs.logger.warning("SubTag doesn't exist (anymore?).");
                                    return null;
                                } else {
                                    nbt = next;
                                }
                            } else {
                                if(nbt instanceof WrappedNBTTagCompound) {
                                    Object next = ((WrappedNBTTagCompound) nbt).getTagCompound(details.key);
                                    if(next == null) {
                                        CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                        CustomMobs.logger.warning("Failed at: ?->getSubTag(" + details.key + ")");
                                        CustomMobs.logger.warning("SubTag doesn't exist (anymore?).");
                                        return null;
                                    } else {
                                        nbt = next;
                                    }
                                } else {
                                    CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                    CustomMobs.logger.warning("Failed at: getSubTag on something else than a TagCompound.");
                                    return null;
                                }
                            }
                            break;
                        case SUB_LIST:
                            NBTTagType executedType;
                            try {
                                executedType = (NBTTagType) details.additionalData[0];
                            } catch (Exception exc) {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: getSubList without an expectedType?");
                                return null;
                            }
                            if(executedType == null) {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: getSubList without an expectedType?");
                                return null;
                            }
                            if(nbt == root) {
                                Object list = rootTag.getTagList(details.key, executedType);
                                if(list == null) {
                                    CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                    CustomMobs.logger.warning("Failed at: Root->getSubList(" + details.key + ", " + executedType.name() + ")");
                                    CustomMobs.logger.warning("SubList doesn't exist (anymore?).");
                                    return null;
                                } else {
                                    nbt = list;
                                }
                            } else {
                                if(nbt instanceof WrappedNBTTagCompound) {
                                    Object list = ((WrappedNBTTagCompound) nbt).getTagList(details.key, executedType);
                                    if(list == null) {
                                        CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                        CustomMobs.logger.warning("Failed at: ?->getSubList(" + details.key + ", " + executedType.name() + ")");
                                        CustomMobs.logger.warning("SubList doesn't exist (anymore?).");
                                        return null;
                                    } else {
                                        nbt = list;
                                    }
                                } else {
                                    CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                    CustomMobs.logger.warning("Failed at: getSubList on something else than a TagCompound.");
                                    return null;
                                }
                            }
                            break;
                        case IT_NEXT:
                            int itIndex;
                            try {
                                itIndex = (int) details.additionalData[0];
                            } catch (Exception exc) {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: Iterator.next without an index?");
                                return null;
                            }
                            if(itIndex == -1) {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: Iterator.next with element index '-1'?");
                                return null;
                            }
                            if(nbt instanceof WrappedNBTTagList) {
                                Object element = ((WrappedNBTTagList) nbt).getElementAtIndex(itIndex);
                                if(element == null) {
                                    CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                    CustomMobs.logger.warning("Failed at: Element for Iterator.next on index " + itIndex + " was not found.");
                                    CustomMobs.logger.warning("The element doesn't exist (anymore?).");
                                    return null;
                                } else {
                                    nbt = element;
                                }
                            } else {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: Iterator.next on something else than a TagList-Iterator.");
                                return null;
                            }
                            break;
                        case LIST_INDEX:
                            int index;
                            try {
                                index = (int) details.additionalData[0];
                            } catch (Exception exc) {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: List.getElementAt without an index?");
                                return null;
                            }
                            if(index == -1) {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: List.getElementAt with index '-1'?");
                                return null;
                            }
                            if(nbt instanceof WrappedNBTTagList) {
                                Object element = ((WrappedNBTTagList) nbt).getElementAtIndex(index);
                                if(element == null) {
                                    CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                    CustomMobs.logger.warning("Failed at: Element for List.getElementAtIndex on index " + index + " was not found.");
                                    CustomMobs.logger.warning("The element doesn't exist (anymore?).");
                                    return null;
                                } else {
                                    nbt = element;
                                }
                            } else {
                                CustomMobs.logger.warning("Can't resolve executed NBT actions.");
                                CustomMobs.logger.warning("Failed at: List.getElementAt on something else than a TagList.");
                                return null;
                            }
                            break;
                    }
                }
                return nbt;
            };
        }

        static class ExecutionDetails {

            private final PathQuery query;
            private final String key;
            private final Object[] additionalData;

            ExecutionDetails(PathQuery query, String key, Object[] additionalData) {
                this.query = query;
                this.key = key;
                this.additionalData = additionalData;
            }

            @Override
            public String toString() {
                return "query=" + query.name() + ",key=" + key + ",details=" + new ArrayList<>(Arrays.asList(additionalData)).toString();
            }

        }

        static enum PathQuery {

            SUB_TAG,
            SUB_LIST,

            LIST_INDEX,

            IT_NEXT

        }

    }

    static class WatchedSubList implements APIWrappedNBTTagList {

        private WrappedNBTTagList watchedlist;
        private ExecutionPath pathToRoot;

        WatchedSubList(WrappedNBTTagList watchedlist, ExecutionPath pathToRoot) {
            this.watchedlist = watchedlist;
            this.pathToRoot = pathToRoot;
        }

        @Override
        public Object getRawNMSTagList() {
            throw new UnsupportedOperationException("getRawNMSTagList");
        }

        @Override
        public boolean appendNewElement(Object element) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if (deepNBTComplex instanceof WrappedNBTTagList) {
                    WrappedNBTTagList res = (WrappedNBTTagList) deepNBTComplex;
                    if(!res.appendNewElement(element)) {
                        CustomMobs.logger.warning("appendNewElement failed for element " + (element == null ? "null" : element.toString()));
                    }
                } else {
                    CustomMobs.logger.warning("appendNewElement-call resolved to something else than a TagList. Did we resolve the wrong way?");
                }
            });
            return true;
        }

        @Override
        public boolean appendTagCompound(WrappedNBTTagCompound compound) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if (deepNBTComplex instanceof WrappedNBTTagList) {
                    WrappedNBTTagList res = (WrappedNBTTagList) deepNBTComplex;
                    if(!res.appendTagCompound(compound)) {
                        CustomMobs.logger.warning("appendTagCompound failed for a TagCompound...");
                    }
                } else {
                    CustomMobs.logger.warning("appendTagCompound-call resolved to something else than a TagList. Did we resolve the wrong way?");
                }
            });
            return true;
        }

        @Override
        public boolean appendTagList(WrappedNBTTagList list) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if (deepNBTComplex instanceof WrappedNBTTagList) {
                    WrappedNBTTagList res = (WrappedNBTTagList) deepNBTComplex;
                    if(!res.appendTagList(list)) {
                        CustomMobs.logger.warning("appendTagList failed for a TagList...");
                    }
                } else {
                    CustomMobs.logger.warning("appendTagList-call resolved to something else than a TagList. Did we resolve the wrong way?");
                }
            });
            return true;
        }

        @Override
        public boolean hasElementType() {
            return watchedlist.hasElementType();
        }

        @Override
        public NBTTagType getElementType() {
            return watchedlist.getElementType();
        }

        @Override
        public NullableIndexedElementIterator<Object> getElementIterator(boolean unmodifiable) {
            pathToRoot.root.checkValid();

            NullableIndexedElementIterator<Object> it = watchedlist.getElementIterator(true);
            if(it != null) {
                it = new WatchedListIterator(it, pathToRoot);
            }
            return it;
        }

        @Override
        public NullableIndexedElementIterator<Object> getElementIterator() {
            pathToRoot.root.checkValid();

            NullableIndexedElementIterator<Object> it = watchedlist.getElementIterator(true);
            if(it != null) {
                it = new WatchedListIterator(it, pathToRoot);
            }
            return it;
        }

        @Override
        public Object getElementAtIndex(int index) {
            pathToRoot.root.checkValid();

            Object element = watchedlist.getElementAtIndex(index);
            if(element == null) return null;
            if(element instanceof WrappedNBTTagCompound) {
                return new WatchedSubTag((WrappedNBTTagCompound) element,
                        pathToRoot.appendCopy(ExecutionPath.PathQuery.LIST_INDEX, "", index));
            } else if(element instanceof WrappedNBTTagList) {
                return new WatchedSubList((WrappedNBTTagList) element,
                        pathToRoot.appendCopy(ExecutionPath.PathQuery.LIST_INDEX, "", index));
            }
            return element;
        }

        @Override
        public WrappedNBTTagList unmodifiable() {
            return this;
        }

        @Override
        public int size() {
            pathToRoot.root.checkValid();

            return watchedlist.size();
        }

        @Override
        public Iterator<Object> iterator() {
            pathToRoot.root.checkValid();

            Iterator<Object> it = watchedlist.getElementIterator(true);
            if(it != null) {
                it = new WatchedListIterator(it, pathToRoot);
            }
            return it;
        }

        @Override
        public boolean appendItemStack(ItemStack stack) {
            pathToRoot.root.checkValid();
            if(stack == null) return false;
            if(getElementType() != null && !getElementType().equals(NBTTagType.TAG_COMPOUND)) return false;

            final ItemStack stackCopy = stack.clone();
            pathToRoot.appendEdit(deepNBTComplex -> {
                if (deepNBTComplex instanceof WrappedNBTTagList) {
                    WrappedNBTTagList res = (WrappedNBTTagList) deepNBTComplex;
                    WrappedNBTTagCompound newStackTag = NMSReflector.nbtProvider.newTagCompound();
                    NMSReflector.nbtProvider.saveStack(stackCopy, newStackTag);
                    if(!res.appendTagCompound(newStackTag)) {
                        CustomMobs.logger.warning("appendItemStack failed for a TagList...");
                    }
                } else {
                    CustomMobs.logger.warning("appendItemStack-call resolved to something else than a TagList. Did we resolve the wrong way?");
                }
            });
            return true;
        }

        @Override
        public APIWrappedNBTTagCompound appendNewTagCompound() throws UnsupportedNBTTypeException {
            pathToRoot.root.checkValid();
            if(getElementType() != null && !getElementType().equals(NBTTagType.TAG_COMPOUND)) throw new UnsupportedNBTTypeException();

            APIWrappedNBTTagCompound newCmp = new WatchedSubTag(NMSReflector.nbtProvider.newTagCompound(), pathToRoot.appendCopy(ExecutionPath.PathQuery.LIST_INDEX, "", size()));
            appendTagCompound(NMSReflector.nbtProvider.newTagCompound());
            return newCmp;
        }

        @Override
        public APIWrappedNBTTagList appendNewTagList() throws UnsupportedNBTTypeException {
            pathToRoot.root.checkValid();
            if(getElementType() != null && !getElementType().equals(NBTTagType.TAG_LIST)) throw new UnsupportedNBTTypeException();

            APIWrappedNBTTagList newList = new WatchedSubList(NMSReflector.nbtProvider.newTagList(), pathToRoot.appendCopy(ExecutionPath.PathQuery.LIST_INDEX, "", size()));
            appendTagList(NMSReflector.nbtProvider.newTagList());
            return newList;
        }
    }

    static class WatchedListIterator implements NullableIndexedElementIterator<Object> {

        private Iterator<Object> iterator;
        private ExecutionPath path;

        WatchedListIterator(Iterator<Object> iterator, ExecutionPath path) {
            this.iterator = iterator;
            this.path = path;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Nullable
        @Override
        public Object next() {
            Object nextElement = iterator.next();
            if(nextElement != null) {
                if(nextElement instanceof WrappedNBTTagCompound) {
                    nextElement = new WatchedSubTag((WrappedNBTTagCompound) nextElement,
                            path.appendCopy(ExecutionPath.PathQuery.IT_NEXT, "", getCurrentIndex()));
                } else if(nextElement instanceof WrappedNBTTagList) {
                    nextElement = new WatchedSubList((WrappedNBTTagList) nextElement,
                            path.appendCopy(ExecutionPath.PathQuery.IT_NEXT, "", getCurrentIndex()));
                }
            }
            return nextElement;
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        public int getCurrentIndex() {
            if(iterator instanceof IndexedIterator) {
                return ((IndexedIterator) iterator).getCurrentIndex();
            } else {
                return -1;
            }
        }
    }

    static class WatchedSubTag implements APIWrappedNBTTagCompound {

        private WrappedNBTTagCompound watchedTag;
        private ExecutionPath pathToRoot;

        WatchedSubTag(WrappedNBTTagCompound watchedTag, ExecutionPath pathToRoot) {
            this.watchedTag = watchedTag;
            this.pathToRoot = pathToRoot;
        }

        @Override
        public Object getRawNMSTagCompound() {
            throw new UnsupportedOperationException("getRawNMSTagCompound");
        }

        @Override
        public void removeKey(String key) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if (deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).removeKey(key);
                } else {
                    CustomMobs.logger.warning("removeKey-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public boolean hasKey(String key) {
            pathToRoot.root.checkValid();
            return watchedTag.hasKey(key);
        }

        @Override
        public void set(String key, Object value) throws UnsupportedNBTTypeException {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    try {
                        ((WrappedNBTTagCompound) deepNBTComplex).set(key, value);
                    } catch (UnsupportedNBTTypeException e) {
                        CustomMobs.logger.warning("set-call threw an UnsupportedNBTTypeException for value " + (value == null ? "null" : value.toString()));
                    }
                } else {
                    CustomMobs.logger.warning("set-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setInt(String key, int value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setInt(key, value);
                } else {
                    CustomMobs.logger.warning("setInt-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setByte(String key, byte value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setByte(key, value);
                } else {
                    CustomMobs.logger.warning("setByte-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setShort(String key, short value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setShort(key, value);
                } else {
                    CustomMobs.logger.warning("setShort-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setLong(String key, long value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setLong(key, value);
                } else {
                    CustomMobs.logger.warning("setLong-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setFloat(String key, float value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setFloat(key, value);
                } else {
                    CustomMobs.logger.warning("setFloat-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setDouble(String key, double value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setDouble(key, value);
                } else {
                    CustomMobs.logger.warning("setDouble-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setBoolean(String key, boolean value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setBoolean(key, value);
                } else {
                    CustomMobs.logger.warning("setBoolean-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setString(String key, String value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setString(key, value);
                } else {
                    CustomMobs.logger.warning("setString-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setIntArray(String key, int[] value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setIntArray(key, value);
                } else {
                    CustomMobs.logger.warning("setIntArray-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setByteArray(String key, byte[] value) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setByteArray(key, value);
                } else {
                    CustomMobs.logger.warning("setByteArray-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setSubTag(String key, WrappedNBTTagCompound subTag) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setSubTag(key, subTag);
                } else {
                    CustomMobs.logger.warning("setSubTag-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public void setSubList(String key, WrappedNBTTagList subList) {
            pathToRoot.root.checkValid();

            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    ((WrappedNBTTagCompound) deepNBTComplex).setSubList(key, subList);
                } else {
                    CustomMobs.logger.warning("setSubList-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Nullable
        @Override
        public ItemStack getItemStack(String key) {
            if(!hasKey(key)) return null;
            pathToRoot.root.checkValid();

            return watchedTag.getItemStack(key);
        }

        @Override
        public APIWrappedNBTTagCompound getTagCompound(String key) {
            pathToRoot.root.checkValid();

            WrappedNBTTagCompound subTag = watchedTag.getTagCompound(key);
            if(subTag == null) return null;
            return new WatchedSubTag(subTag, pathToRoot.appendCopy(ExecutionPath.PathQuery.SUB_TAG, key));
        }

        @Override
        public APIWrappedNBTTagList getTagList(String key, NBTTagType expectedListElements) {
            pathToRoot.root.checkValid();
            WrappedNBTTagList subList = watchedTag.getTagList(key, expectedListElements);
            if(subList == null) return null;
            return new WatchedSubList(subList, pathToRoot.appendCopy(ExecutionPath.PathQuery.SUB_LIST, key, expectedListElements));
        }

        @Override
        public Object getValue(String key) {
            Object returned = watchedTag.getValue(key);
            if(returned == null) return null;

            if(returned instanceof WrappedNBTTagCompound) {
                return new WatchedSubTag((WrappedNBTTagCompound) returned,
                        pathToRoot.appendCopy(ExecutionPath.PathQuery.SUB_TAG, key));
            } else if(returned instanceof WrappedNBTTagList) {
                return new WatchedSubList((WrappedNBTTagList) returned,
                        pathToRoot.appendCopy(ExecutionPath.PathQuery.SUB_LIST, key, ((WrappedNBTTagList) returned).getElementType()));
            }
            return returned;
        }

        @Override
        public WrappedNBTTagCompound unmodifiable() {
            return this;
        }

        @Override
        public void setItemStack(String key, ItemStack stack) {
            pathToRoot.root.checkValid();

            final ItemStack copyStack = stack.clone();
            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    WrappedNBTTagCompound cmp = NMSReflector.nbtProvider.newTagCompound();
                    NMSReflector.nbtProvider.saveStack(copyStack, cmp);
                    ((WrappedNBTTagCompound) deepNBTComplex).setSubTag(key, cmp);
                } else {
                    CustomMobs.logger.warning("setItemStack-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
        }

        @Override
        public APIWrappedNBTTagCompound createOrGetSubTag(String key) {
            if(hasKey(key)) {
                APIWrappedNBTTagCompound tag = getTagCompound(key);
                if(tag != null) return tag;
            }

            WatchedSubTag tag = new WatchedSubTag(NMSReflector.nbtProvider.newTagCompound(), pathToRoot.appendCopy(ExecutionPath.PathQuery.SUB_TAG, key));
            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    WrappedNBTTagCompound tl = (WrappedNBTTagCompound) deepNBTComplex;
                    if(tl.getTagCompound(key) == null) {
                        tl.setSubTag(key, NMSReflector.nbtProvider.newTagCompound());
                    }
                } else {
                    CustomMobs.logger.warning("createOrGetSubTag-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
            return tag;
        }

        @Override
        public APIWrappedNBTTagList createOrGetSubList(String key, NBTTagType expectedElementType) {
            if(hasKey(key)) {
                APIWrappedNBTTagList list = getTagList(key, expectedElementType);
                if(list != null) return list;
            }

            WatchedSubList list = new WatchedSubList(NMSReflector.nbtProvider.newTagList(), pathToRoot.appendCopy(ExecutionPath.PathQuery.SUB_LIST, key, expectedElementType));
            pathToRoot.appendEdit(deepNBTComplex -> {
                if(deepNBTComplex instanceof WrappedNBTTagCompound) {
                    WrappedNBTTagCompound tl = (WrappedNBTTagCompound) deepNBTComplex;
                    if(tl.getTagCompound(key) == null) {
                        tl.setSubList(key, NMSReflector.nbtProvider.newTagList());
                    }
                } else {
                    CustomMobs.logger.warning("createOrGetSubList-call resolved to something else than a TagCompound. Did we resolve the wrong way?");
                }
            });
            return list;
        }
    }

}
