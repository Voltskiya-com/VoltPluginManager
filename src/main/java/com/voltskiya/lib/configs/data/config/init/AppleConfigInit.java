package com.voltskiya.lib.configs.data.config.init;

import com.voltskiya.lib.configs.data.config.AppleConfig;

public class AppleConfigInit implements IAppleConfigInit {

    private transient AppleConfig<?> manager;

    @Override
    public void setManager(AppleConfig<?> manager) {
        this.manager = manager;
    }

    public void save() {
        this.manager.save();
    }

    public void saveNow() {
        this.manager.saveNow();
    }
}
