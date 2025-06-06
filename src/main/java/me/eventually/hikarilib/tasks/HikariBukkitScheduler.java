package me.eventually.hikarilib.tasks;

import me.eventually.hikarilib.HikariLib;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CopyOnWriteArrayList;

public class HikariBukkitScheduler {
    private static boolean initialized = false;
    private static BukkitRunnable task;
    private static BukkitRunnable taskAsync;
    private static long currentTime = 0L;
    private static long currentTimeAsync = 0L;

    private static final int TICK_PERIOD = 50;

    private static final CopyOnWriteArrayList<TaskEntry> tasks = new CopyOnWriteArrayList<>();

    enum TaskScheduler {
        SYNC,
        ASYNC,
    }

    private static class TaskEntry {
        final TaskScheduler scheduler;
        long nextTime;
        protected final long period;
        protected final Runnable runnable;

        TaskEntry(TaskScheduler scheduler, long nextTime, long period, Runnable runnable) {
            this.nextTime = nextTime;
            this.period = period;
            this.runnable = runnable;
            this.scheduler = scheduler;
        }

        TaskEntry(TaskScheduler scheduler, long nextTime, Runnable runnable) {
             this(scheduler, nextTime, -1, runnable);
        }
    }

    public static void init() {
        if (initialized) {
            return;
        }
        currentTime = System.currentTimeMillis();
        currentTimeAsync = System.currentTimeMillis();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                currentTime++;
                tasks.removeIf(entry -> {
                    long nextTime = entry.nextTime;
                    if (entry.scheduler != TaskScheduler.SYNC) return false;
                    if (nextTime <= currentTime) {
                        entry.runnable.run();
                        if (entry.period > 0) {
                            entry.nextTime = currentTime + entry.period;
                            return false;
                        }
                        return true;
                    }
                    return false;
                });
            }
        };
        task.runTaskTimer(HikariLib.getInstance(), 0L, 1L);
        taskAsync = new BukkitRunnable() {
            @Override
            public void run() {
                currentTimeAsync = System.currentTimeMillis();
                tasks.removeIf(entry -> {
                    if (entry.scheduler != TaskScheduler.ASYNC) return false;
                    if (entry.nextTime <= currentTimeAsync) {
                        entry.runnable.run();
                        if (entry.period > 0) {
                            entry.nextTime = currentTimeAsync + entry.period;
                            return false;
                        }
                        return true;
                    }
                    return false;
                });
            }
        };
        taskAsync.runTaskTimerAsynchronously(HikariLib.getInstance(), 0L, 1L);
        initialized = true;
    }

    public static void shutdown() {
        if (!initialized) {
            return;
        }
        task.cancel();
        taskAsync.cancel();
        initialized = false;
    }

    public static void addTimedTask(long delay, Runnable runnable, boolean async) {
        if (delay < 0) throw new IllegalArgumentException("Delay must be positive or zero.");
        tasks.add(
                new TaskEntry(
                        async ? TaskScheduler.ASYNC : TaskScheduler.SYNC,
                        currentTime + delay * TICK_PERIOD,
                        runnable
                )
        );
    }

    public static void addRepeatingTask(long period, Runnable runnable, boolean async) {
        if (period <= 0) throw new IllegalArgumentException("Period must be positive.");
        tasks.add(
                new TaskEntry(
                        async ? TaskScheduler.ASYNC : TaskScheduler.SYNC,
                        currentTime + period * TICK_PERIOD,
                        period * TICK_PERIOD,
                        runnable
                )
        );
    }
}
