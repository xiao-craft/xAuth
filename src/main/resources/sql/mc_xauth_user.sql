-- auto-generated definition
create table mc_xauth_user
(
    uuid              varchar(48)                          not null
        primary key,
    username          varchar(64)                          not null,
    genuine_uuid      varchar(48)                          null,
    is_verify         tinyint(1) default 0                 not null,
    email             varchar(254)                         not null,
    is_email_verified tinyint(1) default 0                 not null,
    password          varchar(60)                          not null,
    phone             varchar(11)                          null,
    qq                varchar(12)                          null,
    is_auth           tinyint(1) default 0                 not null,
    created_at        timestamp  default CURRENT_TIMESTAMP not null,
    last_login_at     timestamp                            null,
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint qq
        unique (qq),
    constraint username
        unique (username)
);

