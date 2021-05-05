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

/**
 * This class provides methods to fetch configuration details from the config files.
 */
public class ConfigUtil {

    private static Map<String, Object> listConfig;

    /**
     * This method reads the configuration for creation of a Redis instance
     * @return {@link Config} object required to create a Redis instance
     */
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

    /**
     * This method returns configuration of all the lists that need to be created in Redis from the configuration file.
     * @return Map with key as the list name and value being a nested Map with the properties mentioned on configuration file.
     */
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

    /**
     * This method gets the size of the list mentioned in the configuration file.
     * @param name Name of the list
     * @return Size of the list. In case, the list is not size bound, it would return null.
     */
    public static Integer getListSize(String name){
        if(!Objects.requireNonNull(getListConfig()).containsKey(name))
            throw new IllegalArgumentException("List with name "+name+" does not exist in rbatch_config.yml");
        else{
            return (Integer) ((Map<String, Object>)getListConfig().get(name)).get(Constants.SIZE);
        }
    }

    /**
     * This method returns the class name of the type of object a list would store.
     * @param name Name of the list
     * @return Class name
     */
    public static String getListClazz(String name){
        if(!Objects.requireNonNull(getListConfig()).containsKey(name))
            throw new IllegalArgumentException("List with name "+name+" does not exist in rbatch_config.yml");
        else{
            return (String) ((Map<String, Object>)getListConfig().get(name)).get(Constants.CLAZZ);
        }
    }

    /**
     * This method returns the name of the callback which would process the objects stored in the list.
     * @param name Name of the list
     * @return Name of the callback
     */
    public static String getListCallback(String name){
        if(!Objects.requireNonNull(getListConfig()).containsKey(name))
            throw new IllegalArgumentException("List with name "+name+" does not exist in rbatch_config.yml");
        else{
            return (String) ((Map<String, Object>)getListConfig().get(name)).get(Constants.CALLBACK);
        }
    }
}
