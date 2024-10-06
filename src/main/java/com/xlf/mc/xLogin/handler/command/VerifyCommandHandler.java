package com.xlf.mc.xLogin.handler.command;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.xlf.mc.xLogin.model.MinecraftVerifyDTO;
import com.xlf.mc.xLogin.util.Database;
import com.xlf.mc.xLogin.util.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            sender.sendMessage(PLUGIN_PREFIX + "§c控制台无法执行该命令！");
        }
        String getUserSql = "SELECT * FROM `mc_xauth_user` WHERE `uuid` = ?;";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(getUserSql)) {
            statement.setString(1, ((Player) sender).getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String getGenuineUuid = resultSet.getString("genuine_uuid");
                if (getGenuineUuid != null) {
                    sender.sendMessage(PLUGIN_PREFIX + "§c您的账号已经验证过了！");
                    return true;
                }
                // 向 API 发送验证请求
                this.sendVerifyRequest(sender);
            }
        } catch (SQLException e) {
            sender.sendMessage(PLUGIN_PREFIX + "§c操作失败，请联系管理员！（错误码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("数据库操作失败，对应错误代码：DatabaseOperationFailed-" + System.currentTimeMillis());
            Logger.error("错误信息：" + e.getMessage());
        }
        return true;
    }

    private void sendVerifyRequest(@NotNull CommandSender sender) {
        // 向 API 发送验证请求
        try {
            URL getUrl = new URI("https://playerdb.co/api/player/minecraft/" + sender.getName()).toURL();
            HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            if (conn.getResponseCode() == 200) {
                Logger.debug("网络API操作成功，对应状态码：" + conn.getResponseCode());
                StringBuilder responseBody = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBody.append(inputLine);
                }
                MinecraftVerifyDTO minecraftVerifyDTO = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
                        .fromJson(responseBody.toString(), MinecraftVerifyDTO.class);
                if (minecraftVerifyDTO.isSuccess()) {
                    sender.sendMessage(PLUGIN_PREFIX + "§a验证成功！");
                } else {
                    sender.sendMessage(PLUGIN_PREFIX + "§c该账号不存在正版。");
                }
            } else {
                sender.sendMessage(PLUGIN_PREFIX + "§c验证失败，请联系管理员！（错误码：NetOperationFailed-" + System.currentTimeMillis());
                Logger.error("网络API操作失败，对应错误代码：NetOperationFailed-" + System.currentTimeMillis());
                Logger.error("错误信息：" + conn.getResponseMessage());
            }
        } catch (URISyntaxException | IOException e) {
            sender.sendMessage(PLUGIN_PREFIX + "§c验证失败，请联系管理员！（错误码：NetOperationFailed-" + System.currentTimeMillis());
            Logger.error("网络API操作失败，对应错误代码：NetOperationFailed-" + System.currentTimeMillis());
            Logger.error("错误信息：" + e.getMessage());
        }
    }
}
