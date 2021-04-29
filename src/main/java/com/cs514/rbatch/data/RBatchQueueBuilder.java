package com.cs514.rbatch.data;

public class RBatchQueueBuilder {

    long range;
    long size;
    String key;
    boolean isBounded = false;

    public RBatchQueueBuilder(String key, long range) {
        this.range = range;
        this.key = key;
    }

    public RBatchQueueBuilder setBound(long size){
        this.isBounded = true;
        this.size = size;
        return this;
    }

    public RBatchQueue build(){
        return new RBatchQueue(this);
    }
}
