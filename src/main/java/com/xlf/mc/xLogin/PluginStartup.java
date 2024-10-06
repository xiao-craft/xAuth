package com.xlf.mc.xLogin;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.handler.command.LoginCommandHandler;
import com.xlf.mc.xLogin.handler.command.RegisterCommandHandler;
import com.xlf.mc.xLogin.handler.listener.PlayerJoinListener;
import com.xlf.mc.xLogin.handler.listener.PlayerOpreateListener;
import com.xlf.mc.xLogin.handler.task.UserLoginTask;
import com.xlf.mc.xLogin.util.Database;
import com.xlf.mc.xLogin.util.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.xlf.mc.xLogin.constant.PluginConstant.*;

/**
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class PluginStartup {
    /**
     * 初始化插件 - 获取插件配置文件
     * <p>
     * 该方法用于获取插件的配置文件，用于启动初始化插件。
     */
    public PluginStartup pluginConfigFile() {
        File coreFile = new File(mcPlugin.getDataFolder() + "/config.yml");
        if (!coreFile.exists()) {
            mcPlugin.saveResource("config.yml", false);
            Logger.warn("初始化文件不存在，已创建!");
        }
        coreConfig = YamlConfiguration.loadConfiguration(coreFile);
        isDebug = coreConfig.getBoolean("setting.debug");
        Logger.debug("初始化文件已完成!");
        return this;
    }

    /**
     * 初始化插件 - 获取插件数据库配置
     * <p>
     * 该方法用于获取插件的数据库配置，用于启动初始化插件。
     */
    public PluginStartup pluginConfigDatabase() {
        Logger.debug("初始化数据库配置...");
        String getDatabaseType = coreConfig.getString("database.type");
        String databaseUsername = coreConfig.getString("database.username");
        String databasePassword = coreConfig.getString("database.password");
        String databaseDb = coreConfig.getString("database.db");
        String getDataBasePath;
        if ("mysql".equalsIgnoreCase(getDatabaseType)) {
            getDataBasePath =
                    coreConfig.getString("database.url") + ":" + coreConfig.getString("database.port");
        } else if ("sqlite".equalsIgnoreCase(getDatabaseType)) {
            File databaseFile = new File(mcPlugin.getDataFolder() + "/mc_xauth.db");
            if (!databaseFile.exists()) {
                mcPlugin.saveResource("mc_xauth.db", false);
            }
            getDataBasePath = mcPlugin.getDataFolder() + "/mc_xauth.db";
        } else {
            throw new RuntimeException("数据库配置错误！");
        }
        Database.setupDatabase(getDatabaseType, getDataBasePath, databaseDb, databaseUsername, databasePassword);
        return this;
    }

    public PluginStartup pluginConfigListener() {
        Logger.debug("初始化监听器...");
        mcPlugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), mcPlugin);
        mcPlugin.getServer().getPluginManager().registerEvents(new PlayerOpreateListener(), mcPlugin);
        return this;
    }

    public PluginStartup pluginConfigTask() {
        Logger.debug("初始化定时任务...");
        UserLoginTask.onUserNotLoginSend();
        UserLoginTask.onNotLoginNotMove();
        UserLoginTask.onNotLoginInvincible();
        UserLoginTask.onNotLoginWith2MinutesKick();
        return this;
    }

    /**
     * 初始化插件 - 获取插件命令
     * <p>
     * 该方法用于获取插件的命令，用于启动初始化插件。
     */
    public PluginStartup pluginConfigCommand() {
        Logger.debug("初始化命令...");
        Objects.requireNonNull(mcPlugin.getCommand("register")).setExecutor(new RegisterCommandHandler());
        Objects.requireNonNull(mcPlugin.getCommand("login")).setExecutor(new LoginCommandHandler());
        return this;
    }

    /**
     * 初始化插件 - 获取插件协议
     * <p>
     * 该方法用于获取插件的协议，用于启动初始化插件。
     */
    public PluginStartup pluginConfigCache() {
        Logger.debug("初始化缓存...");
        PlayerCache.playerList = new ArrayList<>();
        return this;
    }

    /**
     * 初始化插件 - 构建插件
     * <p>
     * 该方法用于构建插件，用于启动初始化插件。
     */
    public void build() {
        Logger.info("插件初始化完成!");
    }
}
