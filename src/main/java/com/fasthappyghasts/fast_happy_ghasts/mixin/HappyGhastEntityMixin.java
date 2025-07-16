package com.fasthappyghasts.fast_happy_ghasts.mixin;

import com.fasthappyghasts.fast_happy_ghasts.attribute.HappyGhastSpeedModifier;
import com.fasthappyghasts.fast_happy_ghasts.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mixin(HappyGhastEntity.class)
public abstract class HappyGhastEntityMixin {
    @Shadow
    public abstract LivingEntity getControllingPassenger();

    private static final Logger LOGGER = LoggerFactory.getLogger("HappyGhastFlight");

    @Inject(method = {"addPassenger*"}, at = {@At("TAIL")})
    private void onAddPassenger(Entity entity, CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.isEnabled())
            return;
        if (entity instanceof PlayerEntity) {
            HappyGhastEntity ghast = (HappyGhastEntity)(Object) this;
            if (getControllingPassenger() == entity) {
                HappyGhastSpeedModifier.add(ghast, config.getSpeedMultiplier());
            }
        }
    }

    @Inject(method = {"removePassenger"}, at = {@At("TAIL")})
    private void onRemovePassenger(Entity entity, CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.isEnabled())
            return;
        if (!(entity instanceof PlayerEntity))
            return;
        HappyGhastEntity ghast = (HappyGhastEntity)(Object) this;
        HappyGhastSpeedModifier.remove(ghast);
    }
}