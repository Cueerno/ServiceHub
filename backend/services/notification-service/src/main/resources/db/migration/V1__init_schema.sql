create schema if not exists notification_service;

SET search_path TO notification_service;

create table password_reset_messages
(
    id              uuid primary key,
    user_id         bigint       not null,
    email_address   varchar(100) not null,
    status          varchar(255) not null check ( status in ('PENDING', 'PROCESSING', 'SENT', 'FAILED') ),
    token           varchar(32)  not null,
    attempt_count   int          not null default 0,
    last_attempt_at timestamptz,
    published_at    timestamptz  not null,
    sent_at         timestamptz
);
