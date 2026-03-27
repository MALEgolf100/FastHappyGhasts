package com.fasthappyghasts.fast_happy_ghasts.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.phys.Vec3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FastHappyGhastsClient implements ClientModInitializer {

    private static final String MOD_ID = "fast_happy_ghasts";
    public static final Path CONFIG_PATH = new File("config/fasthappyghasts.json").toPath();
    public static final Gson GSON = new Gson();
    public static JsonObject config;

    @Override
    public void onInitializeClient() {
        loadConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) {
                log("[FastHappyGhasts] No player or world loaded.");
                return;
            }
            if (!config.get("enabled").getAsBoolean()) {
                log("[FastHappyGhasts] Mod disabled in config.");
                return;
            }
            if (!config.get("allowOnServers").getAsBoolean() && client.getCurrentServer() != null) {
                log("[FastHappyGhasts] Disallowed on servers and player is on a server.");
                return;
            }

            Player player = client.player;
            Entity vehicle = player.getVehicle();

            if (vehicle == null) {
                return;
            }

            net.minecraft.resources.@org.jspecify.annotations.NonNull Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(vehicle.getType());
            if (entityId == null) {
                log("[FastHappyGhasts] Vehicle entity ID is null.");
                return;
            }

            log("[FastHappyGhasts] Riding entity: " + entityId);

            if (entityId.getNamespace().equals("minecraft") && entityId.getPath().equals("happy_ghast")) {
                if (client.options.keyUp.isDown()) {
                    double speedMultiplier = config.get("speedMultiplier").getAsDouble();

                    Vec3 lookVec = player.getLookAngle().normalize();
                    Vec3 newVelocity = lookVec.scale(speedMultiplier);

                    vehicle.setDeltaMovement(newVelocity);
                    vehicle.hurtMarked = true;

                    log("[FastHappyGhasts] Applied velocity " + newVelocity + " to happy_ghast.");
                } else {
                    log("[FastHappyGhasts] Forward key not pressed.");
                }
            } else {
                log("[FastHappyGhasts] Vehicle is not happy_ghast. It is " + entityId);
            }
        });
    }

    private void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String content = new String(Files.readAllBytes(CONFIG_PATH));
                config = GSON.fromJson(content, JsonObject.class);
                log("[FastHappyGhasts] Config loaded: " + config);
            } catch (IOException e) {
                if (isLoggingEnabled()) e.printStackTrace();
                log("[FastHappyGhasts] Failed to load config, creating default.");
                createDefaultConfig();
            }
        } else {
            log("[FastHappyGhasts] Config not found, creating default.");
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        config = new JsonObject();
        config.addProperty("enabled", true);
        config.addProperty("allowOnServers", false);
        config.addProperty("speedMultiplier", 1.5);
        config.addProperty("enableLogging", false);

        saveConfig();
    }

    public static void saveConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, GSON.toJson(config).getBytes());
            log("[FastHappyGhasts] Config saved.");
        } catch (IOException e) {
            if (isLoggingEnabled()) e.printStackTrace();
        }
    }

    private static void log(String message) {
        if (isLoggingEnabled()) {
            System.out.println(message);
        }
    }

    private static boolean isLoggingEnabled() {
        return config != null && config.has("enableLogging") && config.get("enableLogging").getAsBoolean();
    }
}