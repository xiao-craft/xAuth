package com.xlf.mc.xLogin.util;

import com.xlf.mc.xLogin.constant.PrefixConstant;

import static com.xlf.mc.xLogin.constant.PluginConstant.isDebug;

/**
 * 这是一个日志工具类，用于记录日志
 * <p>
 * 该类用于记录插件的日志
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class Logger {

    public static void debug(String message) {
        if (isDebug) {
            java.util.logging.Logger.getLogger(PrefixConstant.PLUGIN_DEBUG_PREFIX).info(message);
        }
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
