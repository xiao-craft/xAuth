package com.xlf.mc.xLogin.model.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * 封禁实体
 * <p>
 * 该类用于存放封禁数据
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
@DatabaseTable(tableName = "mc_xauth_ban")
public class BanEntity {
    @DatabaseField(id = true, dataType = DataType.UUID, canBeNull = false, columnName = "uuid")
    private UUID banUuid;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, foreignColumnName = "uuid", columnName = "user_uuid")
    private UserEntity userUuid;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "uuid", columnName = "operator")
    private UserEntity operator;
    @DatabaseField(dataType = DataType.LONG_STRING, canBeNull = false, columnName = "reason")
    private String reason;
    @DatabaseField(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL", columnName = "banned_at")
    private Timestamp bannedAt;
    @DatabaseField(columnDefinition = "TIMESTAMP NOT NULL", columnName = "expired_at")
    private Timestamp expiredAt;

    public BanEntity() {}

    public BanEntity(UUID banUuid, UserEntity userUuid, UserEntity operator, String reason, Timestamp bannedAt, Timestamp expiredAt) {
        this.banUuid = banUuid;
        this.userUuid = userUuid;
        this.operator = operator;
        this.reason = reason;
        this.bannedAt = bannedAt;
        this.expiredAt = expiredAt;
    }

    public UUID getBanUuid() {
        return banUuid;
    }

    public void setBanUuid(UUID banUuid) {
        this.banUuid = banUuid;
    }

    public UserEntity getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UserEntity userUuid) {
        this.userUuid = userUuid;
    }

    public UserEntity getOperator() {
        return operator;
    }

    public void setOperator(UserEntity operator) {
        this.operator = operator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getBannedAt() {
        return bannedAt;
    }

    public void setBannedAt(Timestamp bannedAt) {
        this.bannedAt = bannedAt;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Timestamp expiredAt) {
        this.expiredAt = expiredAt;
    }
}
