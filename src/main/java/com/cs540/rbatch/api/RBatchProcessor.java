package com.cs540.rbatch.api;

import com.cs540.rbatch.data.RBatchList;
import com.cs540.rbatch.utility.ConfigUtil;
import com.cs540.rbatch.utility.LoggerUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RBatchProcessor {

    private static Map<String, RBatchList> rBatchLists = new HashMap<>();
    //TODO add custom exceptions everywhere and log instead of throwing them
    public static void enqueue(String name, Object obj, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        RBatchList rBatchList = null;
        if(rBatchLists.containsKey(name))
            rBatchList = rBatchLists.get(name);
        else{
            Class clazz = Class.forName(ConfigUtil.getListClazz(name));
            Method callback = caller.getClass().getMethod(ConfigUtil.getListCallback(name), List.class);
            rBatchList = new RBatchList.RBatchListBuilder(name, clazz, caller, callback)
                    .size(ConfigUtil.getListSize(name))
                    .build();
            rBatchLists.put(name, rBatchList);
        }
        try {
            rBatchList.add(obj);
        } catch (InvocationTargetException e) {
            LoggerUtil.error("Callback method failed to exeucte. ", e);
        } catch (IllegalAccessException e) {
            LoggerUtil.error(e.getMessage());
        }
    }

    public static void enqueueAll(String name, Object[] objects, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        enqueueAll(name, Arrays.asList(objects), caller);
    }

    public static void enqueueAll(String name, Set<Object> objects, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        enqueueAll(name, new ArrayList<>(objects), caller);
    }

    public static void enqueueAll(String name, List<Object> objects, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        for(Object obj : objects){
            enqueue(name, obj, caller);
        }
    }
}
