package com.xlf.mc.xLogin.handler.command;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.util.Database;
import com.xlf.mc.xLogin.util.Logger;
import com.xlf.mc.xLogin.util.PasswordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.xlf.mc.xLogin.constant.PrefixConstant.PLUGIN_PREFIX;

/**
 * 这是一个登录命令处理类，用于处理登录命令
 * <p>
 * 该类会在玩家输入登录命令时被调用，用于处理登录命令；
 * 处理登录命令的逻辑、权限等。
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class LoginCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(PLUGIN_PREFIX + "§c控制台无法执行该命令！");
        } else {
            if (args.length != 1) {
                sender.sendMessage(PLUGIN_PREFIX + "§c使用方法: /login <密码>");
            } else {
                String password = args[0];
                // 从数据库获取用户信息
                String getUserInfoSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ?;";
                try (PreparedStatement statement = Database.getConnection().prepareStatement(getUserInfoSql)) {
                    statement.setString(1, sender.getName());
                    try (var resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String userPassword = resultSet.getString("password");
                            if (PasswordUtil.verify(password, userPassword)) {
                                sender.sendMessage(PLUGIN_PREFIX + "§a欢迎回来 §e" + sender.getName() + "§a！");
                                PlayerCache.playerList.forEach(user -> {
                                    if (sender.getName().equals(user.getUsername())) {
                                        user.setLogin(true);
                                    }
                                });
                                ((Player) sender).removePotionEffect(PotionEffectType.SLOWNESS);
                            } else {
                                sender.sendMessage(PLUGIN_PREFIX + "§c密码错误！");
                            }
                        } else {
                            sender.sendMessage(PLUGIN_PREFIX + "§c该用户未注册，请先注册！");
                        }
                    }
                } catch (SQLException e) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c登录失败，请联系管理员！（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("错误信息：" + e.getMessage());
                }
            }
        }
        return true;
    }
}
