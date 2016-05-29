package de.hellfirepvp.data.nbt.entries.base;

import de.hellfirepvp.data.nbt.AbstractNBTEntry;
import de.hellfirepvp.data.nbt.NBTRegister;

import static de.hellfirepvp.data.nbt.NBTEntryParser.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryLivingEntity
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:33
 */
public abstract class NBTEntryLivingEntity extends AbstractNBTEntry {

    protected NBTEntryLivingEntity(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        //Std. Entity Entries.
        offerEntry("FallDistance", FLOAT_PARSER);
        offerEntry("Fire", SHORT_PARSER);
        offerEntry("Air", SHORT_PARSER);
        offerEntry("OnGround", BOOLEAN_PARSER);
        offerEntry("Invulnerable", BOOLEAN_PARSER);
        offerEntry("PortalCooldown", INT_PARSER);
        offerEntry("CustomName", STRING_PARSER);
        offerEntry("CustomNameVisible", BOOLEAN_PARSER);
        offerEntry("Silent", BOOLEAN_PARSER);
        offerEntry("Glowing", BOOLEAN_PARSER);

        //Std. LivingEntity Entries.
        offerEntry("Health", FLOAT_PARSER);
        offerEntry("AbsorptionAmount", FLOAT_PARSER);
        offerEntry("HurtTime", SHORT_PARSER);
        offerEntry("NoAI", BOOLEAN_PARSER);
        offerEntry("PersistenceRequired", BOOLEAN_PARSER);
        offerEntry("LeftHanded", BOOLEAN_PARSER);
        offerEntry("Team", STRING_PARSER);
    }

}
