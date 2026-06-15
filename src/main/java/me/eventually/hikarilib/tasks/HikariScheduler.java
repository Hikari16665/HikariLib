/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:37:23
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/tasks/HikariScheduler.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.tasks;

import me.eventually.hikarilib.server.ServerEnvironment;
import org.jetbrains.annotations.NotNull;

/**
 * 调度器工具类，统一 Bukkit 和 Folia 的调度 API。
 * <p>根据服务端类型自动选择对应的调度器实现。</p>
 *
 * <pre>{@code
 * // 延迟 20 tick 后执行
 * HikariScheduler.addTimedTask(20, () -> player.sendMessage("Hello"), false);
 *
 * // 每 20 tick 重复执行
 * HikariScheduler.addRepeatingTask(20, () -> player.sendMessage("Tick"), false);
 * }</pre>
 */
public class HikariScheduler {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            return;
        }
        if (ServerEnvironment.isFolia()) {
            HikariFoliaScheduler.init();
        } else {
            HikariBukkitScheduler.init();
        }
        initialized = true;
    }

    public static void addTimedTask(long delay, @NotNull Runnable runnable, boolean async) {
        if (ServerEnvironment.isFolia()) {
            HikariFoliaScheduler.addTimedTask(delay, runnable, async);
        } else {
            HikariBukkitScheduler.addTimedTask(delay, runnable, async);
        }
    }

    public static void addRepeatingTask(long period, @NotNull Runnable runnable, boolean async) {
        if (ServerEnvironment.isFolia()) {
            HikariFoliaScheduler.addRepeatingTask(period, runnable, async);
        } else {
            HikariBukkitScheduler.addRepeatingTask(period, runnable, async);
        }
    }

    public static void shutdown() {
        if (ServerEnvironment.isFolia()) {
            HikariFoliaScheduler.shutdown();
        } else {
            HikariBukkitScheduler.shutdown();
        }
    }

}
