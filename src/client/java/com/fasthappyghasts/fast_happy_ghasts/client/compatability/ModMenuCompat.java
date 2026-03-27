package com.fasthappyghasts.fast_happy_ghasts.client.compatability;

import com.fasthappyghasts.fast_happy_ghasts.client.FastHappyGhastsClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;       // screens (plural) in 26.1
import net.minecraft.network.chat.Component;          // Text → Component in 26.1

import java.util.Map;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Fast Happy Ghasts Settings"));

            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General Settings"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.literal("Enable Fast Happy Ghasts"),
                            FastHappyGhastsClient.config.get("enabled").getAsBoolean())
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Enable or disable the mod."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("enabled", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.literal("Allow On Servers"),
                            FastHappyGhastsClient.config.get("allowOnServers").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Component.literal("Allows use on multiplayer servers."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("allowOnServers", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startFloatField(
                            Component.literal("Speed Multiplier"),
                            (float) FastHappyGhastsClient.config.get("speedMultiplier").getAsDouble())
                    .setDefaultValue(1.5f)
                    .setMin(0.1f)
                    .setMax(10.0f)
                    .setTooltip(Component.literal("Adjust how fast the Happy Ghast moves when pressing forward."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("speedMultiplier", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.literal("Enable Logging"),
                            FastHappyGhastsClient.config.get("enableLogging").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Component.literal("Print debug information to the console."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("enableLogging", newValue))
                    .build()
            );

            builder.setSavingRunnable(FastHappyGhastsClient::saveConfig);

            return builder.build();
        };
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return ModMenuApi.super.getProvidedConfigScreenFactories();
    }
}