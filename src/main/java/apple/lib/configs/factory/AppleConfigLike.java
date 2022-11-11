package apple.lib.configs.factory;

import apple.lib.configs.data.config.AppleConfig;
import apple.lib.configs.data.config.AppleConfigProps;

public interface AppleConfigLike {

    AppleConfig<?>[] build(AppleConfigProps addedProps);

    default AppleConfig<?>[] build() {
        return this.build(AppleConfigProps.empty());
    }
}
