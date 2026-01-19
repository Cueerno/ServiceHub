set search_path to innotter_service;

-- pages
insert into pages (name, description, creator_id, image_url)
values
    ('Java World', 'All about Java', 1, null),
    ('Spring Tips', 'Spring Boot and Spring Security', 2, null),
    ('Databases', 'PostgreSQL, MySQL, indexes', 3, null),
    ('DevOps Life', 'CI/CD, Docker, Kubernetes', 4, null),
    ('Backend Notes', 'Architecture and patterns', 5, null);

-- posts
insert into posts (content, page_id)
values
    ('Welcome to Java World!', 1),
    ('Spring Security basics', 2),
    ('PostgreSQL indexing tips', 3),
    ('Docker for beginners', 4),
    ('Clean architecture overview', 5);

insert into posts (content, page_id, reply_to)
values
    ('Thanks for the info!', 1, 1),
    ('Very helpful post', 2, 2),
    ('Great explanation', 3, 3),
    ('Nice article', 4, 4),
    ('Good summary', 5, 5);

-- tags
insert into tags (name)
values
    ('java'),
    ('spring'),
    ('database'),
    ('devops'),
    ('backend');

-- pages_tags
insert into pages_tags (page_id, tag_id)
values
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5);

-- followers
insert into followers (page_id, user_id)
values
    (1, 10),
    (1, 11),
    (2, 12),
    (3, 13),
    (4, 14);

-- likes
insert into likes (post_id, user_id)
values
    (1, 10),
    (2, 11),
    (3, 12),
    (4, 13),
    (5, 14);
