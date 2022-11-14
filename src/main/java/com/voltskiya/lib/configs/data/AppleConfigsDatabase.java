package com.voltskiya.lib.configs.data;

import com.voltskiya.lib.configs.data.config.AppleConfig;
import com.voltskiya.lib.configs.data.config.AppleConfigPath;
import com.voltskiya.lib.configs.data.config.ApplePartialConfigPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class AppleConfigsDatabase {

    private static final AppleConfigsDatabase instance = new AppleConfigsDatabase();

    private final Map<AppleConfigPath, AppleConfig<?>> configs = new HashMap<>();
    private final Map<ApplePartialConfigPath, Set<String>> pathAutoComplete = new HashMap<>();

    public static AppleConfigsDatabase get() {
        return instance;
    }

    public void registerConfig(AppleConfig<?> config) {
        this.configs.put(config.getFullName(), config);
        ApplePartialConfigPath partial = new ApplePartialConfigPath(Collections.emptyList());
        for (String path : config.iteratePath()) {
            Set<String> autoComplete = pathAutoComplete.getOrDefault(partial, new HashSet<>());
            autoComplete.add(path);
            pathAutoComplete.put(partial, autoComplete);
            partial = partial.addCopy(path);
        }

    }

    @Nullable
    public AppleConfig<?> findConfig(List<String> path) {
        if (path.isEmpty())
            return null;
        List<String> partialName = new ArrayList<>();
        for (String n : path) {
            partialName.add(n);
            AppleConfig<?> config = getConfig(partialName);
            if (config != null)
                return config;
        }
        return null;
    }


    private AppleConfig<?> getConfig(List<String> fullPath) {
        if (fullPath.isEmpty())
            return null;
        String name = fullPath.get(fullPath.size() - 1);
        List<String> path = fullPath.subList(0, fullPath.size() - 1);
        return getConfig(name, path.toArray(String[]::new));
    }

    private AppleConfig<?> getConfig(String name, String[] path) {
        return this.configs.get(new AppleConfigPath(name, path));
    }

    public Set<String> autoComplete(List<String> pathStrings) {
        ApplePartialConfigPath path = new ApplePartialConfigPath(pathStrings);
        return this.pathAutoComplete.getOrDefault(path, Collections.emptySet());
    }
}
