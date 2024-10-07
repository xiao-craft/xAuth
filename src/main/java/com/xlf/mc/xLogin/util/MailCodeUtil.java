package com.xlf.mc.xLogin.util;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.config.Mail;
import com.xlf.mc.xLogin.constant.CommonConstant;
import com.xlf.mc.xLogin.model.VerifyCodeInfoDTO;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;
import static com.xlf.mc.xLogin.constant.PrefixConstant.PLUGIN_PREFIX;

/**
 * 邮箱验证码工具类
 * <p>
 * 该类用于发送邮箱验证码，用于验证邮箱。
 * 该类会在玩家注册、修改邮箱时被调用，用于发送邮箱验证码。
 *
 * @author xiao_lfeng
 * @version v1.0-SNAPSHOT
 * @since v1.0-SNAPSHOT
 */
public class MailCodeUtil {
    /**
     * 发送邮箱验证码
     *
     * @param email  邮箱
     * @param sender 命令发送者
     * @throws MessagingException 邮件异常
     */
    public static void sendCode(
            String email,
            CommandSender sender
    ) throws MessagingException, RuntimeException {
        // 检查是否存在邮箱验证码
        AtomicBoolean isExist = new AtomicBoolean(false);
        PlayerCache.verifyCodeList.forEach(verifyCodeInfoDTO -> {
            if (verifyCodeInfoDTO.getEmail().equals(email)) {
                if (System.currentTimeMillis() - verifyCodeInfoDTO.getCreatedAt() > 60000) {
                    PlayerCache.verifyCodeList.remove(verifyCodeInfoDTO);
                } else {
                    isExist.set(true);
                }
            }
        });
        if (isExist.get()) {
            Bukkit.getScheduler().runTask(mcPlugin, () -> sender.sendMessage(PLUGIN_PREFIX + "§c请勿频繁发送验证码！"));
        }
        // 生成随机验证码
        String verifyCode = RandomUtil.randomString(6);
        HashMap<String, String> param = new HashMap<>();
        param.put("USERNAME", sender.getName());
        param.put("CODE", verifyCode);
        // 保存邮箱验证码
        Bukkit.getScheduler().runTask(mcPlugin, () ->
                PlayerCache.verifyCodeList.add(
                        new VerifyCodeInfoDTO()
                                .setEmail(email)
                                .setVerifyCode(verifyCode)
                                .setCreatedAt(System.currentTimeMillis())
                ));

        Message message = new MimeMessage(Mail.getSession());
        message.setFrom(Mail.getFrom());
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject(CommonConstant.PLUGIN_NAME + " 验证码");
        // 创建一个 MimeBodyPart 用于 HTML 内容
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(Mail.getTemplate("code", param), "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        // 设置邮件内容为 Multipart 类型
        message.setContent(multipart);
        Transport.send(message);
        Bukkit.getScheduler().runTask(mcPlugin, () -> {
                    sender.sendMessage(PLUGIN_PREFIX + "§a验证码已发送至邮箱：§e" + email);
                    sender.sendMessage(PLUGIN_PREFIX + "§a请使用 §9/verify <邮箱验证码> §a命令验证邮箱！§7(验证码 §l5§f §7分钟内有效)");
                }
        );
        Logger.debug("验证码已发送至邮箱：§e" + email);
    }

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 验证结果
     */
    public static boolean verifyCode(String email, String code) {
        AtomicBoolean isVerify = new AtomicBoolean(false);
        PlayerCache.verifyCodeList.forEach(verifyCodeInfoDTO -> {
            if (verifyCodeInfoDTO.getEmail().equals(email)) {
                if (System.currentTimeMillis() - verifyCodeInfoDTO.getCreatedAt() > 300000) {
                    PlayerCache.verifyCodeList.remove(verifyCodeInfoDTO);
                    isVerify.set(false);
                } else {
                    if (verifyCodeInfoDTO.getVerifyCode().equals(code)) {
                        PlayerCache.verifyCodeList.remove(verifyCodeInfoDTO);
                        isVerify.set(true);
                    }
                }
            }
        });
        return isVerify.get();
    }
}
