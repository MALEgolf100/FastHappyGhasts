package com.fasthappyghasts.fast_happy_ghasts.client.compatability;

import com.fasthappyghasts.fast_happy_ghasts.client.FastHappyGhastsClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Fast Happy Ghasts Settings"));

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General Settings"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Enable Fast Happy Ghasts"),
                            FastHappyGhastsClient.config.get("enabled").getAsBoolean())
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Enable or disable the mod."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("enabled", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Allow On Servers"),
                            FastHappyGhastsClient.config.get("allowOnServers").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Allows use on multiplayer servers."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("allowOnServers", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startFloatField(
                            Text.literal("Speed Multiplier"),
                            (float) FastHappyGhastsClient.config.get("speedMultiplier").getAsDouble())
                    .setDefaultValue(1.5f)
                    .setMin(0.1f)
                    .setMax(10.0f)
                    .setTooltip(Text.literal("Adjust how fast the Happy Ghast moves when pressing forward."))
                    .setSaveConsumer(newValue -> FastHappyGhastsClient.config.addProperty("speedMultiplier", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Enable Logging"),
                            FastHappyGhastsClient.config.get("enableLogging").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Print debug information to the console."))
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
