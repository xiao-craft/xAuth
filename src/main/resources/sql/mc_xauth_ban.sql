-- auto-generated definition
create table mc_xauth_ban
(
    uuid       varchar(48)                         not null
        primary key,
    user_uuid  varchar(48)                         not null,
    operator   varchar(48)                         null,
    reason     text                                not null,
    banned_at  timestamp default CURRENT_TIMESTAMP not null,
    expired_at timestamp                           not null
);