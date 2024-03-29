package com.voltskiya.lib.pmc;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.AbstractVoltPlugin;
import java.util.Collection;
import java.util.Collections;

public class PmcPlugin extends AbstractVoltPlugin {

    private static PmcPlugin instance;

    public PmcPlugin() {
        instance = this;
    }

    public static PmcPlugin get() {
        return instance;
    }

    @Override
    public Collection<AbstractModule> getModules() {
        return Collections.emptyList();
    }
}
