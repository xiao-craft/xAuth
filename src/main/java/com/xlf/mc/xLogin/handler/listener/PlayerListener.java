package com.xlf.mc.xLogin.handler.listener;

import com.xlf.mc.xLogin.model.entity.LogEntity;
import com.xlf.mc.xLogin.util.DatabaseManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 玩家监听器
 * <p>
 * 该类会在玩家登录、退出时被调用，用于监听玩家登录、退出事件；
 * 当玩家登录、退出时，记录玩家登录、退出日志。
 *
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerPreJoin(
            @NotNull AsyncPlayerPreLoginEvent event
    ) {
        boolean isAllowed = event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED;
        if (!isAllowed) {
            event.disallow(
                    event.getLoginResult(),
                    Component
                            .text("服务器已满，请稍后再试！").appendNewline()
                            .append(Component.text("请稍后再试！"))
            );
        }
    }

    @EventHandler
    public void onPlayerJoin(
            @NotNull PlayerJoinEvent event
    ) throws SQLException {
        LogEntity newLog = new LogEntity(
                event.getPlayer().getName(),
                this.getPlayerAddress(event.getPlayer().getAddress()),
                true,
                true,
                null,
                new Timestamp(System.currentTimeMillis())
        );
        DatabaseManager.getLogEntity().create(newLog);
    }

    @EventHandler
    public void onPlayerQuit(
            @NotNull PlayerQuitEvent event
    ) throws SQLException {
        LogEntity newLog = new LogEntity(
                event.getPlayer().getName(),
                this.getPlayerAddress(event.getPlayer().getAddress()),
                false,
                true,
                event.getReason().toString(),
                new Timestamp(System.currentTimeMillis())
        );
        DatabaseManager.getLogEntity().create(newLog);
    }

    private String getPlayerAddress(InetSocketAddress address) {
        String getAddress;
        if (address == null) {
            getAddress = null;
        } else {
            getAddress = address.getHostString();
        }
        return getAddress;
    }
}
