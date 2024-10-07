package com.xlf.mc.xLogin.handler.command;

import com.xlf.mc.xLogin.config.Database;
import com.xlf.mc.xLogin.constant.PrefixConstant;
import com.xlf.mc.xLogin.util.Logger;
import com.xlf.mc.xLogin.util.MailCodeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.xlf.mc.xLogin.constant.PrefixConstant.PLUGIN_PREFIX;

/**
 * 这是一个验证命令处理类，用于处理验证命令
 * <p>
 * 该类会在玩家输入验证命令时被调用，用于处理验证命令；
 * 处理验证命令的逻辑、权限等。
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class VerifyCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c控制台无法执行该命令！");
        }
        if (args.length != 1) {
            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c使用方法: /verify <邮箱验证码>");
        } else {
            String verifyCode = args[0];
            if (!verifyCode.matches("[0-9A-Za-z]{6}")) {
                sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c验证码格式错误！");
                return true;
            }
            // 获取用户数据库
            String getUserInfoSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ?;";
            try (PreparedStatement statement = Database.getConnection().prepareStatement(getUserInfoSql)) {
                statement.setString(1, sender.getName());
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String userEmail = resultSet.getString("email");
                        String userUuid = resultSet.getString("uuid");
                        if (MailCodeUtil.verifyCode(userEmail, verifyCode)) {
                            String updateSql = "UPDATE `mc_xauth_user` SET `is_verify` = 1 WHERE `uuid` = ?;";
                            try (PreparedStatement updateStatement = Database.getConnection().prepareStatement(updateSql)) {
                                updateStatement.setString(1, userUuid);
                                updateStatement.executeUpdate();
                                sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a验证成功！");
                            } catch (SQLException e) {
                                sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c验证失败，请联系管理员！（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                                Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                                Logger.error("错误信息：" + e.getMessage());
                            }
                        } else {
                            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c验证失败！");
                        }
                    } else {
                        sender.sendMessage(PLUGIN_PREFIX + "§c该用户未注册");
                    }
                }
            } catch (SQLException e) {
                sender.sendMessage(PLUGIN_PREFIX + "§c登录失败，请联系管理员！（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("错误信息：" + e.getMessage());
            }
        }
        return true;
    }
}
