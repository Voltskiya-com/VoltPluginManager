package com.voltskiya.lib.pmc;

import apple.lib.ebean.database.AppleEbeanDatabaseMetaConfig;
import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.AbstractVoltPlugin;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;

public class PmcPlugin extends AbstractVoltPlugin {

    private static PmcPlugin instance;

    public PmcPlugin() {
        instance = this;
    }

    public static PmcPlugin get() {
        return instance;
    }

    @Override
    public void onLoadPre() {
        String loadDepsName = "LoadDependencies";
        Plugin loadDepsPlugin = Bukkit.getPluginManager().getPlugin(loadDepsName);
        if (loadDepsPlugin == null) {
            throw new IllegalPluginAccessException("'%s' not loaded before '%s'".formatted(loadDepsName, getName()));
        }

        AppleEbeanDatabaseMetaConfig.configureMeta(loadDepsPlugin.getClass(),
            loadDepsPlugin.getDataFolder(),
            loadDepsPlugin.getSLF4JLogger()::error,
            loadDepsPlugin.getSLF4JLogger()::info);
    }

    @Override
    public Collection<AbstractModule> getModules() {
        return Collections.emptyList();
    }
}
