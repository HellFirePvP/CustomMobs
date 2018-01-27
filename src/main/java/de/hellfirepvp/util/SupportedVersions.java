package de.hellfirepvp.util;

import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.event.v1_9_R1.AmbigousEventListener;
import org.bukkit.event.Listener;

public enum SupportedVersions
{
    V1_9_R1("v1_9_R1"), 
    V1_9_R2("v1_9_R2"), 
    V1_10_R1("v1_10_R1"), 
    V1_11_R1("v1_11_R1"),
    V1_12_R1("v1_12_R1");
    
    private final String versionStr;
    
    private SupportedVersions(final String versionStr) {
        this.versionStr = versionStr;
    }
    
    public String getVersionStr() {
        return this.versionStr;
    }
    
    public Listener getAmbiguousEventListener() {
        switch (this) {
            case V1_9_R1: {
                return new AmbigousEventListener();
            }
            case V1_9_R2: {
                return new de.hellfirepvp.event.v1_9_R2.AmbigousEventListener();
            }
            case V1_10_R1: {
                return new de.hellfirepvp.event.v1_10_R1.AmbigousEventListener();
            }
            case V1_11_R1: {
                return new de.hellfirepvp.event.v1_11_R1.AmbigousEventListener();
            }
            case V1_12_R1: {
                return new de.hellfirepvp.event.v1_11_R1.AmbigousEventListener();
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean isThisAMoreRecentOrEqualVersionThan(final SupportedVersions other) {
        return this.ordinal() >= other.ordinal();
    }
    
    public boolean isThisAMoreRecentVersionThan(final SupportedVersions other) {
        return this.ordinal() > other.ordinal();
    }
    
    public static SupportedVersions getCurrentVersion() {
        final String v = NMSReflector.VERSION;
        for (final SupportedVersions version : values()) {
            if (version != null) {
                if (version.versionStr.equals(v)) {
                    return version;
                }
            }
        }
        return null;
    }
    
    public static String getSupportedVersions() {
        final StringBuilder sb = new StringBuilder();
        final SupportedVersions[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final SupportedVersions sv = values[i];
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(sv.versionStr);
        }
        return sb.toString();
    }
}
