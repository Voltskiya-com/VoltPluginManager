package com.voltskiya.lib.configs.factory;

import com.voltskiya.lib.configs.data.config.AppleConfig;
import com.voltskiya.lib.configs.data.config.AppleConfigProps;

public interface AppleConfigLike {

    AppleConfig<?>[] build(AppleConfigProps addedProps);

    default AppleConfig<?>[] build() {
        return this.build(AppleConfigProps.empty());
    }
}
