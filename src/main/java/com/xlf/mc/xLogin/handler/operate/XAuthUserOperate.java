package com.xlf.mc.xLogin.handler.operate;

import com.xlf.mc.xLogin.config.Database;
import com.xlf.mc.xLogin.constant.PrefixConstant;
import com.xlf.mc.xLogin.util.Logger;
import com.xlf.mc.xLogin.util.PasswordUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;
import static com.xlf.mc.xLogin.constant.PrefixConstant.PLUGIN_PREFIX;

/**
 * 这是一个xAuth用户操作类，用于处理xAuth用户操作
 * <p>
 * 该类会在玩家(拥有权限的玩家)输入xAuth命令时被调用，用于处理xAuth用户操作；
 * 处理xAuth用户操作的逻辑、权限等。
 *
 * @author xiao_lfeng
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class XAuthUserOperate {

    /**
     * 重载插件配置
     *
     * @param sender 命令发送者
     */
    public void reloadConfig(@NotNull CommandSender sender) {
        mcPlugin.reloadConfig();
        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a插件配置重载成功！");
    }

    /**
     * 创建用户
     * <p>
     * 该方法用于创建用户，用于管理员创建用户。
     * 该方法会在管理员执行创建用户命令时被调用。
     *
     * @param sender 命令发送者
     * @param args   参数
     */
    public void createUser(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 5) {
            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c使用方法: /xauth user create <用户名> <邮箱> <密码>");
            return;
        }
        String username = args[2];
        String password = args[4];
        String email = args[3];
        String isUserExistSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ? OR `email` = ?;";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(isUserExistSql)) {
            statement.setString(1, username);
            statement.setString(2, email);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c用户已存在！");
                    return;
                }
                // 创建用户
                String calcUserUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes()).toString();
                String createUserSql = "INSERT INTO `mc_xauth_user` (`uuid`, `username`, `password`, `email`) VALUES (?, ?, ?, ?);";
                try (PreparedStatement createStatement = Database.getConnection().prepareStatement(createUserSql)) {
                    createStatement.setString(1, calcUserUuid);
                    createStatement.setString(2, username);
                    createStatement.setString(3, PasswordUtil.encrypt(password));
                    createStatement.setString(4, email);
                    createStatement.executeUpdate();
                    sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a用户创建成功！");
                } catch (SQLException e) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("错误信息：" + e.getMessage());
                }
            } catch (SQLException e) {
                sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("错误信息：" + e.getMessage());
            }
        } catch (SQLException e) {
            sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("错误信息：" + e.getMessage());
        }
    }

    /**
     * 重置密码
     * <p>
     * 该方法用于重置用户密码，用于管理员重置用户密码。
     * 该方法会在管理员执行重置密码命令时被调用。
     *
     * @param sender 命令发送者
     * @param args   参数
     */
    public void resetPassword(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length < 3) {
            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c使用方法: /xauth user passwd <用户名> [新密码]");
            return;
        }
        String username = args[2];
        String password = args.length == 4 ? args[3] : null;
        String getUserInfoSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ?;";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(getUserInfoSql)) {
            statement.setString(1, username);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userUuid = resultSet.getString("uuid");
                    if (password == null) {
                        password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a新密码为：§d" + password);
                    }
                    String resetPasswordSql = "UPDATE `mc_xauth_user` SET `password` = ? WHERE `uuid` = ?;";
                    try (PreparedStatement resetStatement = Database.getConnection().prepareStatement(resetPasswordSql)) {
                        resetStatement.setString(1, PasswordUtil.encrypt(password));
                        resetStatement.setString(2, userUuid);
                        resetStatement.executeUpdate();
                        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a密码重置成功！");
                    } catch (SQLException e) {
                        sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                        Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                        Logger.error("错误信息：" + e.getMessage());
                    }
                } else {
                    sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c用户不存在！");
                }
            } catch (SQLException e) {
                sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("错误信息：" + e.getMessage());
            }
        } catch (SQLException e) {
            sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("错误信息：" + e.getMessage());
        }
    }

    /**
     * 重置邮箱
     * <p>
     * 该方法用于重置用户邮箱，用于管理员重置用户邮箱。
     * 该方法会在管理员执行重置邮箱命令时被调用。
     *
     * @param sender 命令发送者
     * @param args   参数
     */
    public void resetEmail(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 4) {
            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c使用方法: /xauth user email <用户名> <新邮箱>");
            return;
        }
        String username = args[2];
        String email = args[3];
        String getUserInfoSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ?;";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(getUserInfoSql)) {
            statement.setString(1, username);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userUuid = resultSet.getString("uuid");
                    String resetEmailSql = "UPDATE `mc_xauth_user` SET `email` = ?, `is_verify` = 0 WHERE `uuid` = ?;";
                    try (PreparedStatement resetStatement = Database.getConnection().prepareStatement(resetEmailSql)) {
                        resetStatement.setString(1, email);
                        resetStatement.setString(2, userUuid);
                        resetStatement.executeUpdate();
                        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a邮箱重置成功！");
                    } catch (SQLException e) {
                        sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                        Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                        Logger.error("错误信息：" + e.getMessage());
                    }
                } else {
                    sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c用户不存在！");
                }
            } catch (SQLException e) {
                sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("错误信息：" + e.getMessage());
            }
        } catch (SQLException e) {
            sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("错误信息：" + e.getMessage());
        }
    }

    /**
     * 验证用户
     * <p>
     * 该方法用于验证用户，用于管理员验证用户。
     * 该方法会在管理员执行验证用户命令时被调用。
     *
     * @param sender 命令发送者
     * @param args   参数
     */
    public void verifyUser(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 3) {
            sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c使用方法: /xauth user verify <用户名>");
            return;
        }
        String username = args[2];
        String getUserInfoSql = "SELECT * FROM `mc_xauth_user` WHERE `username` = ?;";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(getUserInfoSql)) {
            statement.setString(1, username);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userUuid = resultSet.getString("uuid");
                    if (resultSet.getInt("is_verify") == 1) {
                        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c用户已验证！");
                        return;
                    }
                    String updateSql = "UPDATE `mc_xauth_user` SET `is_verify` = 1 WHERE `uuid` = ?;";
                    try (PreparedStatement updateStatement = Database.getConnection().prepareStatement(updateSql)) {
                        updateStatement.setString(1, userUuid);
                        updateStatement.executeUpdate();
                        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§a用户验证成功！");
                    } catch (SQLException e) {
                        sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                        Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                        Logger.error("错误信息：" + e.getMessage());
                    }
                } else {
                    sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c用户不存在！");
                }
            } catch (SQLException e) {
                sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                Logger.error("错误信息：" + e.getMessage());
            }
        } catch (SQLException e) {
            sender.sendMessage(PLUGIN_PREFIX + "§c操作失败（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("错误信息：" + e.getMessage());
        }
    }
}
