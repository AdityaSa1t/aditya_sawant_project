package com.cs514.rbatch.data;

import com.cs514.rbatch.utility.RedisClientUtil;
import org.redisson.api.RQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBatchQueue {
    long range;
    long size;
    String key;
    boolean isBounded;

    private static Logger logger = LoggerFactory.getLogger("RBatchQueue");

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public RBatchQueue(RBatchQueueBuilder builder) {
        this.key = builder.key;
        this.range = builder.range;
        this.isBounded = builder.isBounded;
        if(builder.isBounded){
            this.size = builder.size;
        }
    }

    public RQueue<Object> getQueue(){
        if(range > size && isBounded){
            logger.error("You're trying to fit too many elements in queue: " + key);
            return null;
        }
        if(RedisClientUtil.keyExists(key)){
            logger.error("Queue: "+ key +" already exists");
            return null;
        }
        RQueue<Object> queue = RedisClientUtil.redissonClient.getQueue(key);
        for(int i=0; i<range; i++){
            queue.add(i);
        }
        return queue;
    }
}
