package org.icannt.netherendingores.lib;

import org.icannt.netherendingores.NetherendingOres;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ICannt on -.
 */
public class NeoLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetherendingOres.MOD_NAME);

    public static void always(String msg) {
        LOGGER.info(msg);
    }

    public static void trace(String msg) {
        LOGGER.trace(msg);
    }

    public static void debug(String msg) {
        LOGGER.debug(msg);
    }

    public static void info(String msg) {
        LOGGER.info(msg);
    }

    public static void warn(String msg) {
        LOGGER.warn(msg);
    }

    public static void error(String msg) {
        LOGGER.error(msg);
    }

    public static void error(String msg, Exception e1) {
        LOGGER.error(msg, e1);
    }

    public static void fatal(String msg) {
        LOGGER.error(msg);
    }

    public static void exception(String msg, Throwable t) {
        LOGGER.error(msg, t);
    }

}
