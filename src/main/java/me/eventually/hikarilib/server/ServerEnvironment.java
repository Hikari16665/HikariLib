/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:35:13
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/server/ServerEnvironment.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.eventually.hikarilib.HikariLib;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 服务器环境检测工具类。
 *
 * <pre>{@code
 * if (ServerEnvironment.isFolia()) {
 *     // Folia 特有的逻辑
 * }
 * String version = ServerEnvironment.getMinecraftVersion();
 * }</pre>
 */
public class ServerEnvironment {
    private static Server server = null;

    private ServerEnvironment() {}

    /**
     * 检测当前服务端是否为 Folia。
     *
     * @return true 表示 Folia 或 Folia fork
     */
    public static boolean isFolia() {
        boolean isFolia;
        try {
            // this class is only available in Folia, used for detection
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
        return isFolia;
    }

    public static @NotNull String getVersion() {
        if (server == null) {
            server = HikariLib.getInstance().getServer();
        }
        return server.getVersion();
    }

    public static @NotNull String getMinecraftVersion() {
        if (server == null) {
            server = HikariLib.getInstance().getServer();
        }
        return server.getMinecraftVersion();
    }

    public static @NotNull String getBukkitVersion() {
        if (server == null) {
            server = HikariLib.getInstance().getServer();
        }
        return server.getBukkitVersion();
    }
    public static @NotNull String getName() {
        if (server == null) {
            server = HikariLib.getInstance().getServer();
        }
        return server.getName();
    }

    @SuppressWarnings("deprecation")
    public static @NotNull ServerMeta getMeta() {
        if (server == null) {
            server = HikariLib.getInstance().getServer();
        }
        return new ServerMeta(
                server.getAllowNether(),
                server.getAllowEnd(),
                server.getAllowFlight(),
                server.getSpawnRadius(),
                server.getViewDistance(),
                server.getSimulationDistance(),
                server.getMaxPlayers(),
                server.getMaxWorldSize(),
                server.getPort(),
                server.getName(),
                server.getMotd(),
                server.getShutdownMessage()
        );
    }

    /**
     * 服务器元信息，包含基础配置参数。
     * <p>通过 {@link #getMeta()} 获取。</p>
     */
    @AllArgsConstructor @Getter @Setter
    public static class ServerMeta {
        final boolean allowNether;
        final boolean allowEnd;
        final boolean allowFly;
        final int spawnRadius;
        final int viewDistance;
        final int simulationDistance;
        final int maxPlayers;
        final int maxWorldSize;
        final int port;
        final @Nullable String name;
        final @Nullable String motd;
        final @Nullable String shutdownMessage;
    }


}
