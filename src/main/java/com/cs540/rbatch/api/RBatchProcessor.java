package com.cs540.rbatch.api;

import com.cs540.rbatch.data.RBatchList;
import com.cs540.rbatch.utility.ConfigUtil;
import com.cs540.rbatch.utility.LoggerUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * RBatchProcessor class provides methods to batch user objects in their corresponding Redis lists.
 * rbatch_config.yml holds the information about the lists and type of objects they hold.
 */
public class RBatchProcessor {

    private static Map<String, RBatchList> rBatchLists = new HashMap<>();


    /**
     * This method adds the given object in list specified by the input parameter <i>name</i>.
     * This list will hold the objects till condition to flush the list is met, and the callback
     * is made with all the objects present in the list.
     * @param name {@link String} containing the name of the list in which the obj needs to be stored
     * @param obj {@link Object} that needs to stored in a list to be processed in the future
     * @param caller {@link Object} on which the callback will be called
     * @throws ClassNotFoundException Class by the name <i>clazz</i> mentioned in the config file does not exist
     * @throws NoSuchMethodException <i>callback</i> mentioned in the config file does not exist
     */
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

    /**
     * This method adds the given array of object in list specified by the input parameter <i>name</i>.
     * This list will hold the objects till condition to flush the list is met, and the callback
     * is made with all the objects present in the list.
     * @param name {@link String} containing the name of the list in which the obj needs to be stored
     * @param objects Array of {@link Object} that needs to stored in a list to be processed in the future
     * @param caller {@link Object} on which the callback will be called
     * @throws ClassNotFoundException Class by the name <i>clazz</i> mentioned in the config file does not exist
     * @throws NoSuchMethodException <i>callback</i> mentioned in the config file does not exist
     */
    public static void enqueueAll(String name, Object[] objects, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        enqueueAll(name, Arrays.asList(objects), caller);
    }

    /**
     * This method adds the given {@link Set} of object in list specified by the input parameter <i>name</i>.
     * This list will hold the objects till condition to flush the list is met, and the callback
     * is made with all the objects present in the list.
     * @param name {@link String} containing the name of the list in which the obj needs to be stored
     * @param objects {@link Set} of {@link Object} that needs to stored in a list to be processed in the future
     * @param caller {@link Object} on which the callback will be called
     * @throws ClassNotFoundException Class by the name <i>clazz</i> mentioned in the config file does not exist
     * @throws NoSuchMethodException <i>callback</i> mentioned in the config file does not exist
     */
    public static void enqueueAll(String name, Set<Object> objects, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        enqueueAll(name, new ArrayList<>(objects), caller);
    }

    /**
     * This method adds the given {@link List} of object in list specified by the input parameter <i>name</i>.
     * This list will hold the objects till condition to flush the list is met, and the callback
     * is made with all the objects present in the list.
     * @param name {@link String} containing the name of the list in which the obj needs to be stored
     * @param objects {@link List} of {@link Object} that needs to stored in a list to be processed in the future
     * @param caller {@link Object} on which the callback will be called
     * @throws ClassNotFoundException Class by the name <i>clazz</i> mentioned in the config file does not exist
     * @throws NoSuchMethodException <i>callback</i> mentioned in the config file does not exist
     */
    public static void enqueueAll(String name, List<Object> objects, Object caller) throws ClassNotFoundException, NoSuchMethodException {
        for(Object obj : objects){
            enqueue(name, obj, caller);
        }
    }
}
