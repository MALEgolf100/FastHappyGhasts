package com.fasthappyghasts.fast_happy_ghasts.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

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
            if (client.player == null || client.world == null) {
                System.out.println("[FastHappyGhasts] No player or world loaded.");
                return;
            }
            if (!config.get("enabled").getAsBoolean()) {
                System.out.println("[FastHappyGhasts] Mod disabled in config.");
                return;
            }
            if (!config.get("allowOnServers").getAsBoolean() && client.getCurrentServerEntry() != null) {
                System.out.println("[FastHappyGhasts] Disallowed on servers and player is on a server.");
                return;
            }

            PlayerEntity player = client.player;
            Entity vehicle = player.getVehicle();

            if (vehicle == null) {
                // Player not riding anything
                return;
            }

            Identifier entityId = Registries.ENTITY_TYPE.getId(vehicle.getType());
            if (entityId == null) {
                System.out.println("[FastHappyGhasts] Vehicle entity ID is null.");
                return;
            }

            System.out.println("[FastHappyGhasts] Riding entity: " + entityId);

            // FIXED: Check against "minecraft" namespace, not mod ID
            if (entityId.getNamespace().equals("minecraft") && entityId.getPath().equals("happy_ghast")) {
                if (client.options.forwardKey.isPressed()) {
                    double speedMultiplier = config.get("speedMultiplier").getAsDouble();

                    Vec3d lookVec = player.getRotationVector().normalize();
                    Vec3d newVelocity = lookVec.multiply(speedMultiplier);

                    vehicle.setVelocity(newVelocity);
                    vehicle.velocityModified = true; // ensure velocity update

                    System.out.println("[FastHappyGhasts] Applied velocity " + newVelocity + " to happy_ghast.");
                } else {
                    System.out.println("[FastHappyGhasts] Forward key not pressed.");
                }
            } else {
                System.out.println("[FastHappyGhasts] Vehicle is not happy_ghast. It is " + entityId);
            }
        });
    }

    private void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String content = new String(Files.readAllBytes(CONFIG_PATH));
                config = GSON.fromJson(content, JsonObject.class);
                System.out.println("[FastHappyGhasts] Config loaded: " + config);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[FastHappyGhasts] Failed to load config, creating default.");
                createDefaultConfig();
            }
        } else {
            System.out.println("[FastHappyGhasts] Config not found, creating default.");
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        config = new JsonObject();
        config.addProperty("enabled", true);
        config.addProperty("allowOnServers", false);
        config.addProperty("speedMultiplier", 1.5);

        saveConfig();
    }

    private void saveConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, GSON.toJson(config).getBytes());
            System.out.println("[FastHappyGhasts] Config saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
