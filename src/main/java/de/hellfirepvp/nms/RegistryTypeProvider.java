package de.hellfirepvp.nms;

import javax.annotation.Nullable;

public interface RegistryTypeProvider extends MobTypeProvider
{
    boolean doesMobTypeExist(final String p0);
    
    @Nullable
    String tryTranslateNameToRegistry(final String p0);
    
    @Nullable
    String tryTranslateRegistryNameToName(final String p0);
}
