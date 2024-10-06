package com.xlf.mc.xLogin.handler.task;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.constant.PrefixConstant;
import com.xlf.mc.xLogin.util.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.C;

import static com.xlf.mc.xLogin.constant.PluginConstant.isDebug;
import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;

/**
 * 登录消息定时任务
 * <p>
 * 该类会在玩家未登录时发送消息提醒玩家登录
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class UserLoginTask {

    /**
     * 玩家未登录时发送消息提醒玩家登录
     */
    public static void onUserNotLoginSend() {
        if (isDebug) {
            Logger.debug("初始化登录消息定时任务...");
        }
        Bukkit.getScheduler().runTaskTimer(mcPlugin, () -> {
            mcPlugin.getServer().getOnlinePlayers().forEach(player -> {
                PlayerCache.playerList.forEach(user -> {
                    if (player.getUniqueId().equals(user.getUuid())) {
                        if (!user.isLogin()) {
                            if (user.isFirstLogin()) {
                                player.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c欢迎您，请使用 §a/register <邮箱> <密码> <确认密码> §c注册！");
                            } else {
                                player.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c您还未登录，请使用 §a/login <密码> §c登录！");
                            }
                        }
                    }
                });
            });
        }, 200L, 400L);
    }

    /**
     * 玩家未登录时禁止玩家移动
     */
    public static void onNotLoginNotMove() {
        if (isDebug) {
            Logger.debug("初始化登录消息定时任务...");
        }
        Bukkit.getScheduler().runTaskTimer(mcPlugin, () -> {
            mcPlugin.getServer().getOnlinePlayers().forEach(player -> {
                PlayerCache.playerList.forEach(user -> {
                    if (player.getUniqueId().equals(user.getUuid())) {
                        if (!user.isLogin()) {
                            player.teleport(player.getLocation());
                        }
                    }
                });
            });
        }, 0L, 1L);
    }

    /**
     * 玩家未登录时禁止玩家受伤
     */
    public static void onNotLoginInvincible() {
        if (isDebug) {
            Logger.debug("初始化登录消息定时任务...");
        }
        Bukkit.getScheduler().runTaskTimer(mcPlugin, () -> {
            mcPlugin.getServer().getOnlinePlayers().forEach(player -> {
                PlayerCache.playerList.forEach(user -> {
                    if (player.getUniqueId().equals(user.getUuid())) {
                        player.setInvulnerable(!user.isLogin());
                    }
                });
            });
        }, 0L, 1L);
    }

    /**
     * 玩家未登录时超过 2 分钟踢出玩家
     */
    public static void onNotLoginWith2MinutesKick() {
        if (isDebug) {
            Logger.debug("初始化登录消息定时任务...");
        }
        Bukkit.getScheduler().runTaskTimer(mcPlugin, () -> {
            mcPlugin.getServer().getOnlinePlayers().forEach(player -> {
                PlayerCache.playerList.forEach(user -> {
                    if (player.getUniqueId().equals(user.getUuid())) {
                        if (!user.isLogin()) {
                            // 获取时间戳检查是否相差 2 分钟
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - user.getLoginTime() > 120000) {
                                player.kick(Component
                                        .text(PrefixConstant.PLUGIN_PREFIX)
                                        .append(Component.newline())
                                        .append(Component.text("§c未在指定时间内进行登录操作，已被踢出！"))
                                        .append(Component.newline())
                                        .append(Component.text("§a请重新登录！"))
                                        .append(Component.newline())
                                        .append(Component.text("§c如有疑问，请联系管理员！"))
                                );
                            }
                        }
                    }
                });
            });
        }, 0L, 400L);
    }
}
