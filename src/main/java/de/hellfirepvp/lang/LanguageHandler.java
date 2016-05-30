package de.hellfirepvp.lang;

import de.hellfirepvp.CustomMobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: LanguageHandler
 * Created by HellFirePvP
 * Date: 26.05.2016 / 16:50
 */
public class LanguageHandler {

    private Map<String, String> loadedTranslations = new HashMap<>();

    public void loadLanguageFile() {
        loadedTranslations.clear();
        File defaultLangFile = new File(CustomMobs.instance.getLanguageFileFolder(), "en_US.lang");
        if(!defaultLangFile.exists()) {
            copyDefaultLanguageFile(defaultLangFile);
        }
        readLanguageFile(defaultLangFile);

        String langFile = CustomMobs.instance.getConfigHandler().getLanguageFileConfiguration() + ".lang";
        File requested = new File(CustomMobs.instance.getLanguageFileFolder(), langFile);
        if(!requested.exists()) {
            CustomMobs.logger.info("Configurated languageFile \"" + langFile + "\" could not be found! Using en_US.lang instead!");
        } else {
            readLanguageFile(requested);
        }
        CustomMobs.logger.debug("Successfully loaded " + loadedTranslations.size() + " languagefile entries.");
    }

    private void readLanguageFile(File requested) {
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(requested);
            prop.load(fis);
        } catch (Exception exc) {
            CustomMobs.logger.warning("Could not read language file " + requested.getName());
            CustomMobs.logger.warning("Please check the file's format!");
            exc.printStackTrace();
            return;
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    CustomMobs.logger.warning("Could not close resource InputStream!");
                    e.printStackTrace();
                }
            }
        }
        for (Object key : prop.keySet()) {
            String strKey = (String) key;
            String strValue = prop.getProperty(strKey);
            if(!strValue.isEmpty()) {
                loadedTranslations.put(strKey, strValue);
            }
        }
    }

    private void copyDefaultLanguageFile(File to) {
        CustomMobs.logger.info("Copying default languageFile (en_US.lang)");
        URL resource = CustomMobs.class.getResource("/" + "en_US.lang");
        if(resource == null) {
            CustomMobs.logger.info("Default languageFile could not be found internally! - Not copying");
            return;
        }
        try {
            Files.copy(resource.openStream(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            CustomMobs.logger.info("Copying default languageFile failed!");
            return;
        }
        CustomMobs.logger.info("Copied default languageFile to lang-folder!");
    }

    public static boolean canTranslate(String unlocalizedText) {
        return CustomMobs.instance.getLanguageHandler().hasTranslation(unlocalizedText);
    }

    public static String translate(String unlocalizedText) {
        return CustomMobs.instance.getLanguageHandler().translateToLocal(unlocalizedText);
    }

    public boolean hasTranslation(String unlocalizedText) {
        return loadedTranslations.containsKey(unlocalizedText);
    }

    public String translateToLocal(String unlocalizedText) {
        if(loadedTranslations.containsKey(unlocalizedText)) {
            return loadedTranslations.get(unlocalizedText);
        }
        return unlocalizedText;
    }

}
