package de.hellfirepvp.cmd.cnbt;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.APIWrappedNBTTagCompound;
import de.hellfirepvp.api.data.APIWrappedNBTTagList;
import de.hellfirepvp.api.data.WatchedNBTEditor;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.nbt.BufferingNBTEditor;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCnbtSetraw
 * Created by HellFirePvP
 * Date: 01.06.2016 / 20:24
 */
public class CommandCnbtSetraw extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String mobName = args[1];

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, mobName);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        String value = args[args.length - 1];
        String key = args[args.length - 2];

        List<String> argsList = Arrays.asList(args);

        Deque<NBTEditPathEntry> path;
        try {
            path = NBTParser.parsePath(argsList.subList(2, argsList.size() - 2));
        } catch (LocatableException exc) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + exc.getChatMessage());
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        NBTEditKey nbtEditKey;
        try {
            nbtEditKey = NBTParser.parseEntry(key);
        } catch (LocatableException e) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + e.getChatMessage());
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        ItemStack inHand = null;
        if(nbtEditKey instanceof StackNBTEditKey) {
            inHand = p.getInventory().getItemInHand();
            if(inHand == null || inHand.getType().equals(Material.AIR)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cnbt.setraw.noitem"));
                BaseCommand.sendPlayerDescription(p, this, true);
                return;
            }
        }

        BufferingNBTEditor editor = cmob.createApiAdapter().editNBTTag();
        StringBuilder pathStringBuilder = new StringBuilder();
        Object dNBT = null;
        while (!path.isEmpty()) {
            NBTEditPathEntry entry = path.removeFirst();
            if(dNBT != null) {
                if(dNBT instanceof APIWrappedNBTTagCompound) {
                    if(entry.isTag()) {
                        dNBT = ((APIWrappedNBTTagCompound) dNBT).createOrGetSubTag(entry.getKey());
                        pathStringBuilder.append(entry.getKey()).append(".");
                    } else {
                        NBTTagType type = NBTParser.bruteforceListType((APIWrappedNBTTagCompound) dNBT, entry.getKey());
                        if(type == null) {
                            type = NBTTagType.TAG_COMPOUND; //Dummy
                        }
                        dNBT = ((APIWrappedNBTTagCompound) dNBT).createOrGetSubList(entry.getKey(), type);
                        pathStringBuilder.append(entry.getKey()).append(".");
                    }
                } else if(dNBT instanceof APIWrappedNBTTagList) {
                    NBTTagType listType = ((APIWrappedNBTTagList) dNBT).getElementType();
                    int index = Integer.parseInt(entry.getKey());
                    int size = ((APIWrappedNBTTagList) dNBT).size();
                    if(index < 0 || index >= size) {
                        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                            String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.list.range"),
                                    String.valueOf(index), String.valueOf(size)));
                        BaseCommand.sendPlayerDescription(p, this, true);
                        return;
                    }

                    if(entry.isTag()) {
                        if(listType != null && !listType.equals(NBTTagType.TAG_COMPOUND)) {
                            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                                    String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.contain"), NBTTagType.TAG_COMPOUND));
                            BaseCommand.sendPlayerDescription(p, this, true);
                            return;
                        }
                        dNBT = ((APIWrappedNBTTagList) dNBT).getElementAtIndex(index); //We check-parsed the index and elementtype.
                        pathStringBuilder.append("index(").append(index).append(").");
                    } else {
                        if(listType != null && !listType.equals(NBTTagType.TAG_LIST)) {
                            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                                    String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.contain"), NBTTagType.TAG_LIST));
                            BaseCommand.sendPlayerDescription(p, this, true);
                            return;
                        }
                        dNBT = ((APIWrappedNBTTagList) dNBT).getElementAtIndex(index); //We check-parsed the index and elementtype.
                        pathStringBuilder.append("index(").append(index).append(").");
                    }
                }
            } else {
                if(entry.isTag()) {
                    dNBT = editor.createOrGetSubTag(entry.getKey());
                    pathStringBuilder.append(entry.getKey()).append(".");
                } else {
                    NBTTagType type = NBTParser.bruteforceListType(editor, entry.getKey());
                    if(type == null) {
                        type = NBTTagType.TAG_COMPOUND; //Dummy
                    }
                    dNBT = editor.createOrGetSubList(entry.getKey(), type);
                    pathStringBuilder.append(entry.getKey()).append(".");
                }
            }
        }

        String pathDesc = null;
        if(pathStringBuilder.length() > 0) {
            pathDesc = pathStringBuilder.substring(0, pathStringBuilder.length() - 1);
        }

        boolean remove = value.equalsIgnoreCase("null");

        if(dNBT == null) {
            if(remove) {
                if(editor.hasKey(nbtEditKey.key)) {
                    editor.removeKey(nbtEditKey.key);
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN +
                            String.format(LanguageHandler.translate("command.cnbt.setraw.success.remove"),
                                    pathDesc == null ? "<Root>" : pathDesc, nbtEditKey.key));
                } else {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                            String.format(LanguageHandler.translate("command.cnbt.setraw.failed.remove.element"),
                                    nbtEditKey.key));
                }

                try {
                    editor.saveAndInvalidateTag();
                } catch (Exception ignored) {} //We can ignore it tho..

                return;
            }

            if(nbtEditKey instanceof StackNBTEditKey) {
                editor.setItemStack(nbtEditKey.key, inHand);
            } else {
                if(nbtEditKey.type.equals(NBTTagType.TAG_COMPOUND)) {
                    editor.setSubTag(nbtEditKey.key, NMSReflector.nbtProvider.newTagCompound());
                } else if(nbtEditKey.type.equals(NBTTagType.TAG_LIST)) {
                    editor.setSubList(nbtEditKey.key, NMSReflector.nbtProvider.newTagList());
                } else {
                    NBTEntryParser parser = nbtEditKey.type.getParser();
                    Object val;
                    try {
                        val = parser.parse(value);
                    } catch (ParseException e) {
                        parser.sendParseErrorMessage(p, value);
                        BaseCommand.sendPlayerDescription(p, this, true);
                        return;
                    }
                    cmob.getDataAdapter().saveTagWithPair(nbtEditKey.key, val);
                }
            }
        } else {
            if(dNBT instanceof APIWrappedNBTTagCompound) {
                if(remove) {
                    if(((APIWrappedNBTTagCompound) dNBT).hasKey(nbtEditKey.key)) {
                        ((APIWrappedNBTTagCompound) dNBT).removeKey(nbtEditKey.key);
                        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN +
                                String.format(LanguageHandler.translate("command.cnbt.setraw.success.remove"),
                                        pathDesc == null ? "<Root>" : pathDesc, nbtEditKey.key));
                    } else {
                        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                            String.format(LanguageHandler.translate("command.cnbt.setraw.failed.remove.element"),
                                    nbtEditKey.key));
                    }

                    try {
                        editor.saveAndInvalidateTag();
                    } catch (Exception ignored) {} //We can ignore it tho..

                    return;
                }

                if(nbtEditKey instanceof StackNBTEditKey) {
                    ((APIWrappedNBTTagCompound) dNBT).setItemStack(nbtEditKey.key, inHand);
                } else {
                    if(nbtEditKey.type.equals(NBTTagType.TAG_COMPOUND)) {
                        ((APIWrappedNBTTagCompound) dNBT).setSubTag(nbtEditKey.key, NMSReflector.nbtProvider.newTagCompound());
                    } else if(nbtEditKey.type.equals(NBTTagType.TAG_LIST)) {
                        ((APIWrappedNBTTagCompound) dNBT).setSubList(nbtEditKey.key, NMSReflector.nbtProvider.newTagList());
                    } else {
                        NBTEntryParser parser = nbtEditKey.type.getParser();
                        Object val;
                        try {
                            val = parser.parse(value);
                        } catch (ParseException e) {
                            parser.sendParseErrorMessage(p, value);
                            BaseCommand.sendPlayerDescription(p, this, true);
                            return;
                        }
                        ((APIWrappedNBTTagCompound) dNBT).set(nbtEditKey.key, val);
                    }
                }
            } else if(dNBT instanceof APIWrappedNBTTagList) {
                if(remove) {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                            LanguageHandler.translate("command.cnbt.setraw.failed.remove.list"));
                    return;
                }

                if(nbtEditKey instanceof StackNBTEditKey) {
                    NBTTagType listElements = ((APIWrappedNBTTagList) dNBT).getElementType();
                    if(listElements != null && !listElements.equals(NBTTagType.TAG_COMPOUND)) {
                        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                                String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.accept"), nbtEditKey.type.name()));
                        BaseCommand.sendPlayerDescription(p, this, true);
                        return;
                    }

                    ((APIWrappedNBTTagList) dNBT).appendItemStack(inHand);
                } else {
                    NBTTagType listElements = ((APIWrappedNBTTagList) dNBT).getElementType();
                    if(listElements != null && !listElements.equals(nbtEditKey.type)) {
                        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                                String.format(LanguageHandler.translate("command.cnbt.setraw.parse.path.listtype.accept"), nbtEditKey.type.name()));
                        BaseCommand.sendPlayerDescription(p, this, true);
                        return;
                    }

                    if(nbtEditKey.type.equals(NBTTagType.TAG_COMPOUND)) {
                        ((APIWrappedNBTTagList) dNBT).appendNewTagCompound();
                    } else if(nbtEditKey.type.equals(NBTTagType.TAG_LIST)) {
                        ((APIWrappedNBTTagList) dNBT).appendNewTagList();
                    } else {
                        NBTEntryParser parser = nbtEditKey.type.getParser();
                        Object val;
                        try {
                            val = parser.parse(value);
                        } catch (ParseException e) {
                            parser.sendParseErrorMessage(p, value);
                            BaseCommand.sendPlayerDescription(p, this, true);
                            return;
                        }
                        ((APIWrappedNBTTagList) dNBT).appendNewElement(val);
                    }
                }
            }
        }

        try {
            editor.checkValid();
            cmob.askForSave(editor, p);
            editor.invalidate();
        } catch (Exception ignored) {} //We can ignore it tho..

        if(nbtEditKey instanceof StackNBTEditKey) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN +
                String.format(LanguageHandler.translate("command.cnbt.setraw.success.item"),
                        pathDesc == null ? "<root>" : pathDesc, cmob.getMobFileName()));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN +
                String.format(LanguageHandler.translate("command.cnbt.setraw.success.generic"),
                    pathDesc == null ? "<root>" : pathDesc, cmob.getMobFileName(), value));
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

    public static class NBTEditKey {

        private NBTTagType type;
        protected String key;

        public NBTEditKey(NBTTagType type, String key) {
            this.type = type;
            this.key = key;
        }

    }

    public static class StackNBTEditKey extends NBTEditKey {

        public StackNBTEditKey(String key) {
            super(NBTTagType.TAG_COMPOUND, key);
        }

    }

    public static class NBTEditPathEntry {

        private boolean isTag;
        private String key;

        public NBTEditPathEntry(boolean isTag, String key) {
            this.isTag = isTag;
            this.key = key;
        }

        public boolean isTag() {
            return isTag;
        }

        public String getKey() {
            return key;
        }
    }

    public static class LocatableException extends Exception {

        private String unlocalizedMessage;
        private String[] arguments;

        public LocatableException(String unlocalizedMessage, String... args) {
            this.unlocalizedMessage = unlocalizedMessage;
            this.arguments = args;
        }

        public String getTranslatedMessage() {
            return LanguageHandler.translate(unlocalizedMessage);
        }

        public String getChatMessage() {
            return String.format(getTranslatedMessage(), arguments);
        }

    }

    public static class NBTParser {

        public static Deque<NBTEditPathEntry> parsePath(Collection<String> arguments) throws LocatableException {
            Deque<NBTEditPathEntry> path = new ArrayDeque<>();
            NBTEditPathEntry prev = null;
            for (String pathElement : arguments) {
                String[] spl = pathElement.split(":");
                if(spl.length != 2) throw new LocatableException("command.cnbt.setraw.parse.path.colon", pathElement);
                String type = spl[0];
                String pathKey = spl[1];
                boolean isTag;
                if(type.equalsIgnoreCase("t")) {
                    isTag = true;
                } else if(type.equalsIgnoreCase("tl")) {
                    isTag = false;
                } else {
                    throw new LocatableException("command.cnbt.setraw.parse.path.type", type);
                }
                if(prev != null && !prev.isTag) {
                    try {
                        Integer.parseInt(pathKey);
                    } catch (Exception e) {
                        throw new LocatableException("command.cnbt.setraw.parse.path.list.keyint", pathKey);
                    }
                }
                NBTEditPathEntry entry = new NBTEditPathEntry(isTag, pathKey);
                prev = entry;
                path.addLast(entry);
            }
            return path;
        }

        public static NBTEditKey parseEntry(String specificEntry) throws LocatableException {
            int colon = specificEntry.indexOf(':');
            if(colon == -1) throw new LocatableException("command.cnbt.setraw.parse.path.colon", specificEntry);
            if(colon + 1 >= specificEntry.length()) throw new LocatableException("command.cnbt.setraw.parse.path.nopath", specificEntry);
            //From a technical viewpoint, colon + 1 > length() is sufficient to prevent an error, however we can't do anything with an empty string...

            String type = specificEntry.substring(0, colon);
            String key = specificEntry.substring(colon + 1);

            if(type.equalsIgnoreCase("item")) {
                return new StackNBTEditKey(key);
            }
            NBTTagType nbtType = NBTTagType.getByIdentifier(type);
            if(nbtType == null) {
                throw new LocatableException("command.cnbt.setraw.parse.path.typeunknown",
                        type, buildKnownTypesMessage());
            }
            return new NBTEditKey(nbtType, key);
        }

        public static NBTTagType bruteforceListType(WrappedNBTTagCompound tag, String key) {
            if(!tag.hasKey(key)) return null;

            for (NBTTagType type : NBTTagType.values()) {
                WrappedNBTTagList list = tag.getTagList(key, type);
                if(list == null || list.size() <= 0) continue;
                return list.getElementType();
            }
            return null;
        }

        public static NBTTagType bruteforceListType(WatchedNBTEditor editor, String key) {
            if(!editor.hasKey(key)) return null;

            for (NBTTagType type : NBTTagType.values()) {
                WrappedNBTTagList list = editor.getTagList(key, type);
                if(list == null || list.size() <= 0) continue;
                return list.getElementType();
            }
            return null;
        }

        private static String msgKnownTypes;
        private static String buildKnownTypesMessage() {
            if(msgKnownTypes == null) {
                StringBuilder sb = new StringBuilder();
                for (NBTTagType type : NBTTagType.values()) {
                    if(sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append("'").append(type.getIdentifier())
                            .append("' -> ").append(type.name());
                }
                sb.append(", 'ITEM' -> ItemInHand");
                msgKnownTypes = sb.toString();
            }
            return msgKnownTypes;
        }

    }

}
