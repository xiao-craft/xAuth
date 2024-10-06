package com.xlf.mc.xLogin;

import com.xlf.mc.xLogin.constant.PluginConstant;
import com.xlf.mc.xLogin.util.Database;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 这是一个主类，用于启动插件
 * <p>
 * 该类会在插件启动时被调用，用于启动插件；
 * 启动插件的初始化、配置、数据库连接等。
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public final class XAuth extends JavaPlugin {
    @Override
    public void onEnable() {
        // 初始化常量
        PluginConstant.mcPlugin = this;

        // 初始化插件配置文件
        PluginStartup startup = new PluginStartup();
        startup
                .pluginConfigFile()
                .pluginConfigDatabase()
                .pluginConfigListener()
                .pluginConfigTask()
                .pluginConfigCommand()
                .pluginConfigCache()
                .build();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Database.shutdown();
    }
}
