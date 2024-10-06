package com.xlf.mc.xLogin.model.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Token 实体
 * <p>
 * 该类用于存放用户的 Token 数据
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
@DatabaseTable(tableName = "mc_xauth_token")
public class TokenEntity {
    @DatabaseField(generatedId = true, columnName = "token_uuid", dataType = DataType.UUID)
    private UUID token;
    @DatabaseField(canBeNull = false, columnName = "user_uuid", foreign = true, foreignColumnName = "uuid", foreignAutoRefresh = true)
    private UserEntity uuid;
    @DatabaseField(columnName = "is_login", dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean isLogin;
    @DatabaseField(columnName = "x", dataType = DataType.LONG, canBeNull = false)
    private long x;
    @DatabaseField(columnName = "y", dataType = DataType.LONG, canBeNull = false)
    private long y;
    @DatabaseField(columnName = "z", dataType = DataType.LONG, canBeNull = false)
    private long z;
    @DatabaseField(columnName = "login_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private Timestamp loginAt;
    @DatabaseField(columnName = "logout_at")
    private Timestamp logoutAt;

    public TokenEntity() {}

    public TokenEntity(UUID token, UserEntity uuid, boolean isLogin, long x, long y, long z, Timestamp loginAt, Timestamp logoutAt) {
        this.token = token;
        this.uuid = uuid;
        this.isLogin = isLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.loginAt = loginAt;
        this.logoutAt = logoutAt;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public UserEntity getUuid() {
        return uuid;
    }

    public void setUuid(UserEntity uuid) {
        this.uuid = uuid;
    }

    public Timestamp getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Timestamp loginAt) {
        this.loginAt = loginAt;
    }

    public Timestamp getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(Timestamp logoutAt) {
        this.logoutAt = logoutAt;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }
}
