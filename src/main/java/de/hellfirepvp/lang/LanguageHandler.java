package de.hellfirepvp.lang;

import java.util.Hashtable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.File;
import de.hellfirepvp.CustomMobs;
import java.util.HashMap;
import java.util.Map;

public class LanguageHandler
{
    private Map<String, String> loadedTranslations;
    
    public LanguageHandler() {
        this.loadedTranslations = new HashMap<String, String>();
    }
    
    public void loadLanguageFile() {
        this.loadedTranslations.clear();
        final File defaultLangFile = new File(CustomMobs.instance.getLanguageFileFolder(), "en_US.lang");
        if (defaultLangFile.exists() && !defaultLangFile.delete()) {
            CustomMobs.logger.debug("Could not refresh default language file!");
        }
        this.copyDefaultLanguageFile(defaultLangFile);
        this.readLanguageFile(defaultLangFile);
        final String langFile = CustomMobs.instance.getConfigHandler().getLanguageFileConfiguration() + ".lang";
        final File requested = new File(CustomMobs.instance.getLanguageFileFolder(), langFile);
        if (!requested.exists()) {
            CustomMobs.logger.info("Configurated languageFile \"" + langFile + "\" could not be found! Using en_US.lang instead!");
        }
        else {
            this.readLanguageFile(requested);
        }
        CustomMobs.logger.debug("Successfully loaded " + this.loadedTranslations.size() + " languagefile entries.");
    }
    
    private void readLanguageFile(final File requested) {
        final Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(requested);
            prop.load(fis);
        }
        catch (Exception exc) {
            CustomMobs.logger.warning("Could not read language file " + requested.getName());
            CustomMobs.logger.warning("Please check the file's format!");
            exc.printStackTrace();
            return;
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    CustomMobs.logger.warning("Could not close resource InputStream!");
                    e.printStackTrace();
                }
            }
        }
        for (final Object key : (prop).keySet()) {
            final String strKey = (String)key;
            final String strValue = prop.getProperty(strKey);
            if (!strValue.isEmpty()) {
                this.loadedTranslations.put(strKey, strValue);
            }
        }
    }
    
    private void copyDefaultLanguageFile(final File to) {
        CustomMobs.logger.info("Copying default languageFile (en_US.lang)");
        final URL resource = CustomMobs.class.getResource("/en_US.lang");
        if (resource == null) {
            CustomMobs.logger.info("Default languageFile could not be found internally! - Not copying");
            return;
        }
        try {
            Files.copy(resource.openStream(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            CustomMobs.logger.info("Copying default languageFile failed!");
            return;
        }
        CustomMobs.logger.info("Copied default languageFile to lang-folder!");
    }
    
    public static boolean canTranslate(final String unlocalizedText) {
        return CustomMobs.instance.getLanguageHandler().hasTranslation(unlocalizedText);
    }
    
    public static String translate(final String unlocalizedText) {
        return CustomMobs.instance.getLanguageHandler().translateToLocal(unlocalizedText);
    }
    
    public boolean hasTranslation(final String unlocalizedText) {
        return this.loadedTranslations.containsKey(unlocalizedText);
    }
    
    public String translateToLocal(final String unlocalizedText) {
        if (this.loadedTranslations.containsKey(unlocalizedText)) {
            return this.loadedTranslations.get(unlocalizedText);
        }
        return unlocalizedText;
    }
}
