package com.xlf.mc.xLogin.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.xlf.mc.xLogin.constant.PluginConstant.isDebug;
import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;

/**
 * 数据库管理器
 * <p>
 * 该类用于管理数据库连接和表结构
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class Database {
    private static String connectionSource;
    private static Connection connection;
    private static String dbType;
    private static String dbUsername;
    private static String dbPassword;

    // 初始化数据库连接和表结构
    public static void setupDatabase(String dbType, String dbPath, String database, String dbUsername, String dbPassword) {
        Database.dbType = dbType;
        Database.dbUsername = dbUsername;
        Database.dbPassword = dbPassword;
        if ("mysql".equalsIgnoreCase(dbType)) {
            connectionSource = "jdbc:mysql://" + dbPath + "/" + database;
        } else if ("sqlite".equalsIgnoreCase(dbType)) {
            // 获取插件配置目录
            connectionSource = "jdbc:sqlite:"+ mcPlugin.getDataFolder() +"/mc_xauth.db";
            Logger.debug("数据库连接地址：" + connectionSource);
        }

        // 连接数据库
        try {
            if ("mysql".equalsIgnoreCase(dbType)) {
                connection = DriverManager.getConnection(connectionSource, dbUsername, dbPassword);
            } else if ("sqlite".equalsIgnoreCase(dbType)) {
                connection = DriverManager.getConnection(connectionSource);
            }
        } catch (SQLException e) {
            Logger.warn("数据库连接失败！");
        }

        // 创建数据表结构
        URL resource = Database.class.getClassLoader().getResource("sql");
        if (resource != null && "jar".equals(resource.getProtocol())) {
            try {
                // 打开 JAR 连接
                JarURLConnection jarUrlConnection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = jarUrlConnection.getJarFile();

                // 遍历 JAR 文件中的所有条目（包括文件和目录）
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (!entryName.matches("sql/.*\\.sql")) {
                        continue;
                    }
                    String newName = entryName
                            .replaceAll("sql/", "")
                            .replaceAll("\\.sql", "");
                    if (!checkDatabaseHasThisTable(newName)) {
                        if (entryName.startsWith("sql/") && entryName.endsWith(".sql")) {
                            // 读取 SQL 文件
                            Logger.debug("正在读取 JAR 文件中的 SQL 文件：" + entryName);
                            InputStream inputStream = Database.class.getClassLoader().getResourceAsStream(entryName);
                            if (inputStream != null) {
                                String sql = new String(inputStream.readAllBytes());
                                // 执行 SQL 语句
                                connection.createStatement().execute(sql);
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                    }
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Logger.warn("在 JAR 文件中找不到资源或未找到资源！");
            throw new RuntimeException("在 JAR 文件中找不到资源或未找到资源！");
        }
    }

    private static boolean checkDatabaseHasThisTable(String tableName) {
        try {
            return connection.getMetaData().getTables(null, null, tableName, null).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 关闭数据库连接
    public static void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.warn("数据库连接关闭失败！");
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                if (isDebug) {
                    Logger.debug("数据库连接已关闭，正在重新连接...");
                }
                if ("mysql".equalsIgnoreCase(dbType)) {
                    connection = DriverManager.getConnection(connectionSource, dbUsername, dbPassword);
                } else if ("sqlite".equalsIgnoreCase(dbType)) {
                    connection = DriverManager.getConnection(connectionSource);
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}