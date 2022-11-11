package apple.lib.pmc;

import apple.lib.configs.data.AppleConfigsDatabase;
import apple.lib.configs.data.config.AppleConfig;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
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

public abstract class ApplePlugin extends JavaPlugin {

    private final List<AppleModule> modules = new ArrayList<>();
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
        for (AppleModule module : modules) {
            if (module.shouldEnable()) {
                module.onDisable();
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
        for (AppleModule module : getModules()) {
            registerModule(module);
            if (module.shouldEnable()) {
                loadModule(module);
            }
        }
    }

    public abstract Collection<AppleModule> getModules();

    private void enableModules() {
        for (AppleModule module : getRegisteredModules()) {
            if (module.shouldEnable()) {
                enableModule(module);
            }
        }
    }

    public List<AppleModule> getRegisteredModules() {
        return modules;
    }

    private void registerModule(AppleModule module) {
        modules.add(module);
        module._init(this);
        module.setLogger(getLogger());
    }

    private void loadModule(AppleModule module) {
        module.init();
    }

    public void enableModule(AppleModule module) {
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

    public void scheduleSyncDelayedTask(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, task, delay);
    }

    public void scheduleSyncDelayedTask(Runnable task) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, task);
    }

    public NamespacedKey namespacedKey(@NotNull String key) {
        return new NamespacedKey(this, key);
    }

    public <DBType> void registerConfig(AppleConfig<DBType> config) {
        AppleConfigsDatabase.get().registerConfig(config);
    }
}
