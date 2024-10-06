package com.xlf.mc.xLogin.handler.command;

import com.xlf.mc.xLogin.model.entity.UserEntity;
import com.xlf.mc.xLogin.util.DatabaseManager;
import com.xlf.mc.xLogin.util.Logger;
import com.xlf.mc.xLogin.util.PasswordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

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
                String email = args[0];
                String password = args[1];
                String confirmPassword = args[2];
                if (!password.equals(confirmPassword)) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c两次输入的密码不一致！");
                }
                try {
                    UserEntity emailExist = DatabaseManager.getUserEntity().queryBuilder()
                            .where().eq("email", email).queryForFirst();
                    if (emailExist != null) {
                        sender.sendMessage(PLUGIN_PREFIX + "§c注册失败，该邮箱已被注册！");
                        return true;
                    }
                    Player getUser = (Player) sender;
                    // 获取玩家信息
                    UserEntity userExist = DatabaseManager.getUserEntity().queryBuilder()
                            .where().eq("uuid", getUser.getUniqueId().toString())
                            .or().eq("username", getUser.getName())
                            .queryForFirst();
                    if (userExist != null) {
                        sender.sendMessage(PLUGIN_PREFIX + "§c注册失败，该玩家已注册！");
                        return true;
                    }
                    UserEntity newUser = new UserEntity(
                            getUser.getUniqueId(),
                            getUser.getName(),
                            email,
                            PasswordUtil.encrypt(password),
                            null,
                            null
                    );
                    DatabaseManager.getUserEntity().create(newUser);
                } catch (SQLException e) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c注册失败，请联系管理员！（错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
                    Logger.error("错误信息：" + e.getMessage());
                }
            }
        }
        return true;
    }
}
