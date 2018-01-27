package de.hellfirepvp.cmd;

import org.bukkit.command.CommandSender;

public abstract class AbstractCmobCommand
{
    protected String category;
    
    public final AbstractCmobCommand setCategory(final CommandRegistry.CommandCategory category) {
        return this.setCategory(category.getName());
    }
    
    public final AbstractCmobCommand setCategory(final String category) {
        this.category = category;
        return this;
    }
    
    public abstract String getCommandStart();
    
    public abstract boolean hasFixedLength();
    
    public abstract int getFixedArgLength();
    
    public abstract int getMinArgLength();
    
    public int[] getCustomMobArgumentIndex() {
        return new int[0];
    }
    
    public final String getInputDescriptionKey() {
        return "command." + this.category + "." + this.getCommandStart() + ".inputdesc";
    }
    
    public final String getUsageDescriptionKey() {
        return "command." + this.category + "." + this.getCommandStart() + ".usagedesc";
    }
    
    public final String getAdditionalInformationKey() {
        return "command." + this.category + "." + this.getCommandStart() + ".additional";
    }
    
    public String getLocalizedSpecialMessage() {
        return null;
    }
    
    public abstract void execute(final CommandSender p0, final String[] p1);
}
