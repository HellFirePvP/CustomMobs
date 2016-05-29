package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.EntityAdapter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobResetPotion
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:07
 */
public class CommandCmobResetPotion extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "resetpotion";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 3;
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
        EntityAdapter adapter = cmob.getEntityAdapter();
        Collection<PotionEffect> potions = adapter.getAcivePotionEffects();
        boolean found = false;
        for (PotionEffect pe : potions) {
            if(pe.getType().equals(type)) {
                found = true;
                break;
            }
        }
        if(!found) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.resetpotion.noeffect"), name, effectStr));
            return;
        }
        adapter.removePotionEffect(type);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.resetpotion.success"), type.getName(), name));
    }
}
