-- auto-generated definition
create table mc_xauth_token
(
    token_uuid varchar(48)                          not null
        primary key,
    user_uuid  varchar(48)                          not null,
    is_login   tinyint(1) default 0                 null,
    x          bigint                               not null,
    y          bigint                               not null,
    z          bigint                               not null,
    login_at   timestamp  default CURRENT_TIMESTAMP not null,
    logout_at  datetime                             null
);

