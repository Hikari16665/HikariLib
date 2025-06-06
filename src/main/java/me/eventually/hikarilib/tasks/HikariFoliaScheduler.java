package me.eventually.hikarilib.tasks;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import me.eventually.hikarilib.HikariLib;
import org.bukkit.Server;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class HikariFoliaScheduler {
    private static boolean initialized = false;
    private static GlobalRegionScheduler schedulerGlobal;
    private static AsyncScheduler schedulerAsync;

    private static final CopyOnWriteArrayList<TaskEntry> timedTasks = new CopyOnWriteArrayList<>();

    private static long currentTime = 0L;
    private static long currentTimeAsync = 0L;

    private static final int TICK_PERIOD = 50;


    public static void init() {
        if (initialized) {
            return;
        }
        currentTime = System.currentTimeMillis();
        currentTimeAsync = System.currentTimeMillis();
        Server server = HikariLib.getInstance().getServer();
        schedulerGlobal = server.getGlobalRegionScheduler();
        schedulerAsync = server.getAsyncScheduler();
        schedulerGlobal.runAtFixedRate(HikariLib.getInstance(), (task) -> {
            currentTime++;
            timedTasks.removeIf(entry -> {
                long nextTime = entry.runTime;
                if (entry.scheduler != TaskScheduler.SYNC) return false;
                if (nextTime <= currentTime) {
                    entry.runnable.run();
                    if (entry.period > 0) {
                        entry.runTime = currentTime + entry.period;
                        return false;
                    }
                    return true;
                }
                return false;
            });
        }, 0L, 1L);
        schedulerAsync.runAtFixedRate(HikariLib.getInstance(), (task) -> {
            currentTimeAsync = System.currentTimeMillis();
            timedTasks.removeIf(entry -> {
                if (entry.scheduler != TaskScheduler.ASYNC) return false;
                if (entry.runTime <= currentTimeAsync) {
                    entry.runnable.run();
                    if (entry.period > 0) {
                        entry.runTime = currentTimeAsync + entry.period;
                        return false;
                    }
                    return true;
                }
                return false;
            });
        },  0L, TICK_PERIOD, TimeUnit.MILLISECONDS);
        initialized = true;
    }

    public static void shutdown() {
        if (!initialized) {
            return;
        }
        schedulerGlobal.cancelTasks(HikariLib.getInstance());
        schedulerAsync.cancelTasks(HikariLib.getInstance());
    }

    enum TaskScheduler {
        SYNC,
        ASYNC,
    }

    private static class TaskEntry {
        TaskScheduler scheduler;
        long runTime;
        long period;
        Runnable runnable;

        public TaskEntry(TaskScheduler scheduler, long runTime, Runnable runnable) {
            this(scheduler, runTime, -1, runnable);
        }


        public TaskEntry(TaskScheduler scheduler, long runTime, long period, Runnable runnable) {
            this.scheduler = scheduler;
            this.runTime = currentTime + runTime * TICK_PERIOD;
            this.period = period;
            this.runnable = runnable;
        }
    }

    public static void addTimedTask(long delay, Runnable runnable, boolean async) {
        if (delay < 0) throw new IllegalArgumentException("Delay must be positive or zero.");
        timedTasks.add(
                new TaskEntry(
                        async ? TaskScheduler.ASYNC : TaskScheduler.SYNC,
                        currentTime + delay * TICK_PERIOD,
                        runnable
                )
        );
    }

    public static void addRepeatingTask(long period, Runnable runnable, boolean async) {
        if (period <= 0) throw new IllegalArgumentException("Period must be positive.");
        timedTasks.add(
                new TaskEntry(
                        async ? TaskScheduler.ASYNC : TaskScheduler.SYNC,
                        currentTime + period * TICK_PERIOD,
                        period * TICK_PERIOD,
                        runnable
                )
        );
    }
}
