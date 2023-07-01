package com.voltskiya.lib.timings.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CancellingAsyncTask implements VoltTask {


    private final Runnable runnable;
    private BukkitTask bukkitTask;
    private VoltTaskManager manager;

    public CancellingAsyncTask(Runnable runnable) {
        this.runnable = runnable;
    }

    public void run() {
        try {
            this.runnable.run();
        } finally {
            synchronized (this) {
                this.manager.popOnDisable(this.bukkitTask);
            }
        }
    }

    @Override
    public void start(VoltTaskManager manager) {
        synchronized (this) {
            this.manager = manager;
            Plugin plugin = manager.getPlugin();
            if (plugin.isEnabled())
                this.bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(plugin, this::run);
            this.manager.putOnDisable(this.bukkitTask);
        }
    }
}
