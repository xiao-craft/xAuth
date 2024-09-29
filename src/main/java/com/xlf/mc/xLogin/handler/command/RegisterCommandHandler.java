package com.xlf.mc.xLogin.handler.command;

import com.xlf.mc.xLogin.constant.PrefixConstant;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import static com.xlf.mc.xLogin.constant.PrefixConstant.PLUGIN_PREFIX;

/**
 * 这是一个注册命令处理类，用于处理注册命令
 * <p>
 * 该类会在玩家输入注册命令时被调用，用于处理注册命令；
 * 处理注册命令的逻辑、权限等。
 *
 * @since 1.0.0-SNAPSHOT
 * @version 1.0.0-SNAPSHOT
 * @author xiao_lfeng
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
            sender.sendMessage(PLUGIN_PREFIX + "§c该命令暂未开放！");
        }
        return true;
    }
}
