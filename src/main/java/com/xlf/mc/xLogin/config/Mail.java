package com.xlf.mc.xLogin.config;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.xlf.mc.xLogin.constant.PluginConstant.coreConfig;
import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;

/**
 * 邮件配置
 * <p>
 * 该类用于配置邮件发送的相关信息
 * 该类会在邮件发送时被调用，用于配置邮件发送的相关信息
 * 该类包含了邮件发送的相关信息，如：邮件服务器、邮件发送者等
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class Mail {
    private static Session session;
    private static Address from;
    private static String fromName;

    public static void setUp() {
        // 设置 SMTP 服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.host", coreConfig.getString("mail.host"));
        props.put("mail.smtp.port", coreConfig.getString("mail.port"));
        props.put("mail.smtp.auth", coreConfig.getString("mail.auth"));
        props.put("mail.smtp.starttls.enable", coreConfig.getString("mail.tls"));
        props.put("mail.smtp.ssl.enable", coreConfig.getString("mail.ssl"));
        props.put("mail.debug", coreConfig.getString("mail.debug"));

        // 创建邮件会话
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        coreConfig.getString("mail.username"),
                        coreConfig.getString("mail.password")
                );
            }
        });

        try {
            from = new InternetAddress(Objects.requireNonNull(coreConfig.getString("mail.username")));
            fromName = coreConfig.getString("mail.from_name");
        } catch (AddressException e) {
            throw new RuntimeException("邮件配置错误！");
        }
    }

    public static String getTemplate(String template, Map<String, String> params) {
        // 读取邮件模板
        File templateFile = new File(mcPlugin.getDataFolder() + "/template/" + template + ".html");
        if (!templateFile.exists()) {
            throw new RuntimeException("邮件模板不存在！");
        }
        try {
            byte[] readAllBytes = Files.readAllBytes(templateFile.toPath());
            String content = new String(readAllBytes);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return content;
        } catch (Exception e) {
            throw new RuntimeException("邮件模板读取失败！");
        }
    }

    public static void shutdown() {
        session = null;
    }

    public static Session getSession() {
        return session;
    }

    public static Address getFrom() {
        return from;
    }

    public static String getFromName() {
        return fromName;
    }
}
