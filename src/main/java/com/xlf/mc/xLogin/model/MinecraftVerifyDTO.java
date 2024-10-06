package com.xlf.mc.xLogin.model;

/**
 * Minecraft 验证 DTO
 * <p>
 * 该类用于封装 Minecraft 验证的数据传输对象。
 *
 * @since v1.0-SNAPSHOT
 * @version v1.0-SNAPSHOT
 * @author xiao_lfeng
 */
public class MinecraftVerifyDTO {
    private String code;
    private String message;
    private Data data;
    private boolean success;

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 静态内部类 Data
    public static class Data {
        private Player player;

        // Getters and Setters
        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }
    }

    // 静态内部类 Player
    public static class Player {
        private Meta meta;
        private String username;
        private String id;
        private String rawId; // JSON 中为 raw_id
        private String avatar;
        private String skinTexture; // JSON 中为 skin_texture

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRawId() {
            return rawId;
        }

        public void setRawId(String rawId) {
            this.rawId = rawId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSkinTexture() {
            return skinTexture;
        }

        public void setSkinTexture(String skinTexture) {
            this.skinTexture = skinTexture;
        }
    }

    // 静态内部类 Meta
    public static class Meta {
        private long cachedAt; // JSON 中为 cached_at

        public long getCachedAt() {
            return cachedAt;
        }

        public void setCachedAt(long cachedAt) {
            this.cachedAt = cachedAt;
        }
    }
}
