package me.eventually.hikarilib;

import me.eventually.hikarilib.itemstack.PregenerateStacks;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLib extends JavaPlugin {

    @Override
    public void onLoad() {
        getLogger().info("HikariLib is loading...");
        getLogger().info("Loading Modules...");
        PregenerateStacks.pregenStacks();
        getLogger().info("Modules loaded.");
    }

    @Override
    public void onEnable() {
        getLogger().info("HikariLib is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
