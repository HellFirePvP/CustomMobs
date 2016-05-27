package de.hellfirepvp.cmd;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: AbstractCmobCommand
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 3:59
 */
public abstract class AbstractCmobCommand {

    protected String category;

    public final AbstractCmobCommand setCategory(CommandRegistry.CommandCategory category) {
        this.category = category.getName();
        return this;
    }

    public final AbstractCmobCommand setCategory(String category) {
        this.category = category;
        return this;
    }

    public abstract String getCommandStart();

    /**
     * If it contains optional args.
     */
    public abstract boolean hasFixedLength();

    public abstract int getFixedArgLength();

    /**
     * If its length is not fixed, this defines how many args it has to have at least.
     */
    public abstract int getMinArgLength();

    public final String getInputDescriptionKey() {
        return "command." + category + "." + getCommandStart() + ".inputdesc";
    }

    public final String getUsageDescriptionKey() {
        return "command." + category + "." + getCommandStart() + ".usagedesc";
    }

    public final String getAdditionalInformationKey() {
        return "command." + category + "." + getCommandStart() + ".additional";
    }

    public String getLocalizedSpecialMessage() {
        return null;
    }

    public abstract void execute(CommandSender cs, String[] args);

}
