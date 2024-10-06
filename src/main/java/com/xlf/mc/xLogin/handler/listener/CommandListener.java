package com.xlf.mc.xLogin.handler.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * 指令监听器
 * <p>
 * 该类会在玩家输入命令时被调用，用于监听玩家命令；
 * 当玩家未登录时，拦截玩家命令。
 *
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserNotLoginToDeniedUserCommand(
            PlayerCommandPreprocessEvent event
    ) {
        // 检查玩家是否登录
        if (false) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c您还未登录，请先登录！");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserNotLoginToDeniedChat(
            AsyncChatEvent event
    ) {
        if (false) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c您还未登录，请先登录！");
        }
    }
}
