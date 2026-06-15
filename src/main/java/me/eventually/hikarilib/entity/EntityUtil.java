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
import org.jetbrains.annotations.NotNull;

/**
 * 实体工具类。
 */
public class EntityUtil {

    /**
     * 以实体身份执行任务。
     * <p>在 Folia 服务端上通过实体调度器执行；在 Bukkit 上直接在当前线程执行。</p>
     *
     * @param entity   调度该任务的实体
     * @param runnable 要执行的任务
     */
    public static void runAsEntity(@NotNull Entity entity, @NotNull Runnable runnable) {
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
