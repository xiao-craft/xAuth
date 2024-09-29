package com.xlf.mc.xLogin.util;

import com.xlf.mc.xLogin.constant.PrefixConstant;
import org.slf4j.LoggerFactory;

/**
 * 这是一个日志工具类，用于记录日志
 * <p>
 * 该类用于记录插件的日志
 *
 * @since 1.0.0-SNAPSHOT
 * @version 1.0.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class Logger {

    public static void debug(String message) {
        java.util.logging.Logger.getLogger(PrefixConstant.PLUGIN_DEBUG_PREFIX).info(message);
    }

    public static void info(String message) {
        java.util.logging.Logger.getLogger(PrefixConstant.PLUGIN_INFO_PREFIX).info(message);
    }

    public static void warn(String message) {
        java.util.logging.Logger.getLogger(PrefixConstant.PLUGIN_WARNING_PREFIX).warning(message);
    }

    public static void error(String message) {
        java.util.logging.Logger.getLogger(PrefixConstant.PLUGIN_ERROR_PREFIX).warning(message);
    }
}
