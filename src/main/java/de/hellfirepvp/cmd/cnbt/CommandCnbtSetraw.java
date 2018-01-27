package de.hellfirepvp.cmd.cnbt;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import java.util.Iterator;
import java.util.ArrayDeque;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.BufferingNBTEditor;
import org.bukkit.inventory.ItemStack;
import java.util.Deque;
import java.util.List;
import de.hellfirepvp.data.mob.CustomMob;
import java.text.ParseException;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.api.data.WatchedNBTEditor;
import de.hellfirepvp.api.data.APIWrappedNBTTagList;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.APIWrappedNBTTagCompound;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import java.util.Collection;
import java.util.Arrays;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCnbtSetraw extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String mobName = args[1];
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, mobName);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final String value = args[args.length - 1];
        final String key = args[args.length - 2];
        final List<String> argsList = Arrays.asList(args);
        Deque<NBTEditPathEntry> path;
        try {
            path = NBTParser.parsePath(argsList.subList(2, argsList.size() - 2));
        }
        catch (LocatableException exc) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + exc.getChatMessage());
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        NBTEditKey nbtEditKey;
        try {
            nbtEditKey = NBTParser.parseEntry(key);
        }
        catch (LocatableException e) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + e.getChatMessage());
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        ItemStack inHand = null;
        if (nbtEditKey instanceof StackNBTEditKey) {
            inHand = p.getInventory().getItemInHand();
            if (inHand == null || inHand.getType().equals((Object)Material.AIR)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cnbt.setraw.noitem"));
                BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                return;
            }
        }
        final BufferingNBTEditor editor = cmob.createApiAdapter().editNBTTag();
        final StringBuilder pathStringBuilder = new StringBuilder();
        Object dNBT = null;
        while (!path.isEmpty()) {
            final NBTEditPathEntry entry = path.removeFirst();
            if (dNBT != null) {
                if (dNBT instanceof APIWrappedNBTTagCompound) {
                    if (entry.isTag()) {
                        dNBT = ((APIWrappedNBTTagCompound)dNBT).createOrGetSubTag(entry.getKey());
                        pathStringBuilder.append(entry.getKey()).append(".");
                    }
                    else {
                        NBTTagType type = NBTParser.bruteforceListType((WrappedNBTTagCompound)dNBT, entry.getKey());
                        if (type == null) {
                            type = NBTTagType.TAG_COMPOUND;
                        }
                        dNBT = ((APIWrappedNBTTagCompound)dNBT).createOrGetSubList(entry.getKey(), type);
                        pathStringBuilder.append(entry.getKey()).append(".");
                    }
                }
                else {
                    if (!(dNBT instanceof APIWrappedNBTTagList)) {
                        continue;
                    }
                    final NBTTagType listType = ((APIWrappedNBTTagList)dNBT).getElementType();
                    final int index = Integer.parseInt(entry.getKey());
                    final int size = ((APIWrappedNBTTagList)dNBT).size();
                    if (index < 0 || index >= size) {
                        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.list.range"), String.valueOf(index), String.valueOf(size)));
                        BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                        return;
                    }
                    if (entry.isTag()) {
                        if (listType != null && !listType.equals(NBTTagType.TAG_COMPOUND)) {
                            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.contain"), NBTTagType.TAG_COMPOUND));
                            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                            return;
                        }
                        dNBT = ((APIWrappedNBTTagList)dNBT).getElementAtIndex(index);
                        pathStringBuilder.append("index(").append(index).append(").");
                    }
                    else {
                        if (listType != null && !listType.equals(NBTTagType.TAG_LIST)) {
                            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.contain"), NBTTagType.TAG_LIST));
                            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                            return;
                        }
                        dNBT = ((APIWrappedNBTTagList)dNBT).getElementAtIndex(index);
                        pathStringBuilder.append("index(").append(index).append(").");
                    }
                }
            }
            else if (entry.isTag()) {
                dNBT = editor.createOrGetSubTag(entry.getKey());
                pathStringBuilder.append(entry.getKey()).append(".");
            }
            else {
                NBTTagType type = NBTParser.bruteforceListType(editor, entry.getKey());
                if (type == null) {
                    type = NBTTagType.TAG_COMPOUND;
                }
                dNBT = editor.createOrGetSubList(entry.getKey(), type);
                pathStringBuilder.append(entry.getKey()).append(".");
            }
        }
        String pathDesc = null;
        if (pathStringBuilder.length() > 0) {
            pathDesc = pathStringBuilder.substring(0, pathStringBuilder.length() - 1);
        }
        final boolean remove = value.equalsIgnoreCase("null");
        if (dNBT == null) {
            if (remove) {
                if (editor.hasKey(nbtEditKey.key)) {
                    editor.removeKey(nbtEditKey.key);
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cnbt.setraw.success.remove"), (pathDesc == null) ? "<Root>" : pathDesc, nbtEditKey.key));
                }
                else {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.failed.remove.element"), nbtEditKey.key));
                }
                try {
                    editor.saveAndInvalidateTag();
                }
                catch (Exception ex) {}
                return;
            }
            if (nbtEditKey instanceof StackNBTEditKey) {
                editor.setItemStack(nbtEditKey.key, inHand);
            }
            else if (nbtEditKey.type.equals(NBTTagType.TAG_COMPOUND)) {
                editor.setSubTag(nbtEditKey.key, NMSReflector.nbtProvider.newTagCompound());
            }
            else if (nbtEditKey.type.equals(NBTTagType.TAG_LIST)) {
                editor.setSubList(nbtEditKey.key, NMSReflector.nbtProvider.newTagList());
            }
            else {
                final NBTEntryParser parser = nbtEditKey.type.getParser();
                Object val;
                try {
                    val = parser.parse(value);
                }
                catch (ParseException e2) {
                    parser.sendParseErrorMessage((CommandSender)p, value);
                    BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                    return;
                }
                cmob.getDataAdapter().saveTagWithPair(nbtEditKey.key, val);
            }
        }
        else if (dNBT instanceof APIWrappedNBTTagCompound) {
            if (remove) {
                if (((APIWrappedNBTTagCompound)dNBT).hasKey(nbtEditKey.key)) {
                    ((APIWrappedNBTTagCompound)dNBT).removeKey(nbtEditKey.key);
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cnbt.setraw.success.remove"), (pathDesc == null) ? "<Root>" : pathDesc, nbtEditKey.key));
                }
                else {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.failed.remove.element"), nbtEditKey.key));
                }
                try {
                    editor.saveAndInvalidateTag();
                }
                catch (Exception ex2) {}
                return;
            }
            if (nbtEditKey instanceof StackNBTEditKey) {
                ((APIWrappedNBTTagCompound)dNBT).setItemStack(nbtEditKey.key, inHand);
            }
            else if (nbtEditKey.type.equals(NBTTagType.TAG_COMPOUND)) {
                ((APIWrappedNBTTagCompound)dNBT).setSubTag(nbtEditKey.key, NMSReflector.nbtProvider.newTagCompound());
            }
            else if (nbtEditKey.type.equals(NBTTagType.TAG_LIST)) {
                ((APIWrappedNBTTagCompound)dNBT).setSubList(nbtEditKey.key, NMSReflector.nbtProvider.newTagList());
            }
            else {
                final NBTEntryParser parser = nbtEditKey.type.getParser();
                Object val;
                try {
                    val = parser.parse(value);
                }
                catch (ParseException e2) {
                    parser.sendParseErrorMessage((CommandSender)p, value);
                    BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                    return;
                }
                ((APIWrappedNBTTagCompound)dNBT).set(nbtEditKey.key, val);
            }
        }
        else if (dNBT instanceof APIWrappedNBTTagList) {
            if (remove) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cnbt.setraw.failed.remove.list"));
                return;
            }
            if (nbtEditKey instanceof StackNBTEditKey) {
                final NBTTagType listElements = ((APIWrappedNBTTagList)dNBT).getElementType();
                if (listElements != null && !listElements.equals(NBTTagType.TAG_COMPOUND)) {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.accept"), nbtEditKey.type.name()));
                    BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                    return;
                }
                ((APIWrappedNBTTagList)dNBT).appendItemStack(inHand);
            }
            else {
                final NBTTagType listElements = ((APIWrappedNBTTagList)dNBT).getElementType();
                if (listElements != null && !listElements.equals(nbtEditKey.type)) {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.accept"), nbtEditKey.type.name()));
                    BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                    return;
                }
                if (nbtEditKey.type.equals(NBTTagType.TAG_COMPOUND)) {
                    ((APIWrappedNBTTagList)dNBT).appendNewTagCompound();
                }
                else if (nbtEditKey.type.equals(NBTTagType.TAG_LIST)) {
                    ((APIWrappedNBTTagList)dNBT).appendNewTagList();
                }
                else {
                    final NBTEntryParser parser2 = nbtEditKey.type.getParser();
                    Object val2;
                    try {
                        val2 = parser2.parse(value);
                    }
                    catch (ParseException e3) {
                        parser2.sendParseErrorMessage((CommandSender)p, value);
                        BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                        return;
                    }
                    ((APIWrappedNBTTagList)dNBT).appendNewElement(val2);
                }
            }
        }
        try {
            editor.checkValid();
            cmob.askForSave(editor, (CommandSender)p);
            editor.invalidate();
        }
        catch (Exception ex3) {}
        if (nbtEditKey instanceof StackNBTEditKey) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cnbt.setraw.success.item"), (pathDesc == null) ? "<root>" : pathDesc, cmob.getMobFileName()));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cnbt.setraw.success.generic"), (pathDesc == null) ? "<root>" : pathDesc, cmob.getMobFileName(), value));
        }
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "setraw";
    }
    
    @Override
    public boolean hasFixedLength() {
        return false;
    }
    
    @Override
    public int getFixedArgLength() {
        return 0;
    }
    
    @Override
    public int getMinArgLength() {
        return 4;
    }
    
    public static class NBTEditKey
    {
        private NBTTagType type;
        protected String key;
        
        public NBTEditKey(final NBTTagType type, final String key) {
            this.type = type;
            this.key = key;
        }
    }
    
    public static class StackNBTEditKey extends NBTEditKey
    {
        public StackNBTEditKey(final String key) {
            super(NBTTagType.TAG_COMPOUND, key);
        }
    }
    
    public static class NBTEditPathEntry
    {
        private boolean isTag;
        private String key;
        
        public NBTEditPathEntry(final boolean isTag, final String key) {
            this.isTag = isTag;
            this.key = key;
        }
        
        public boolean isTag() {
            return this.isTag;
        }
        
        public String getKey() {
            return this.key;
        }
    }
    
    public static class LocatableException extends Exception
    {
        private String unlocalizedMessage;
        private String[] arguments;
        
        public LocatableException(final String unlocalizedMessage, final String... args) {
            this.unlocalizedMessage = unlocalizedMessage;
            this.arguments = args;
        }
        
        public String getTranslatedMessage() {
            return LanguageHandler.translate(this.unlocalizedMessage);
        }
        
        public String getChatMessage() {
            return String.format(this.getTranslatedMessage(), (Object[])this.arguments);
        }
    }
    
    public static class NBTParser
    {
        private static String msgKnownTypes;
        
        public static Deque<NBTEditPathEntry> parsePath(final Collection<String> arguments) throws LocatableException {
            final Deque<NBTEditPathEntry> path = new ArrayDeque<NBTEditPathEntry>();
            NBTEditPathEntry prev = null;
            for (final String pathElement : arguments) {
                final String[] spl = pathElement.split(":");
                if (spl.length != 2) {
                    throw new LocatableException("command.cnbt.setraw.parse.path.colon", new String[] { pathElement });
                }
                final String type = spl[0];
                final String pathKey = spl[1];
                boolean isTag;
                if (type.equalsIgnoreCase("t")) {
                    isTag = true;
                }
                else {
                    if (!type.equalsIgnoreCase("tl")) {
                        throw new LocatableException("command.cnbt.setraw.parse.path.type", new String[] { type });
                    }
                    isTag = false;
                }
                if (prev != null && !prev.isTag) {
                    try {
                        Integer.parseInt(pathKey);
                    }
                    catch (Exception e) {
                        throw new LocatableException("command.cnbt.setraw.parse.path.list.keyint", new String[] { pathKey });
                    }
                }
                final NBTEditPathEntry entry = prev = new NBTEditPathEntry(isTag, pathKey);
                path.addLast(entry);
            }
            return path;
        }
        
        public static NBTEditKey parseEntry(final String specificEntry) throws LocatableException {
            final int colon = specificEntry.indexOf(58);
            if (colon == -1) {
                throw new LocatableException("command.cnbt.setraw.parse.path.colon", new String[] { specificEntry });
            }
            if (colon + 1 >= specificEntry.length()) {
                throw new LocatableException("command.cnbt.setraw.parse.path.nopath", new String[] { specificEntry });
            }
            final String type = specificEntry.substring(0, colon);
            final String key = specificEntry.substring(colon + 1);
            if (type.equalsIgnoreCase("item")) {
                return new StackNBTEditKey(key);
            }
            final NBTTagType nbtType = NBTTagType.getByIdentifier(type);
            if (nbtType == null) {
                throw new LocatableException("command.cnbt.setraw.parse.path.typeunknown", new String[] { type, buildKnownTypesMessage() });
            }
            return new NBTEditKey(nbtType, key);
        }
        
        public static NBTTagType bruteforceListType(final WrappedNBTTagCompound tag, final String key) {
            if (!tag.hasKey(key)) {
                return null;
            }
            for (final NBTTagType type : NBTTagType.values()) {
                final WrappedNBTTagList list = tag.getTagList(key, type);
                if (list != null && list.size() > 0) {
                    return list.getElementType();
                }
            }
            return null;
        }
        
        public static NBTTagType bruteforceListType(final WatchedNBTEditor editor, final String key) {
            if (!editor.hasKey(key)) {
                return null;
            }
            for (final NBTTagType type : NBTTagType.values()) {
                final WrappedNBTTagList list = editor.getTagList(key, type);
                if (list != null && list.size() > 0) {
                    return list.getElementType();
                }
            }
            return null;
        }
        
        private static String buildKnownTypesMessage() {
            if (NBTParser.msgKnownTypes == null) {
                final StringBuilder sb = new StringBuilder();
                for (final NBTTagType type : NBTTagType.values()) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append("'").append(type.getIdentifier()).append("' -> ").append(type.name());
                }
                sb.append(", 'ITEM' -> ItemInHand");
                NBTParser.msgKnownTypes = sb.toString();
            }
            return NBTParser.msgKnownTypes;
        }
    }
}
