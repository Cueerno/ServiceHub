create schema if not exists innotter_service;

set search_path to innotter_service;

create table pages
(
    id           bigserial primary key,
    name         varchar(100) not null,
    description  text         not null,
    creator_id   bigint,
    image_url    text,
    is_blocked   boolean      not null default false,
    unblock_date timestamptz,
    created_at   timestamptz  not null default now(),
    updated_at   timestamptz  not null default now()
);

create table posts
(
    id         bigserial primary key,
    content    text        not null,
    reply_to   bigint references posts (id),
    page_id    bigint      not null references pages (id),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table tags
(
    id         bigserial primary key,
    name       text        not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table pages_tags
(
    page_id bigint not null references pages (id),
    tag_id  bigint not null references tags (id),
    primary key (page_id, tag_id)
);

create table followers
(
    page_id bigint not null references pages (id),
    user_id bigint not null,
    primary key (page_id, user_id)
);

create table likes
(
    post_id bigint not null references posts (id),
    user_id bigint not null,
    primary key (post_id, user_id)
);
