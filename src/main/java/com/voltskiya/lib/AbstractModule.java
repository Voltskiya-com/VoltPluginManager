package com.voltskiya.lib;

import apple.utilities.util.FileFormatting;
import com.voltskiya.lib.configs.factory.AppleConfigModule;
import com.voltskiya.lib.timings.scheduler.VoltTaskManager;
import java.io.File;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractModule implements AppleConfigModule {

    private boolean enabled;
    private AbstractVoltPlugin parent;
    private File dataFolder = null;
    private Logger logger;
    private VoltTaskManager taskManager;


    public final void _init(AbstractVoltPlugin parent) {
        this.parent = parent;
        // I still want this to work better than it does
        this.logger = LoggerFactory.getLogger(parent.getName() + "] [" + this.getName());
        this.registerConfigs();
    }

    public VoltTaskManager getTaskManager() {
        if (this.taskManager == null) this.taskManager = new VoltTaskManager(this);
        return this.taskManager;
    }

    public void init() {
    }

    public abstract void enable();

    public final void onDisable_() {
        if (this.taskManager != null) this.taskManager.onDisable();
        this.onDisable();
        this.setEnabled(false);
    }

    public void onDisable() {
    }


    public final Logger logger() {
        return this.logger;
    }

    public final void onEnable_() {
        this.setEnabled(true);
        enable();
    }

    @Override
    public File getDataFolder() {
        if (this.dataFolder == null) {
            this.dataFolder = new File(parent.getDataFolder(), getName());
            this.dataFolder.mkdirs();
        }
        return this.dataFolder;
    }

    public File getFile(String... children) {
        return FileFormatting.fileWithChildren(getDataFolder(), children);
    }

    public NamespacedKey namespacedKey(String key) {
        return new NamespacedKey(getPlugin(), getName() + "." + key);
    }

    public boolean shouldEnable() {
        return true;
    }

    public abstract String getName();

    public final AbstractVoltPlugin getPlugin() {
        return parent;
    }

    public final AbstractModule getModule() {
        return this;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    private void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
