package de.hellfirepvp.data.nbt;

import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryParser
 * Created by HellFirePvP
 * Date: 29.05.2016 / 12:41
 */
public abstract class NBTEntryParser<T> {

    public static final NBTEntryParser<Byte> BYTE_PARSER = new ByteNBTParser();
    public static final NBTEntryParser<Short> SHORT_PARSER = new ShortNBTParser();
    public static final NBTEntryParser<Integer> INT_PARSER = new IntNBTParser();
    public static final NBTEntryParser<Long> LONG_PARSER = new LongNBTParser();
    public static final NBTEntryParser<Float> FLOAT_PARSER = new FloatNBTParser();
    public static final NBTEntryParser<Double> DOUBLE_PARSER = new DoubleNBTParser();
    public static final NBTEntryParser<Boolean> BOOLEAN_PARSER = new BooleanNBTParser();
    public static final NBTEntryParser<String> STRING_PARSER = new StringNBTParser(); //100% redundant. but well.
    public static final NBTEntryParser<byte[]> BYTE_ARRAY_PARSER = new ByteArrayNBTParser();
    public static final NBTEntryParser<int[]> INT_ARRAY_PARSER = new IntArrayNBTParser();

    @Nonnull
    public abstract T parse(String str) throws ParseException;

    public abstract void sendParseErrorMessage(CommandSender cs, String failedParsing);

    private static <E> List<E> parseArray(String suggestedArray, NBTEntryParser<E> elementParser) throws ParseException {
        String[] separated = suggestedArray.split(";");
        if(separated.length == 0) throw new ParseException(suggestedArray, 0); //LUL wtf.
        List<E> toReturn = new ArrayList<>(separated.length);
        for (String element : separated) {
            if(element == null) throw new ParseException(suggestedArray, 0);
            toReturn.add(elementParser.parse(element));
        }
        return toReturn;
    }

    public static class StringNBTParser extends NBTEntryParser<String> {

        @Nonnull
        @Override
        public String parse(String str) throws ParseException {
            return str;
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {}
    }

    public static class ByteArrayNBTParser extends NBTEntryParser<byte[]> {

        @Nonnull
        @Override
        public byte[] parse(String str) throws ParseException {
            List<Byte> elements = NBTEntryParser.parseArray(str, BYTE_PARSER);
            byte[] array = new byte[elements.size()];
            for (int i = 0; i < elements.size(); i++) {
                array[i] = elements.get(i);
            }
            return array;
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.parse.arrayByte"), failedParsing));
        }

    }

    public static class IntArrayNBTParser extends NBTEntryParser<int[]> {

        @Nonnull
        @Override
        public int[] parse(String str) throws ParseException {
            List<Integer> elements = NBTEntryParser.parseArray(str, INT_PARSER);
            int[] array = new int[elements.size()];
            for (int i = 0; i < elements.size(); i++) {
                array[i] = elements.get(i);
            }
            return array;
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.parse.arrayInt"), failedParsing));
        }
    }

    public static class IntNBTParser extends NBTEntryParser<Integer> {

        @Nonnull
        @Override
        public Integer parse(String str) throws ParseException {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException exc) {
                throw new ParseException(str, 0);
            }
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeAIntNumberRanged(cs, failedParsing, String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE));
        }

    }

    public static class BooleanNBTParser extends NBTEntryParser<Boolean> {

        private static final String TRUE = "true", FALSE = "false";

        @Nonnull
        @Override
        public Boolean parse(String str) throws ParseException {
            String s = str.toLowerCase();
            if(s.equals(TRUE)) {
                return true;
            } else if(s.equals(FALSE)) {
                return false;
            }
            throw new ParseException(s, 0);
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeABooleanValue(cs, failedParsing);
        }

    }

    public static class ByteNBTParser extends NBTEntryParser<Byte> {

        @Nonnull
        @Override
        public Byte parse(String str) throws ParseException {
            try {
                return Byte.parseByte(str);
            } catch (Exception exc) {
                throw new ParseException(str, 0);
            }
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeAIntNumberRanged(cs, failedParsing, String.valueOf(Byte.MIN_VALUE), String.valueOf(Byte.MAX_VALUE));
        }

    }

    public static class ShortNBTParser extends NBTEntryParser<Short> {

        @Nonnull
        @Override
        public Short parse(String str) throws ParseException {
            try {
                return Short.parseShort(str);
            } catch (Exception exc) {
                throw new ParseException(str, 0);
            }
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeAIntNumberRanged(cs, failedParsing, String.valueOf(Short.MIN_VALUE), String.valueOf(Short.MAX_VALUE));
        }
    }

    public static class LongNBTParser extends NBTEntryParser<Long> {

        @Nonnull
        @Override
        public Long parse(String str) throws ParseException {
            try {
                return Long.parseLong(str);
            } catch (Exception exc) {
                throw new ParseException(str, 0);
            }
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeAIntNumberRanged(cs, failedParsing, String.valueOf(Long.MIN_VALUE), String.valueOf(Long.MAX_VALUE));
        }

    }

    public static class FloatNBTParser extends NBTEntryParser<Float> {

        @Nonnull
        @Override
        public Float parse(String str) throws ParseException {
            try {
                return Float.parseFloat(str);
            } catch (Exception exc) {
                throw new ParseException(str, 0);
            }
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeAFloatNumberRanged(cs, failedParsing, String.valueOf(Float.MIN_VALUE), String.valueOf(Float.MAX_VALUE));
        }

    }

    public static class DoubleNBTParser extends NBTEntryParser<Double> {

        @Nonnull
        @Override
        public Double parse(String str) throws ParseException {
            try {
                return Double.parseDouble(str);
            } catch (Exception exc) {
                throw new ParseException(str, 0);
            }
        }

        @Override
        public void sendParseErrorMessage(CommandSender cs, String failedParsing) {
            MessageAssist.msgShouldBeAFloatNumberRanged(cs, failedParsing, String.valueOf(Double.MIN_VALUE), String.valueOf(Double.MAX_VALUE));
        }
    }

}
