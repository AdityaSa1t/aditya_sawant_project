package com.cs540.rbatch.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static Logger logger = LoggerFactory.getLogger("RBatch");

    public static void error(String message, Throwable e){
        logger.error(message, e);
    }

    public static void error(String message) {
        logger.error(message);
    }
}
