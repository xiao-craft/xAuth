package com.xlf.mc.xLogin.handler.command;


import com.xlf.mc.xLogin.constant.CommonConstant;
import com.xlf.mc.xLogin.constant.PrefixConstant;
import com.xlf.mc.xLogin.handler.operate.XAuthUserOperate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * 这是一个xAuth命令处理类，用于处理xAuth命令
 * <p>
 * 该类会在玩家(拥有权限的玩家)输入xAuth命令时被调用，用于处理xAuth命令；
 * 处理xAuth命令的逻辑、权限等。
 *
 * @author xiao_lfeng
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class XAuthCommandHandler implements CommandExecutor {

    /**
     * 处理xAuth命令
     *
     * @param sender  命令发送者
     * @param command 命令
     * @param label   标签
     * @param args    参数
     * @return 是否成功
     */
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!sender.hasPermission("xauth.admin")) {
            sender.sendMessage("§c你没有权限执行该命令！");
            return true;
        }
        if (args.length == 0) {
            this.getHelp(sender, 1);
            return true;
        }
        XAuthUserOperate xAuthUserOperate = new XAuthUserOperate();
        switch (args[0]) {
            case "help": {
                try {
                    if (args[1].isEmpty()) {
                        this.getHelp(sender, 2);
                        return true;
                    }
                    int page = Integer.parseInt(args[1]);
                    this.getHelp(sender, page);
                } catch (NumberFormatException e) {
                    sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c请输入正确的页码！");
                }
                break;
            }
            case "reload": {
                xAuthUserOperate.reloadConfig(sender);
                break;
            }
            case "user": {
                if (args.length == 1) {
                    sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                    sender.sendMessage("§e/xauth user create <用户名> <邮箱> <密码> §7- 注册一个新的用户");
                    sender.sendMessage("§e/xauth user passwd <用户名> [新密码] §7- 重置一个新用户的密码");
                    sender.sendMessage("§e/xauth user email <用户名> <新邮箱> §7- 重置一个新用户的邮箱");
                    sender.sendMessage("§e/xauth user verify <用户名> §7- 验证一个用户");
                    sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                    return true;
                }
                switch (args[1]) {
                    case "create": {
                        xAuthUserOperate.createUser(sender, args);
                        break;
                    }
                    case "passwd": {
                        xAuthUserOperate.resetPassword(sender, args);
                        break;
                    }
                    case "email": {
                        xAuthUserOperate.resetEmail(sender, args);
                        break;
                    }
                    case "verify": {
                        xAuthUserOperate.verifyUser(sender, args);
                        break;
                    }
                    default: {
                        sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c参数错误！");
                    }
                }
                break;
            }
            default: {
                sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c参数错误！");
            }
        }
        return true;
    }

    /**
     * 获取帮助
     *
     * @param sender 命令发送者
     * @param page   页码
     */
    private void getHelp(@NotNull CommandSender sender, int page) {
        switch (page) {
            case 1: {
                sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                sender.sendMessage("§e/xauth <操作> [选项] §7- 执行xAuth操作");
                sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                sender.sendMessage("§6名字：§d" + CommonConstant.PLUGIN_NAME);
                sender.sendMessage("§6版本：§d" + CommonConstant.PLUGIN_VERSION);
                sender.sendMessage("§6作者：§d" + CommonConstant.PLUGIN_AUTHOR);
                sender.sendMessage("§6描述：§d" + CommonConstant.PLUGIN_DESCRIPTION);
                sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                sender.sendMessage("§91 / 2");
                break;
            }
            case 2: {
                sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                sender.sendMessage("§e/xauth help [页码] §7- 查看帮助");
                sender.sendMessage("§e/xauth user <操作> §7- 用户相关操作");
                sender.sendMessage("§e/xauth reload §7- 重载插件配置");
                sender.sendMessage("§2§l§m=]=============§f §a§lxAuth §2§l§m=============[=");
                sender.sendMessage("§92 / 2");
                break;
            }
            default: {
                sender.sendMessage(PrefixConstant.PLUGIN_PREFIX + "§c该页面不存在！");
            }
        }
    }
}
