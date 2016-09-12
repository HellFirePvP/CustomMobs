package de.hellfirepvp.util;

import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.event.Listener;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EventListener
 * Created by HellFirePvP-
 * Date: 26.06.2016 / 20:39
 */
public enum SupportedVersions {

    /*V1_8_R1("v1_8_R1"),
    V1_8_R2("v1_8_R2"),
    V1_8_R3("v1_8_R3");*/

    V1_9_R1("v1_9_R1"),
    V1_9_R2("v1_9_R2"),
    V1_10_R1("v1_10_R1");

    private final String versionStr;

    private SupportedVersions(String versionStr) {
        this.versionStr = versionStr;
    }

    public String getVersionStr() {
        return versionStr;
    }

    public Listener getAmbiguousEventListener() {
        switch (this) {
            /*case V1_8_R1:
            case V1_8_R2:
            case V1_8_R3:
                return new AmbigousEventListenerNoOp();*/
            case V1_9_R1:
                return new de.hellfirepvp.event.v1_9_R1.AmbigousEventListener();
            case V1_9_R2:
                return new de.hellfirepvp.event.v1_9_R2.AmbigousEventListener();
            case V1_10_R1:
                return new de.hellfirepvp.event.v1_10_R1.AmbigousEventListener();
            default:
                break;
        }
        return null;
    }

    public static SupportedVersions getCurrentVersion() {
        String v = NMSReflector.VERSION;
        for (SupportedVersions version : values()) {
            if(version == null) continue;
            if(version.versionStr.equals(v)) return version;
        }
        return null;
    }

    public static String getSupportedVersions() {
        StringBuilder sb = new StringBuilder();
        SupportedVersions[] values = values();
        for (int i = 0; i < values.length; i++) {
            SupportedVersions sv = values[i];
            if(i != 0)
                sb.append(", ");
            sb.append(sv.versionStr);
        }
        return sb.toString();
    }

}
