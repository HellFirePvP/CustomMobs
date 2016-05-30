package de.hellfirepvp.util;

import de.hellfirepvp.CustomMobs;

import java.util.logging.Logger;

/**
 * HellFirePvP@Admin
 * Date: 08.06.2015 / 16:10
 * on CustomBosses
 * WrappedPrefixLogger
 */
public class WrappedPrefixLogger {

    private Logger log;
    private String prefix;

    public WrappedPrefixLogger(Logger log, String prefix) {
        this.log = log;
        this.prefix = "[" + prefix + "] ";
    }

    public void debug(String msg) {
        if(CustomMobs.instance.getConfigHandler().debug()) {
            log.info(prefix + msg);
        }
    }

    public void info(String msg) {
        log.info(prefix + msg);
    }

    public void severe(String msg) {
        log.severe(prefix + msg);
    }

    public void warning(String msg) {
        log.warning(prefix + msg);
    }

}
