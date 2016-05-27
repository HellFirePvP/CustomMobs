package de.hellfirepvp.util;

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
