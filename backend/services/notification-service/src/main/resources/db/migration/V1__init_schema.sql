create schema if not exists notification_service;

SET search_path TO notification_service;

create table password_reset_messages
(
    id            uuid primary key,
    user_id       bigint       not null,
    email_address varchar(100) not null,
    subject       varchar(255) not null,
    body          text         not null,
    published_at  timestamptz,
    sent_at       timestamptz
);
