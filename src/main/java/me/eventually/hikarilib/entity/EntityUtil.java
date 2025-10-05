/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-07 13:39:04
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:50
 * @FilePath: src/main/java/me/eventually/hikarilib/entity/EntityUtil.java
 * @Description: This file is licensed under MIT license
 */
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
