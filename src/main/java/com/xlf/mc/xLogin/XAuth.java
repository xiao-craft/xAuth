package com.xlf.mc.xLogin;

import com.xlf.mc.xLogin.init.Init;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * 这是一个主类，用于启动插件
 * <p>
 * 该类会在插件启动时被调用，用于启动插件；
 * 启动插件的初始化、配置、数据库连接等。
 *
 * @author xiao_lfeng
 * @version 1.0.0-SNAPSHOT
 * @since 1.0.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public final class XAuth extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().setLevel(Level.FINEST);
        new Init(this).setup();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
