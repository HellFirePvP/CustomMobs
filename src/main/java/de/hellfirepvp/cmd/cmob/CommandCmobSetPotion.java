package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobSetPotion
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:07
 */
public class CommandCmobSetPotion extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "setpotion";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 5;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public String getLocalizedSpecialMessage() {
        String potions = StringUtils.connectWithSeperator(Arrays.asList(PotionEffectType.values()), ", ", new StringUtils.ToStringRunnable<Object>() {
            @Override
            public String toString(Object val) {
                return ((PotionEffectType) val).getName();
            }
        });
        return String.format(LanguageHandler.translate("command.potiontypes"), potions);
    }

    @Override
    public int getCustomMobArgumentIndex() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String effectStr = args[2];
        String amplifierStr = args[3];
        String durationStr = args[4];

        int amplifier;
        try {
            amplifier = Integer.parseInt(amplifierStr);
            if(amplifier < 0) amplifier = 0;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, amplifierStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        int duration;
        try {
            if(durationStr.endsWith("s")) {
                duration = Integer.parseInt(durationStr.substring(0, durationStr.length() - 1));
                duration *= 20;
            } else {
                duration = Integer.parseInt(durationStr);
            }
            if(duration < 1) duration = 1;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, durationStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        PotionEffectType type;
        try {
            type = PotionEffectType.getByName(effectStr);
            type.getName(); //null check
        } catch (Exception exc) {
            MessageAssist.msgPotionTypeDoesntExist(p, effectStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        cmob.getEntityAdapter().addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.setpotion.success"), type.getName(), duration, amplifier));
    }
}
