package com.fasthappyghasts.fast_happy_ghasts.config;

import lombok.Getter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Getter
@Config(name = "fast_happy_ghasts")
public class ModConfig implements ConfigData {
    boolean enabled = true;
    double speedMultiplier = 1.5;
}
