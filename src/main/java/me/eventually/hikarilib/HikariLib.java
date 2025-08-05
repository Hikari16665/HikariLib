package me.eventually.hikarilib;

import lombok.Getter;
import me.eventually.hikarilib.inventory.HikariMenu;
import me.eventually.hikarilib.listener.ChatAsk;
import me.eventually.hikarilib.tasks.HikariScheduler;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLib extends JavaPlugin {
    public static @Getter HikariLib instance;

    @Override
    public void onLoad() {
        instance = this;
        Banner.printBanner(getLogger());
        getLogger().info("HikariLib is loading...");
        getLogger().info("Loading Modules (Part 1/2)...");
    }

    @Override
    public void onEnable() {
        getLogger().info("HikariLib is enabling...");
        getLogger().info("Loading Modules (Part 2/2)...");
        getLogger().info("Starting Schedulers...");
        HikariScheduler.init();
        getLogger().info("Setting up listeners...");
        HikariMenu.setupListeners(this);
        ChatAsk.setupListeners(this);
        getLogger().info("Modules loaded Part 2.");
        getLogger().info("HikariLib is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("HikariLib is disabling...");
        getLogger().info("Shutting down Schedulers...");
        HikariScheduler.shutdown();
    }
}
