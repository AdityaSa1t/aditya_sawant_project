package com.cs540.rbatch.data;

import com.cs540.rbatch.utility.RedisClientUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class represents the list present in Redis which would hold the objects for batch processing.
 * This class also hold the properties of the lists like whether bounded by size, time etc.
 */
public class RBatchList {

    /*Name of the list in Redis*/
    private String name;
    /*Type of object the list would hold*/
    private Class clazz;
    /*Object on which the callback needs to be called*/
    private Object caller;
    /*Method which would process the objects in the list*/
    private Method callBack;
    /*Is the list bounded by size*/
    private boolean isBounded;
    /*Size of the list if it is bounded by size*/
    private long size;

    private RBatchList(){

    }

    /**
     * This method adds the given obj to its list. When a condition to flush the list is met, this function also makes a
     * callback.
     * @param obj {@link Object} to be added to be added to the list
     * @throws InvocationTargetException This exception is thrown when the callback throws an exception during its execution
     * @throws IllegalAccessException This exception is thrown when the callback is inaccessible
     */
    public void add(Object obj) throws InvocationTargetException, IllegalAccessException {
        if(obj.getClass() == clazz){
            RedisClientUtil.addToList(this.name, obj);
            if(isBounded && RedisClientUtil.getListSize(name)==size)
                makeCallback();
        }
        throw new IllegalArgumentException("List "+name+" accepts only objects of type "+clazz.getName());
    }

    /**
     * This method invokes the given callback with all the objects present in the list at the given time.
     * @throws InvocationTargetException This exception is thrown when the callback throws an exception during its execution
     * @throws IllegalAccessException This exception is thrown when the callback is inaccessible
     */
    private void makeCallback() throws InvocationTargetException, IllegalAccessException {
        callBack.invoke(caller, RedisClientUtil.getList(name));
        RedisClientUtil.clearList(name);
    }

    /**
     * This class facilitates in the creation of {@link RBatchList}. Additional methods can be added to this class when
     * new properties are added to {@link RBatchList} class.
     */
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
