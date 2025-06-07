package me.eventually.hikarilib.entity;

import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import me.eventually.hikarilib.HikariLib;
import me.eventually.hikarilib.server.ServerEnvironment;
import org.bukkit.entity.Entity;

public class EntityUtil {
    public static void runAsEntity(Entity entity, Runnable runnable) {
        if (ServerEnvironment.isFolia()) {
            EntityScheduler entityScheduler = entity.getScheduler();
            entityScheduler.run(HikariLib.getInstance(), task -> {
                runnable.run();
            }, () -> {
            });
        } else {
            runnable.run();
        }
    }
}
