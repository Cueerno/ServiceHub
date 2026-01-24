create schema if not exists user_management_service;

set search_path to user_management_service;

create table groups
(
    id         bigserial primary key,
    name       varchar(100) not null,
    created_at timestamptz  not null default now(),
    updated_at timestamptz  not null default now()
);

create table users
(
    id           bigserial primary key,
    username     varchar(100) not null unique,
    firstname    varchar(100) not null,
    lastname     varchar(100) not null,
    password     varchar(64)  not null,
    phone_number varchar(25)  not null,
    email        varchar(100) not null unique,
    role         varchar(50)  not null check (role in ('USER', 'ADMIN', 'MODERATOR')),
    image_url    text,
    is_blocked   boolean      not null default false,
    created_at   timestamptz  not null default now(),
    updated_at   timestamptz  not null default now(),

    group_id     bigint references groups (id)
);

create table refresh_tokens
(
    id         bigserial primary key,
    token_hash varchar(64) not null,
    revoked    boolean     not null default false,
    jti        varchar(36) not null unique,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,

    user_id    bigint      not null references users (id)
);

create table password_reset_tokens
(
    id         bigserial primary key,
    token      varchar(128) not null unique,
    expires_at timestamptz  not null,
    used       boolean      not null default false,
    created_at timestamptz  not null,

    user_id    bigint       not null references users (id)
);