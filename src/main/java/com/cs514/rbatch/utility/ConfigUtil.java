package com.cs514.rbatch.utility;

import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class ConfigUtil {

    public static Config getRedisConfig(){
        try {
            URI redisConfigFileUri = Objects.requireNonNull(ConfigUtil.class.getClassLoader()
                    .getResource("redis_config.yml")).toURI();
            return Config.fromYAML(new File(redisConfigFileUri));
        } catch (URISyntaxException | IOException e) {
            LoggerUtil.error("redis_config.yml file does not exist at correct location", e);
        }
        return null;
    }
}
