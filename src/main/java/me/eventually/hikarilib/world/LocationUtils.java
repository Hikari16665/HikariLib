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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationUtils {

    private LocationUtils() {}

    public static @Nullable Chunk getChunk(@NotNull Location location) {
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
    public static @NotNull Chunk getChunk(@NotNull World world, int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return world.getChunkAt(chunkX, chunkZ);
    }
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

    public static boolean teleportPlayer(Player player, Location location) {
        return teleportPlayer(player, location, false);
    }

    /**
     * Teleports a player to a location. Always async if Folia is used
     * @param player the player to teleport
     * @param location to teleport to
     * @param async whether to teleport asynchronously
     * @return true if the teleport was successful, false otherwise
     */
    public static boolean teleportPlayer(Player player, Location location, boolean async) {
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
