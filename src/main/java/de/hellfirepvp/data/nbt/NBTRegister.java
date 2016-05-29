package de.hellfirepvp.data.nbt;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import de.hellfirepvp.CustomMobs;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTRegister
 * Created by HellFirePvP
 * Date: 29.05.2016 / 12:40
 */
public class NBTRegister {

    private Map<String, TypeRegister> entryRegister = new HashMap<>();

    private static NBTRegister instance = new NBTRegister();
    private NBTRegister() {}

    public static NBTRegister getRegister() {
        return instance;
    }

    public Collection<String> getEntries(String mobTypeStr) {
        if(mobTypeStr == null) return Collections.emptyList();
        TypeRegister reg = entryRegister.get(mobTypeStr);
        if(reg == null) return Collections.emptyList();
        return reg.registeredTypes.keySet();
    }

    public NBTEntryParser<?> getParserFor(String mobTypeStr, String suggestedEntry) {
        TypeRegister register = entryRegister.get(mobTypeStr);
        if(register == null) return null;
        return register.getParser(suggestedEntry);
    }

    public static void initializeRegistry() {
        NBTRegister reg = instance;

        try {
            ClassPath cp = ClassPath.from(NBTRegister.class.getClassLoader());
            ImmutableSet<ClassPath.ClassInfo> classes = cp.getTopLevelClassesRecursive("de.hellfirepvp.data.nbt.entries");
            for (ClassPath.ClassInfo ci : classes) {
                Class<?> c = ci.load();
                if(c != null && AbstractNBTEntry.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
                    AbstractNBTEntry entry = (AbstractNBTEntry) c.getConstructor(TypeRegister.class).newInstance(new TypeRegister());
                    entry.registerEntries();
                    reg.registerType(entry.getMobTypeName(), entry.getContext());
                    CustomMobs.logger.info("Loaded NBTEntry for " + entry.getMobTypeName());
                }
            }
        } catch (Exception e) {
            CustomMobs.logger.warning("Could not initialize NBTRegistry! Some things might be missing...");
        }
    }

    private void registerType(String mobType, TypeRegister register) {
        entryRegister.put(mobType, register);
    }

    public static class TypeRegister {

        private Map<String, NBTEntryParser<?>> registeredTypes = new HashMap<>();

        protected boolean setParser(String entry, NBTEntryParser<?> parser) {
            if(registeredTypes.containsKey(entry)) return false;
            registeredTypes.put(entry, parser);
            return true;
        }

        public NBTEntryParser<?> getParser(String entry) {
            return registeredTypes.get(entry);
        }

    }

}
