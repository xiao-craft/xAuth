package com.xlf.mc.xLogin.handler.listener;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.constant.PrefixConstant;
import com.xlf.mc.xLogin.model.OnlinePlayerInfoDTO;
import com.xlf.mc.xLogin.util.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;

/**
 * 玩家登录监听器
 * <p>
 * 该类会在玩家登录、退出时被调用，用于监听玩家登录、退出事件；
 * 当玩家登录、退出时，记录玩家登录、退出日志。
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class PlayerJoinListener implements Listener {

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
            return;
        }
        // 添加在线列表
        PlayerCache.playerList.add(new OnlinePlayerInfoDTO()
                .setUuid(event.getUniqueId())
                .setUsername(event.getName())
                .setLoginTime(System.currentTimeMillis())
                .setFirstLogin(true)
                .setLogin(false));
        // 从数据库读取用户信息
        String prepareSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ?;";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(prepareSql)) {
            statement.setString(1, event.getName());
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // 是否存在玩家信息
                    if (resultSet.getString("uuid") != null) {
                        PlayerCache.playerList.forEach(user -> {
                            if (event.getUniqueId().equals(user.getUuid())) {
                                user.setFirstLogin(false);
                            }
                        });
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(mcPlugin, () -> {
            String prepareSql = "INSERT INTO `mc_xauth_log` (`username`, `ip`, `type`, `success`, `reason`, `current_time`) VALUES (?, ?, ?, ?, ?, ?);";
            try (PreparedStatement statement = Database.getConnection().prepareStatement(prepareSql)) {
                statement.setString(1, event.getPlayer().getName());
                statement.setString(2, this.getPlayerAddress(event.getPlayer().getAddress()));
                statement.setBoolean(3, true);
                statement.setBoolean(4, true);
                statement.setString(5, null);
                statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        // 为玩家添加缓效果
        event.getPlayer().addPotionEffect(PotionEffectType.SLOWNESS.createEffect(999999, 255));
        // 检查玩家是否第一次进入服务器
        PlayerCache.playerList.forEach(user -> {
            if (event.getPlayer().getUniqueId().equals(user.getUuid())) {
                if (user.isFirstLogin()) {
                    event.getPlayer().showTitle(Title.title(
                            Component.text("§a欢迎加入这个大家庭！"),
                            Component.text("§a请先注册账号吧~")
                    ));
                    event.getPlayer().sendActionBar(Component
                            .text(PrefixConstant.PLUGIN_PREFIX)
                            .append(Component.text("§a使用 §9/register <邮箱> <密码> <确认密码> §a注册账号")));
                } else {
                    event.getPlayer().showTitle(Title.title(
                            Component.text("§a欢迎回来 §e" + event.getPlayer().getName() + "§a！"),
                            Component.text("§a请先登录账号吧~")
                    ));
                    event.getPlayer().sendActionBar(Component
                            .text(PrefixConstant.PLUGIN_PREFIX)
                            .append(Component.text("§a使用 §9/login <密码> §a登录账号")));
                }
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Bukkit.getScheduler().runTask(mcPlugin, () -> {
            String prepareSql = "INSERT INTO `mc_xauth_log` (`username`, `ip`, `type`, `success`, `reason`, `current_time`) VALUES (?, ?, ?, ?, ?, ?);";
            try (PreparedStatement statement = Database.getConnection().prepareStatement(prepareSql)) {
                statement.setString(1, event.getPlayer().getName());
                statement.setString(2, this.getPlayerAddress(event.getPlayer().getAddress()));
                statement.setBoolean(3, false);
                statement.setBoolean(4, true);
                statement.setString(5, event.getReason().toString());
                statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        // 移除在线列表
        PlayerCache.playerList.removeIf(player -> player.getUuid().equals(event.getPlayer().getUniqueId()));
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
