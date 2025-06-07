package me.eventually.hikarilib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatAsk implements Listener {
    private static final HashMap<UUID, Consumer<String>> callbacks = new HashMap<>();

    public static void setupListeners(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ChatAsk(), plugin);
    }

    @EventHandler
    public void onPlayerChat(@NotNull PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        UUID uuid = player.getUniqueId();
        if (callbacks.containsKey(uuid)) {
            event.setCancelled(true);
            Consumer<String> callback = callbacks.remove(uuid);
            callback.accept(message);
        }
    }

    public static void ask(@NotNull Player player, String question, Consumer<String> callback) {
        UUID playerId = player.getUniqueId();
        callbacks.put(playerId, callback);
        player.sendMessage(question);
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        callbacks.remove(playerId);
    }
}
