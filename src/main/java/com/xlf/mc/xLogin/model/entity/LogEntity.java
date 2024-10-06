package com.xlf.mc.xLogin.model.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * 日志实体
 * <p>
 * 该类用于存放日志数据
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
@DatabaseTable(tableName = "mc_xauth_log")
public class LogEntity {
    @DatabaseField(generatedId = true, canBeNull = false, columnName = "log_id", dataType = DataType.LONG)
    private long logId;
    @DatabaseField(canBeNull = false, columnName = "username", dataType = DataType.STRING)
    private String username;
    @DatabaseField(columnName = "ip", dataType = DataType.STRING)
    private String ip;
    @DatabaseField(canBeNull = false, columnName = "type", dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean type;
    @DatabaseField(canBeNull = false, columnName = "success", dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean success;
    @DatabaseField(columnName = "reason", dataType = DataType.STRING)
    private String reason;
    @DatabaseField(columnName = "current_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private Timestamp currentTime;

    public LogEntity() {
    }

    public LogEntity(String username, String ip, boolean type, boolean success, String reason, Timestamp currentTime) {
        this.username = username;
        this.ip = ip;
        this.type = type;
        this.success = success;
        this.reason = reason;
        this.currentTime = currentTime;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Timestamp currentTime) {
        this.currentTime = currentTime;
    }
}
