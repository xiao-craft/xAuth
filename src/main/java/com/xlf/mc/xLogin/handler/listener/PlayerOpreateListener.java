package com.xlf.mc.xLogin.handler.listener;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.constant.PrefixConstant;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * 玩家操作监听器
 * <p>
 * 该类用于监听玩家操作，如玩家未登录时禁止玩家操作
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class PlayerOpreateListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserNotLoginToDeniedUserCommand(
            PlayerCommandPreprocessEvent event
    ) {
        PlayerCache.playerList.forEach(user -> {
            if (event.getPlayer().getUniqueId().equals(user.getUuid())) {
                if (!user.isLogin()) {
                    // 检查指令是否使用的是 /login 或 /register
                    if (!event.getMessage().startsWith("/login")
                            && !event.getMessage().startsWith("/register")
                            && !event.getMessage().startsWith("/l")
                            && !event.getMessage().startsWith("/r")
                            && !event.getMessage().startsWith("/reg")
                    ) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c 您还未登录，不可使用指令！");
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserNotLoginToDeniedChat(
            AsyncChatEvent event
    ) {
        PlayerCache.playerList.forEach(user -> {
            if (event.getPlayer().getUniqueId().equals(user.getUuid())) {
                if (!user.isLogin()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c 您还未登录，不可发送消息！");
                }
            }
        });
    }

    @EventHandler
    public void onUserNotLoginToDeniedDestroyAndPlaceBlock(
            PlayerInteractEvent event
    ) {
        PlayerCache.playerList.forEach(user -> {
            if (event.getPlayer().getUniqueId().equals(user.getUuid())) {
                if (!user.isLogin()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c 您还未登录，不可破坏或放置方块！");
                }
            }
        });
    }
}
