package com.xlf.mc.xLogin.init;

import com.xlf.mc.xLogin.handler.command.RegisterCommandHandler;
import com.xlf.mc.xLogin.util.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

import static com.xlf.mc.xLogin.constant.PluginConstant.*;

/**
 * @author xiao_lfeng
 * @version 1.0.0-SNAPSHOT
 * @since 1.0.0-SNAPSHOT
 */
public class PluginInit {
    /**
     * 初始化插件 - 获取插件配置文件
     * <p>
     * 该方法用于获取插件的配置文件，用于启动初始化插件。
     */
    public void pluginConfigFile() {
        File coreFile = new File(mcPlugin.getDataFolder() + "/config.yml");
        if (!coreFile.exists()) {
            mcPlugin.saveResource("config.yml", false);
            Logger.info("初始化文件不存在，已创建!");
        }
        coreConfig = YamlConfiguration.loadConfiguration(coreFile);
        isDebug = coreConfig.getBoolean("setting.debug");
        if (isDebug) {
            Logger.debug("初始化文件已完成!");
        }
    }

    public void pluginConfigCommand() {
        Objects.requireNonNull(mcPlugin.getCommand("register")).setExecutor(new RegisterCommandHandler());
    }
}
