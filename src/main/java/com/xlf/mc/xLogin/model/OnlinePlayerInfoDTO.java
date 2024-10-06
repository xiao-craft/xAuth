package com.xlf.mc.xLogin.model;

import java.util.UUID;

/**
 * 在线玩家信息数据传输对象
 * <p>
 * 该类用于传输在线玩家的信息
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class OnlinePlayerInfoDTO {
    private UUID uuid;
    private String username;
    private boolean isLogin;
    private long loginTime;
    private boolean isFirstLogin;

    public long getLoginTime() {
        return loginTime;
    }

    public OnlinePlayerInfoDTO setLoginTime(long loginTime) {
        this.loginTime = loginTime;
        return this;
    }


    public UUID getUuid() {
        return uuid;
    }

    public OnlinePlayerInfoDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public OnlinePlayerInfoDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public OnlinePlayerInfoDTO setLogin(boolean login) {
        isLogin = login;
        return this;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public OnlinePlayerInfoDTO setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
        return this;
    }
}
