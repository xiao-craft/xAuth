package com.xlf.mc.xLogin.model.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * 用户实体
 * <p>
 * 该类用于存放用户的数据
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
@DatabaseTable(tableName = "mc_xauth_user")
public class UserEntity {
    @DatabaseField(generatedId = true, dataType = DataType.UUID, columnName = "uuid")
    private UUID uuid;
    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.STRING, width = 64, columnName = "username")
    private String username;
    @DatabaseField(dataType = DataType.UUID, columnName = "genuine_uuid")
    private UUID genuineUuid;
    @DatabaseField(canBeNull = false, dataType = DataType.BOOLEAN, defaultValue = "false", columnName = "is_verify")
    private boolean isVerify;
    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.STRING, width = 254, columnName = "email")
    private String email;
    @DatabaseField(canBeNull = false, dataType = DataType.BOOLEAN, defaultValue = "false", columnName = "is_email_verified")
    private boolean isEmailVerified;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING, width = 60, columnName = "password")
    private String password;
    @DatabaseField(unique = true, dataType = DataType.STRING, width = 11, columnName = "phone")
    private String phone;
    @DatabaseField(unique = true, dataType = DataType.STRING, width = 12, columnName = "qq")
    private String qq;
    @DatabaseField(canBeNull = false, dataType = DataType.BOOLEAN, defaultValue = "false", columnName = "is_auth")
    private boolean isAuth;
    @DatabaseField(canBeNull = false, dataType = DataType.TIME_STAMP, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL", columnName = "created_at")
    private Timestamp createdAt;
    @DatabaseField(columnDefinition = "TIMESTAMP", columnName = "last_login_at")
    private Timestamp lastLoginAt;

    public UserEntity() {}

    public UserEntity(UUID uuid, String username, String email, String password, String phone, String qq) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.qq = qq;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Timestamp lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public UUID getGenuineUuid() {
        return genuineUuid;
    }

    public void setGenuineUuid(UUID genuineUuid) {
        this.genuineUuid = genuineUuid;
    }
}
