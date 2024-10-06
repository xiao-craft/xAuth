-- auto-generated definition
create table mc_xauth_log
(
    log_id         bigint auto_increment
        primary key,
    username       varchar(255)                         not null,
    ip             varchar(255)                         null,
    type           tinyint(1) default 0                 not null,
    success        tinyint(1) default 0                 not null,
    reason         varchar(255)                         null,
    `current_time` timestamp  default CURRENT_TIMESTAMP not null
);

