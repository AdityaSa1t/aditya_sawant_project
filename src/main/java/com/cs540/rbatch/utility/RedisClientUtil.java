package com.cs540.rbatch.utility;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.List;

public class RedisClientUtil {

    private static RedissonClient redissonClient;

    static{
        Config config = ConfigUtil.getRedisConfig();
        if(config!=null)
            redissonClient = Redisson.create(config);
        else
            redissonClient = Redisson.create();
    }

    public static List<Object> getList(String name){
        return redissonClient.getList(name);
    }

    public static boolean addToList(String name, Object object){
        return redissonClient.getList(name).add(object);
    }

    public static int getListSize(String name){
        return redissonClient.getList(name).size();
    }

    public static void clearList(String name){
        redissonClient.getList(name).clear();
    }

    public static boolean keyExists(String key){
        RKeys redisKeys = redissonClient.getKeys();
        return redisKeys.countExists(key) > 0;
    }

}
