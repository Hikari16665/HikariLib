package me.eventually.hikarilib.tasks;

import me.eventually.hikarilib.server.ServerEnvironment;

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

    public static void addTimedTask(long delay, Runnable runnable,  boolean async) {
        if (ServerEnvironment.isFolia()) {
            HikariFoliaScheduler.addTimedTask(delay, runnable, async);
        } else {
            HikariBukkitScheduler.addTimedTask(delay, runnable, async);
        }
    }

    public static void addRepeatingTask(long period, Runnable runnable, boolean async) {
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
