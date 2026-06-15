/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-07 14:32:43
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:50
 * @FilePath: src/main/java/me/eventually/hikarilib/listener/ChatAsk.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.listener;

import me.eventually.hikarilib.func.ExpiringHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 聊天询问工具类，用于在聊天栏中向玩家提问并等待回复。
 * <p>每个 ask 调用可指定独立的超时时间，超时后回调自动失效。</p>
 *
 * <pre>{@code
 * // 默认 30 秒超时
 * ChatAsk.ask(player, "请输入你的名字：", name -> {
 *     player.sendMessage("你的名字是：" + name);
 * });
 *
 * // 自定义 10 秒超时
 * ChatAsk.ask(player, "请输入验证码：", code -> {
 *     player.sendMessage("验证码是：" + code);
 * }, 10000);
 * }</pre>
 */
public class ChatAsk implements Listener {
    private static final long DEFAULT_TIMEOUT = 30000;
    private static final ConcurrentHashMap<Long, ExpiringHashMap<UUID, Consumer<String>>> callbackMaps = new ConcurrentHashMap<>();

    /**
     * 注册聊天监听器，由 HikariLib 在启用时自动调用。
     *
     * @param plugin 插件实例
     */
    public static void setupListeners(@NotNull JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ChatAsk(), plugin);
    }

    @NotNull
    private static ExpiringHashMap<UUID, Consumer<String>> getMap(long timeoutMillis) {
        return callbackMaps.computeIfAbsent(timeoutMillis, ExpiringHashMap::new);
    }

    @EventHandler
    public void onPlayerChat(@NotNull PlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        for (ExpiringHashMap<UUID, Consumer<String>> map : callbackMaps.values()) {
            if (map.containsKey(uuid)) {
                event.setCancelled(true);
                Consumer<String> callback = map.remove(uuid);
                callback.accept(event.getMessage());
                return;
            }
        }
    }

    /**
     * 向玩家提问，默认 30 秒超时。
     *
     * @param player   目标玩家
     * @param question 发送给玩家的问题
     * @param callback 玩家回复后的回调，参数为玩家发送的消息
     */
    public static void ask(@NotNull Player player, @NotNull String question, @NotNull Consumer<String> callback) {
        ask(player, question, callback, DEFAULT_TIMEOUT);
    }

    /**
     * 向玩家提问，指定超时时间。
     *
     * @param player        目标玩家
     * @param question      发送给玩家的问题
     * @param callback      玩家回复后的回调，参数为玩家发送的消息
     * @param timeoutMillis 超时毫秒数，超时后回调自动失效
     */
    public static void ask(@NotNull Player player, @NotNull String question, @NotNull Consumer<String> callback, long timeoutMillis) {
        UUID playerId = player.getUniqueId();
        getMap(timeoutMillis).put(playerId, callback);
        player.sendMessage(question);
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        for (ExpiringHashMap<UUID, Consumer<String>> map : callbackMaps.values()) {
            map.remove(playerId);
        }
    }
}
