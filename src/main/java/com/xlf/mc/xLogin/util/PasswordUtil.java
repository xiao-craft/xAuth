package com.xlf.mc.xLogin.util;

import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码工具类
 * <p>
 * 用于提供密码加密、验证方法。
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class PasswordUtil {
    /**
     * 加密密码
     * <hr/>
     * 加密密码
     * <p>
     * 1. 使用SHA-256加密密码
     * 2. 使用BCrypt加密
     * 3. 返回加密后的密码
     *
     * @param password 密码
     * @return 加密后的密码
     */
    @NotNull
    public static String encrypt(String password) {
        String sha256Hash = EncryptUtil.sha256Hash(password);
        return BCrypt.hashpw(sha256Hash, BCrypt.gensalt());
    }

    /**
     * 验证密码
     * <hr/>
     * 验证密码
     * <p>
     * 1. 使用SHA-256加密密码
     * 2. 使用BCrypt验证密码
     *
     * @param password          密码
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String password, String encryptedPassword) {
        String sha256Hash = EncryptUtil.sha256Hash(password);
        return BCrypt.checkpw(sha256Hash, encryptedPassword);
    }
}
