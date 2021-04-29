package com.cs514.rbatch.utility;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisClientUtil {

    public static RedissonClient redissonClient;

    static{
        Config config = ConfigUtil.getRedisConfig();
        if(config!=null)
            redissonClient = Redisson.create(config);
        else
            redissonClient = Redisson.create();
    }

    public static boolean keyExists(String... key){
        RKeys redisKeys = redissonClient.getKeys();
        return redisKeys.countExists(key) > 0;
    }

}
