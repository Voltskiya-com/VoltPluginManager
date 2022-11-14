package com.voltskiya.lib.configs.data.config;

import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.Arrays;

public record AppleConfigFolder(
    AppleConfigProps props,
    AppleConfigLike[] configs
) implements AppleConfigLike {

    @Override
    public AppleConfig<?>[] build(AppleConfigProps addedProps) {
        AppleConfigProps newProps = addedProps.addOuterProps(this.props);
        return Arrays.stream(this.configs)
            .flatMap(config -> Arrays.stream(config.build(newProps)))
            .toArray(AppleConfig<?>[]::new);
    }
}
