package com.xlf.mc.xLogin.handler.task;

import com.xlf.mc.xLogin.model.entity.TokenEntity;
import com.xlf.mc.xLogin.model.entity.UserEntity;
import com.xlf.mc.xLogin.util.DatabaseManager;
import com.xlf.mc.xLogin.util.Logger;
import org.bukkit.Bukkit;

import java.sql.SQLException;

import static com.xlf.mc.xLogin.constant.PluginConstant.isDebug;
import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;

/**
 * 登录消息定时任务
 * <p>
 * 该类会在玩家未登录时发送消息提醒玩家登录
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class LoginMessageTask {

    /**
     * 玩家未登录时发送消息提醒玩家登录
     */
    public static void onUserNotLoginSend() {
        if (isDebug) {
            Logger.debug("初始化登录消息定时任务...");
        }
        Bukkit.getScheduler().runTaskTimer(mcPlugin, () -> {
            mcPlugin.getServer().getOnlinePlayers().forEach(player -> {
                try {
                    // 检查用户是否存在
                    UserEntity getUser = DatabaseManager.getUserEntity().queryBuilder()
                            .where().eq("uuid", player.getUniqueId()).queryForFirst();
                    if (getUser == null) {
                        player.sendMessage("§c欢迎您");
                    } else {
                        TokenEntity getToken = DatabaseManager.getTokenEntity().queryBuilder()
                                .where().eq("user_uuid", player.getUniqueId()).queryForFirst();
                        if (getToken == null) {
                            player.sendMessage("§c您还未登录，请先登录！");
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }, 200L, 400L);
    }

    public static void onNotLoginNotMove() {
        if (isDebug) {
            Logger.debug("初始化登录消息定时任务...");
        }
        Bukkit.getScheduler().runTaskTimer(mcPlugin, () -> {
            mcPlugin.getServer().getOnlinePlayers().forEach(player -> {
                try {
                    TokenEntity getUser = DatabaseManager.getTokenEntity().queryBuilder()
                            .where().eq("user_uuid", player.getUniqueId()).queryForFirst();
                    if (getUser == null) {
                        player.teleport(player.getLocation());
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }, 0L, 1L);
    }
}
