package com.cs540.rbatch.utility;

import org.redisson.config.Config;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

public class ConfigUtil {

    private static Map<String, Object> listConfig;

    public static Config getRedisConfig(){
        try {
            URI redisConfigFileUri = Objects.requireNonNull(ConfigUtil.class.getClassLoader()
                    .getResource(Constants.REDIS_CONFIG_FILE_NAME)).toURI();
            return Config.fromYAML(new File(redisConfigFileUri));
        } catch (URISyntaxException | IOException e) {
            LoggerUtil.error("redis_config.yml file does not exist at correct location", e);
        }
        return null;
    }

    public static Map<String, Object> getListConfig(){
        if(listConfig!=null)
            return listConfig;
        else {
            try {
                URI uri = Objects.requireNonNull(ConfigUtil.class.getClassLoader()
                        .getResource(Constants.RBATCH_CONFIG_FILE_NAME)).toURI();
                Yaml yaml = new Yaml();
                listConfig = yaml.load(new FileInputStream(new File(uri)));
                return listConfig;
            } catch (URISyntaxException | FileNotFoundException e) {
                LoggerUtil.error("rbatch_config.yml file does not exist at correct location", e);
                return null;
            }
        }
    }

    public static Integer getListSize(String name){
        if(!Objects.requireNonNull(getListConfig()).containsKey(name))
            throw new IllegalArgumentException("List with name "+name+" does not exist in rbatch_config.yml");
        else{
            return (Integer) ((Map<String, Object>)getListConfig().get(name)).get(Constants.SIZE);
        }
    }

    public static String getListClazz(String name){
        if(!Objects.requireNonNull(getListConfig()).containsKey(name))
            throw new IllegalArgumentException("List with name "+name+" does not exist in rbatch_config.yml");
        else{
            return (String) ((Map<String, Object>)getListConfig().get(name)).get(Constants.CLAZZ);
        }
    }

    public static String getListCallback(String name){
        if(!Objects.requireNonNull(getListConfig()).containsKey(name))
            throw new IllegalArgumentException("List with name "+name+" does not exist in rbatch_config.yml");
        else{
            return (String) ((Map<String, Object>)getListConfig().get(name)).get(Constants.CALLBACK);
        }
    }
}
