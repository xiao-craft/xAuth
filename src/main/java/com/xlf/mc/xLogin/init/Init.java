package com.xlf.mc.xLogin.init;

import com.xlf.mc.xLogin.constant.PluginConstant;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 这是一个初始化类，用于初始化插件
 * <p>
 * 该类会在插件启动时被调用，用于初始化插件；
 * 初始化插件的配置、数据库连接等。
 *
 * @author xiao_lfeng
 * @version 1.0.0-SNAPSHOT
 * @since 1.0.0-SNAPSHOT
 */
public class Init {
    private final JavaPlugin javaPlugin;

    public Init(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void setup() {
        // 初始化常量
        PluginConstant.mcPlugin = this.javaPlugin;

        // 初始化插件配置文件
        PluginInit plugin = new PluginInit();
        plugin.pluginConfigFile();
        plugin.pluginConfigCommand();
    }
}
