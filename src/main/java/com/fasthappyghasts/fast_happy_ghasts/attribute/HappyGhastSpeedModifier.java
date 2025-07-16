package com.fasthappyghasts.fast_happy_ghasts.attribute;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.util.Identifier;

public class HappyGhastSpeedModifier {
    private static final Identifier SPEED_MODIFIER_ID = Identifier.of("fast_happy_ghasts", "ride_speed_modifier");

    static public void add(HappyGhastEntity ghast, double speedMultiplier) {
        EntityAttributeInstance flyingSpeedAttr = ghast.getAttributeInstance(EntityAttributes.FLYING_SPEED);
        if (flyingSpeedAttr != null && !flyingSpeedAttr.hasModifier(SPEED_MODIFIER_ID)) {
            double diff = (speedMultiplier - 1.0) * flyingSpeedAttr.getBaseValue();     // diff maybe +, maybe -
            EntityAttributeModifier modifier = new EntityAttributeModifier(SPEED_MODIFIER_ID, diff, EntityAttributeModifier.Operation.ADD_VALUE);
            flyingSpeedAttr.addTemporaryModifier(modifier);
        }
    }

    static public void remove(HappyGhastEntity ghast) {
        EntityAttributeInstance flyingSpeedAttr = ghast.getAttributeInstance(EntityAttributes.FLYING_SPEED);
        if (flyingSpeedAttr != null && flyingSpeedAttr.hasModifier(SPEED_MODIFIER_ID)) {
            flyingSpeedAttr.removeModifier(SPEED_MODIFIER_ID);
        }
    }
}
