package com.xlf.mc.xLogin.model.dto;

import java.util.UUID;

/**
 * 在线玩家信息数据传输对象
 * <p>
 * 该类用于传输在线玩家的信息
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class OnlinePlayerInfoDTO {
    private UUID uuid;
    private String username;
    private boolean isLogin;
}
