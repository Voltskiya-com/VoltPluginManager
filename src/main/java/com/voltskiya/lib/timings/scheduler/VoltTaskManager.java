package com.voltskiya.lib.timings.scheduler;

import com.voltskiya.lib.AbstractModule;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class VoltTaskManager {

    private final Map<Integer, BukkitTask> runningBukkitTasks = new HashMap<>();
    private final AbstractModule module;

    public VoltTaskManager(AbstractModule module) {
        this.module = module;
    }

    public void onDisable() {
        Stream<BukkitTask> tasks;
        synchronized (this.runningBukkitTasks) {
            tasks = this.runningBukkitTasks.values().stream();
        }
        tasks.forEach(bukkitTask -> {
            bukkitTask.cancel();
            if (!bukkitTask.isCancelled()) {
                System.err.println("wtf");
            }
        });
    }

    public void putOnDisable(BukkitTask onDisable) {
        synchronized (this.runningBukkitTasks) {
            this.runningBukkitTasks.put(onDisable.getTaskId(), onDisable);
        }
    }

    public void popOnDisable(BukkitTask onDisable) {
        synchronized (this.runningBukkitTasks) {
            this.runningBukkitTasks.remove(onDisable.getTaskId());
        }
    }

    public Plugin getPlugin() {
        return this.module.getPlugin();
    }
}
