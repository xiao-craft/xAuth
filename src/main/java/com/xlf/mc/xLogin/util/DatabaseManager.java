package com.xlf.mc.xLogin.util;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.xlf.mc.xLogin.model.entity.BanEntity;
import com.xlf.mc.xLogin.model.entity.LogEntity;
import com.xlf.mc.xLogin.model.entity.TokenEntity;
import com.xlf.mc.xLogin.model.entity.UserEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.UUID;

/**
 * 数据库管理器
 * <p>
 * 该类用于管理数据库连接和表结构
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class DatabaseManager {
    private static JdbcPooledConnectionSource connectionSource;

    // 定义 DAO（数据访问对象）
    private static Dao<UserEntity, UUID> userEntities;
    private static Dao<BanEntity, UUID> banEntities;
    private static Dao<TokenEntity, UUID> tokenEntities;
    private static Dao<LogEntity, Long> logEntities;

    // 初始化数据库连接和表结构
    public static void setupDatabase(String dbType, String dbPath, String database, String dbUsername, String dbPassword) {
        try {
            if ("mysql".equalsIgnoreCase(dbType)) {
                connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://" + dbPath + "/" + database, dbUsername, dbPassword);
            } else if ("sqlite".equalsIgnoreCase(dbType)) {
                connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + dbPath);
            }

            // 初始化 DAO
            userEntities = DaoManager.createDao(connectionSource, UserEntity.class);
            banEntities = DaoManager.createDao(connectionSource, BanEntity.class);
            tokenEntities = DaoManager.createDao(connectionSource, TokenEntity.class);
            logEntities = DaoManager.createDao(connectionSource, LogEntity.class);

            // 创建表结构（如果表不存在）
            TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, BanEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, TokenEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, LogEntity.class);

        } catch (SQLSyntaxErrorException | CommunicationsException e) {
            Logger.debug("数据库连接失败：" + e.getMessage());
            if ("mysql".equalsIgnoreCase(dbType)) {
                try {
                    connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://" + dbPath, dbUsername, dbPassword);
                    Connection connection = connectionSource.getReadWriteConnection(null).getUnderlyingConnection();
                    Statement stmt = connection.createStatement();
                    stmt.execute("CREATE DATABASE IF NOT EXISTS `" + database + "`");
                    stmt.close();
                    Logger.warn("已创建 " + database + " 数据库.");
                    setupDatabase(dbType, dbPath, database, dbUsername, dbPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException("数据库连接失败：" + ex.getMessage());
                }
            } else {
                throw new RuntimeException("数据库连接失败：" + e.getMessage());
            }
        } catch (SQLException e) {
            Logger.error("数据库连接失败：" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 关闭数据库连接
    public static void shutdown() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
                Logger.info("数据库连接已关闭.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Dao<UserEntity, UUID> getUserEntity() {
        return userEntities;
    }

    public static Dao<BanEntity, UUID> getBanEntity() {
        return banEntities;
    }

    public static Dao<TokenEntity, UUID> getTokenEntity() {
        return tokenEntities;
    }

    public static Dao<LogEntity, Long> getLogEntity() {
        return logEntities;
    }
}