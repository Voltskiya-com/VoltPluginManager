package com.voltskiya.lib.configs.factory;

import com.voltskiya.lib.configs.data.config.AppleConfig.Builder;
import com.voltskiya.lib.configs.data.config.AppleConfigFolder;
import com.voltskiya.lib.configs.data.config.AppleConfigProps;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

public interface AppleConfigFactory {

    AppleConfigModule getModule();

    default AppleConfigFolder configFolder(String path, AppleConfigLike... configs) {
        return configFolder(List.of(path), configs);
    }

    default AppleConfigFolder configFolder(List<String> path, AppleConfigLike... configs) {
        AppleConfigProps props = new AppleConfigProps(path.toArray(String[]::new));
        return new AppleConfigFolder(props, configs);
    }

    default <DBType> Builder<DBType> config(Class<DBType> dbType, String name,
        String... path) {
        return new Builder<>(name, getModule(), dbType, path);
    }

    default <DBType> Builder<DBType> configJson(Class<DBType> dbType, String name,
        String... path) {
        return this.config(dbType, name, path).asJson();
    }


    default <DBType> Builder<DBType> configYaml(Class<DBType> dbType, String name,
        String... path) {
        throw new NotImplementedException("Config Yaml not implemented");
    }
}
