package com.voltskiya.lib;

import apple.utilities.util.FileFormatting;
import com.voltskiya.lib.configs.factory.AppleConfigModule;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractModule implements AppleConfigModule {

    private boolean enabled;
    private AbstractVoltPlugin parent;
    private File dataFolder = null;
    private Logger logger;


    public void _init(AbstractVoltPlugin parent) {
        this.parent = parent;
        // i still want this to work better than it does
        this.logger = LoggerFactory.getLogger(parent.getName() + "] [" + this.getName());
        this.registerConfigs();
    }

    public void init() {
    }

    public abstract void enable();

    public void onDisable() {
    }


    public Logger logger() {
        return this.logger;
    }

    public void doEnable() {
        this.enabled = true;
        enable();
    }

    public File getDataFolder() {
        if (this.dataFolder == null) {
            this.dataFolder = new File(parent.getDataFolder(), getName());
            if (!this.dataFolder.exists())
                this.dataFolder.mkdirs();
        }
        return this.dataFolder;
    }

    public File getFile(String... children) {
        return FileFormatting.fileWithChildren(getDataFolder(), children);
    }

    public boolean shouldEnable() {
        return true;
    }

    public abstract String getName();

    public AbstractVoltPlugin getPlugin() {
        return parent;
    }

    public AbstractModule getModule() {
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
