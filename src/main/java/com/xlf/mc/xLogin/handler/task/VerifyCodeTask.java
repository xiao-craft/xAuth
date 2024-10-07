package com.xlf.mc.xLogin.handler.task;

import com.xlf.mc.xLogin.cache.PlayerCache;
import com.xlf.mc.xLogin.util.Logger;
import org.bukkit.Bukkit;

import static com.xlf.mc.xLogin.constant.PluginConstant.mcPlugin;

/**
 * 验证码任务
 * <p>
 * 该类为控制邮箱验证码的过期删除行为
 *
 * @since 1.0-SNAPSHOT
 * @version 1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class VerifyCodeTask {
    public static void onDeleteExpiredVerificationCodes() {
        Logger.debug("初始化验证码过期删除任务...");
        Bukkit.getScheduler().runTaskTimerAsynchronously(mcPlugin, () ->
                PlayerCache.verifyCodeList.forEach(verifyCodeInfoDTO -> {
                    if (System.currentTimeMillis() - verifyCodeInfoDTO.getCreatedAt() > 300000) {
                        PlayerCache.verifyCodeList.remove(verifyCodeInfoDTO);
                    }
                }), 200L, 1200L);
    }
}
