/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:35:13
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 11:41:12
 * @FilePath: src/main/java/me/eventually/hikarilib/world/LocationUtils.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.world;

import me.eventually.hikarilib.HikariLib;
import me.eventually.hikarilib.server.ServerEnvironment;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 位置与传送工具类。
 */
public class LocationUtils {

    private LocationUtils() {}

    /**
     * 获取位置所在的区块，失败时返回 null。
     *
     * @param location 目标位置
     * @return 所在区块，可能为 null
     */
    @Contract("null -> null; !null -> !null")
    public static @Nullable Chunk getChunk(@Nullable Location location) {
        if (location == null) return null;
        try {
            World world = location.getWorld();
            if (world == null) {
                return null;
            }
            return world.getChunkAt(location);
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 根据世界和坐标获取区块。
     *
     * @param world 目标世界
     * @param x     区块 X 坐标
     * @param z     区块 Z 坐标
     * @return 对应的区块
     */
    public static @NotNull Chunk getChunk(@NotNull World world, int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return world.getChunkAt(chunkX, chunkZ);
    }

    /**
     * 获取区块内的偏移位置。
     *
     * @param chunk 目标区块
     * @param dx    区块内 X 偏移 (0-15)
     * @param dy    Y 坐标
     * @param dz    区块内 Z 偏移 (0-15)
     * @return 对应位置，偏移越界或 Y 越界时返回 null
     */
    @Contract("_, _, _, _ -> new")
    public static @Nullable Location getChunkOffsetLocation(@NotNull Chunk chunk, int dx, int dy, int dz) {
        World world = chunk.getWorld();
        if (dy < world.getMinHeight() || dy >= world.getMaxHeight()) {
            return null;
        }
        if (dx < 0 || dx >= 16 || dz < 0 || dz >= 16) {
            return null;
        }
        return chunk.getBlock(dx, dy, dz).getLocation();
    }

    /**
     * 传送玩家到指定位置（同步传送）。
     *
     * @param player   目标玩家
     * @param location 目标位置
     * @return 是否传送成功
     */
    public static boolean teleportPlayer(@NotNull Player player, @NotNull Location location) {
        return teleportPlayer(player, location, false);
    }

    /**
     * 传送玩家到指定位置。
     * <p>在 Folia 服务端上强制异步传送；在 Bukkit 服务端上由 async 参数决定。</p>
     *
     * @param player   目标玩家
     * @param location 目标位置
     * @param async    是否异步传送（Bukkit 上有效，Folia 上忽略）
     * @return 是否传送成功
     */
    public static boolean teleportPlayer(@NotNull Player player, @NotNull Location location, boolean async) {
        AtomicBoolean teleported = new AtomicBoolean(false);
        if (ServerEnvironment.isFolia()) {
            HikariLib.getInstance().getServer().getScheduler().runTaskAsynchronously(HikariLib.getInstance(), (task) -> {
                player.teleportAsync(location).thenAccept(teleported::set);
            });
        } else {
            if (async) {
                player.teleportAsync(location).thenAccept(teleported::set);
            } else {
                teleported.set(player.teleport(location));
            }
        }
        return teleported.get();
    }
}
