package com.cs514.rbatch.api;

import com.cs514.rbatch.data.RBatchList;
import com.cs514.rbatch.utility.ConfigUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RBatchProcessor {

    private static Map<String, RBatchList> rBatchLists = new HashMap<>();
    //TODO add custom exceptions everywhere and log instead of throwing them
    public static void enqueue(String name, Object obj, Object caller) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
        rBatchList.add(obj);
    }
}
