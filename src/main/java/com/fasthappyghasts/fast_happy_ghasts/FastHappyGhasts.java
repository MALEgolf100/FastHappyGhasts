package com.fasthappyghasts.fast_happy_ghasts;

import com.fasthappyghasts.fast_happy_ghasts.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FastHappyGhasts implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }
}
