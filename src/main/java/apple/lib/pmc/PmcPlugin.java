package apple.lib.pmc;

import apple.lib.configs.command.AppleConfigsCommand;
import java.util.Collection;
import java.util.Collections;

public class PmcPlugin extends ApplePlugin {

    private static PmcPlugin instance;

    public PmcPlugin() {
        instance = this;
    }

    public static PmcPlugin get() {
        return instance;
    }

    @Override
    public Collection<AppleModule> getModules() {
        return Collections.emptyList();
    }

    @Override
    public void onEnablePost() {
        new AppleConfigsCommand();
    }
}
