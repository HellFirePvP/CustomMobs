package de.hellfirepvp.util;

import de.hellfirepvp.CustomMobs;
import java.util.logging.Logger;

public class WrappedPrefixLogger
{
    private Logger log;
    private String prefix;
    
    public WrappedPrefixLogger(final Logger log, final String prefix) {
        this.log = log;
        this.prefix = "[" + prefix + "] ";
    }
    
    public void debug(final String msg) {
        if (CustomMobs.instance.getConfigHandler().debug()) {
            this.log.info(this.prefix + msg);
        }
    }
    
    public void info(final String msg) {
        this.log.info(this.prefix + msg);
    }
    
    public void severe(final String msg) {
        this.log.severe(this.prefix + msg);
    }
    
    public void warning(final String msg) {
        this.log.warning(this.prefix + msg);
    }
}
