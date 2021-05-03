package com.cs540.rbatch.data;

import com.cs540.rbatch.utility.RedisClientUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RBatchList {

    private String name;
    private Class clazz;
    private Object caller;
    private Method callBack;
    private boolean isBounded;
    private long size;

    private RBatchList(){

    }

    public void add(Object obj) throws InvocationTargetException, IllegalAccessException {
        if(obj.getClass() == clazz){
            RedisClientUtil.addToList(this.name, obj);
            if(isBounded && RedisClientUtil.getListSize(name)==size)
                makeCallback();
        }
        throw new IllegalArgumentException("List "+name+" accepts only objects of type "+clazz.getName());
    }

    private void makeCallback() throws InvocationTargetException, IllegalAccessException {
        callBack.invoke(caller, RedisClientUtil.getList(name));
        RedisClientUtil.clearList(name);
    }

    public static class RBatchListBuilder {
        private String name;
        private Class clazz;
        private Object caller;
        private Method callback;
        private boolean isBounded;
        private long size;

        public RBatchListBuilder(String name, Class clazz, Object caller, Method callback){
            this.name = name;
            this.clazz = clazz;
            this.isBounded = false;
            this.caller = caller;
            this.callback = callback;
        }
        
        public RBatchListBuilder size(Integer size){
            if(size!=null) {
                this.size = size;
                this.isBounded = true;
            }
            return this;
        }

        public RBatchList build(){
            RBatchList queue = new RBatchList();
            queue.name = this.name;
            queue.clazz = clazz;
            queue.isBounded = this.isBounded;
            queue.size = this.size;
            queue.caller = this.caller;
            queue.callBack = this.callback;
            return queue;
        }
        
    }
}
