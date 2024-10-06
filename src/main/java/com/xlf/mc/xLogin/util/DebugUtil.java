package com.xlf.mc.xLogin.util;

import static com.xlf.mc.xLogin.constant.PluginConstant.isDebug;

/**
 * 调试工具类
 * <p>
 * 该类用于输出调试信息
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class DebugUtil {

    public static void debug(Object message) {
        if (isDebug) {
            Logger.debug(message.toString());
        }
    }
}
