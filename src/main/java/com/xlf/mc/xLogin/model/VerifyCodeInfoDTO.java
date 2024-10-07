package com.xlf.mc.xLogin.model;

/**
 * 验证码信息DTO
 * <p>
 * 该类用于存储验证码信息
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class VerifyCodeInfoDTO {
    private String email;
    private String verifyCode;
    private long createdAt;

    public String getEmail() {
        return email;
    }

    public VerifyCodeInfoDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public VerifyCodeInfoDTO setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
        return this;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public VerifyCodeInfoDTO setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
