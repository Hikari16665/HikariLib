package me.eventually.hikarilib.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class LocationUtils {

    private LocationUtils() {}

    public static @Nullable Chunk getChunk(@NotNull Location location) {
        try {
            World world = location.getWorld();
            if (world == null) return null;
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
        if (dy < world.getMinHeight() || dy >= world.getMaxHeight()) return null;
        if (dx < 0 || dx >= 16 || dz < 0 || dz >= 16) return null;
        return chunk.getBlock(dx, dy, dz).getLocation();
    }
}
