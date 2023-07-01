package com.voltskiya.lib;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.voltskiya.lib.configs.data.AppleConfigsDatabase;
import com.voltskiya.lib.configs.data.config.AppleConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractVoltPlugin extends JavaPlugin {

    private final List<AbstractModule> modules = new ArrayList<>();
    private PaperCommandManager commandManager;
    private LuckPerms luckPerms;

    @Override
    public void onLoad() {
        onLoadPre();
        loadModules();
        onLoadPost();
    }

    public void onLoadPre() {
    }

    public void onLoadPost() {
    }


    @Override
    public void onEnable() {
        onEnablePreInit();
        setUp();
        onEnablePre();
        initialize();
        enableModules();
        onEnablePost();
    }


    public void onEnablePreInit() {
    }

    public void onEnablePre() {
    }

    public void initialize() {
    }

    public void onEnablePost() {
    }


    @Override
    public void onDisable() {
        onDisablePre();
        for (AbstractModule module : modules) {
            if (module.shouldEnable()) {
                module.onDisable_();
                module.setEnabled(false);
            }
        }
        onDisablePost();
    }

    public void onDisablePre() {
    }

    public void onDisablePost() {
    }


    //
    // Dealing with modules
    //
    private void loadModules() {
        for (AbstractModule module : getModules()) {
            registerModule(module);
            if (module.shouldEnable()) {
                loadModule(module);
            }
        }
    }

    public abstract Collection<AbstractModule> getModules();

    private void enableModules() {
        for (AbstractModule module : getRegisteredModules()) {
            if (module.shouldEnable()) {
                enableModule(module);
            }
        }
    }

    public List<AbstractModule> getRegisteredModules() {
        return modules;
    }

    private void registerModule(AbstractModule module) {
        modules.add(module);
        module._init(this);
    }

    private void loadModule(AbstractModule module) {
        module.init();
    }

    public void enableModule(AbstractModule module) {
        module.doEnable();
        getLogger().log(Level.INFO, "Enabled Module: " + module.getName());
    }

    //
    // Setting up integration
    //
    private void setUp() {
        setupACF();
        setupLuckPerms();
    }

    private void setupACF() {
        commandManager = new PaperCommandManager(this);
    }

    private void setupLuckPerms() {
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            luckPerms = LuckPermsProvider.get();
        }
    }

    public @NotNull LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }


    //
    // Quality of Life utilities
    //
    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(BaseCommand command) {
        getCommandManager().registerCommand(command);
    }

    public int scheduleSyncDelayedTask(Runnable task, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(this, task, delay);
    }

    public int scheduleSyncDelayedTask(Runnable task) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(this, task);
    }

    public void runTaskAsync(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, task, delay);
    }

    public void runTaskAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(this, task);
    }

    public NamespacedKey namespacedKey(@NotNull String key) {
        return new NamespacedKey(this, key);
    }

    public <DBType> void registerConfig(AppleConfig<DBType> config) {
        AppleConfigsDatabase.get().registerConfig(config);
    }
}
