package com.xlf.mc.xLogin.handler.command;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.config.Database;
import com.xlf.mc.xLogin.util.Logger;
import com.xlf.mc.xLogin.util.MailCodeUtil;
import com.xlf.mc.xLogin.util.PasswordUtil;
import jakarta.mail.MessagingException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;
import static com.xlf.mc.xLogin.constant.PrefixConstant.PLUGIN_PREFIX;

/**
 * 这是一个注册命令处理类，用于处理注册命令
 * <p>
 * 该类会在玩家输入注册命令时被调用，用于处理注册命令；
 * 处理注册命令的逻辑、权限等。
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class RegisterCommandHandler implements CommandExecutor {
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
            if (args.length != 3) {
                sender.sendMessage(PLUGIN_PREFIX + "§c使用方法: /register <邮箱> <密码> <确认密码>");
            } else {
                // 检查用户是否已登录
                AtomicBoolean isLogin = new AtomicBoolean(false);
                PlayerCache.playerList.forEach(user -> {
                    if (sender.getName().equals(user.getUsername())) {
                        if (user.isLogin()) {
                            sender.sendMessage(PLUGIN_PREFIX + "§a你已经登录！");
                            isLogin.set(true);
                        }
                    }
                });
                if (isLogin.get()) {
                    return true;
                }
                String email = args[0];
                String password = args[1];
                String confirmPassword = args[2];
                if (!password.equals(confirmPassword)) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c两次输入的密码不一致！");
                    return true;
                }
                try (Connection connection = Database.getConnection()) {
                    String sql = "SELECT * FROM `mc_xauth_user` WHERE `email` = ? OR `username` = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, email);
                        preparedStatement.setString(2, sender.getName());

                        // 执行查询并获取结果集
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            // 处理结果集
                            while (resultSet.next()) {
                                String userUuid = resultSet.getString("uuid");
                                if (userUuid != null) {
                                    sender.sendMessage(PLUGIN_PREFIX + "§c注册失败，该邮箱或用户已被注册！");
                                    return true;
                                }
                            }
                        }
                    }

                    // 注册玩家
                    Player getUser = (Player) sender;
                    String registerSql = "INSERT INTO `mc_xauth_user` (`uuid`, `username`, `email`, `password`) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(registerSql)) {
                        preparedStatement.setString(1, getUser.getUniqueId().toString());
                        preparedStatement.setString(2, getUser.getName());
                        preparedStatement.setString(3, email);
                        preparedStatement.setString(4, PasswordUtil.encrypt(password));

                        preparedStatement.execute();
                    }
                    // 设置用户已登录
                    sender.sendMessage(PLUGIN_PREFIX + "§a欢迎您 §e" + sender.getName() + "§a 加入这个大家庭！");
                    PlayerCache.playerList.forEach(user -> {
                        if (getUser.getUniqueId().equals(user.getUuid())) {
                            user.setLogin(true);
                        }
                    });
                    ((Player) sender).removePotionEffect(PotionEffectType.SLOWNESS);
                    Bukkit.getScheduler().runTaskAsynchronously(mcPlugin, () -> {
                        try {
                            MailCodeUtil.sendCode(email, sender);
                        } catch (MessagingException e) {
                            Bukkit.getScheduler().runTask(mcPlugin, () -> sender.sendMessage(PLUGIN_PREFIX + "§c验证码发送失败，请联系管理员！（错误码：SendMailFailed-" + System.currentTimeMillis()));
                            Logger.error("数据库操作失败，对应错误代码：SendMailFailed-" + System.currentTimeMillis());
                            Logger.error("错误信息：" + e.getMessage());
                        } catch (RuntimeException e) {
                            Bukkit.getScheduler().runTask(mcPlugin, () -> sender.sendMessage(PLUGIN_PREFIX + "§c模版获取失败，请联系管理员！（错误码： MailTemplateFailed-" + System.currentTimeMillis()));
                            Logger.error("数据库操作失败，对应错误代码：MailTemplateFailed-" + System.currentTimeMillis());
                            Logger.error("错误信息：" + e.getMessage());
                        }
                    });
                } catch (SQLException e) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c注册失败，请联系管理员！（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("错误信息：" + e.getMessage());
                }
            }
        }
        return true;
    }
}
