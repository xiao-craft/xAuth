package com.xlf.mc.xLogin.util;

import org.jetbrains.annotations.NotNull;

/**
 * 随机工具类
 * <p>
 * 用于提供随机工具方法。
 *
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class RandomUtil {

    /**
     * 生成随机字符串
     * <hr/>
     * 生成指定长度的随机字符串；
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static @NotNull String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
