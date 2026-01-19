set search_path to user_management_service;

insert into groups (name)
values
    ('Developers'),
    ('QA'),
    ('Users');

insert into users (
    username,
    firstname,
    lastname,
    password,
    phone_number,
    email,
    role,
    image_url,
    group_id
)
values
    ('admin', 'Admin', 'Root', '$2a$10$TXc2lm2kcI3C4n698Ga0Oej5z1Ba6mrhKYc/ojrMjBVyAvSJ4.ULa', '+10000000001', 'admin@example.com', 'ADMIN', null, null),
    ('moderator', 'Mod', 'Erator', '$2a$10$QGcLNT80U8mT908HY2ZDJeFRSP/gX4emCmBCF1.oEdE1xOQGyxtFy', '+10000000002', 'moderator@example.com', 'MODERATOR', null, null),
    ('dev1', 'John', 'Doe', '$2a$10$GYtcHIj4rnP8bIWvcUUn2OXrBBmEYCpafxBhE6QRbU1irE80gc6ty', '+10000000003', 'john.doe@example.com', 'USER', null, 1),
    ('dev2', 'Arthur', 'Matson', '$2a$10$Uq2WQQjXTh86n7GigkI4BuIUrzLBupi7Jdh6.hvuHcNEuT2KxEx5a', '+10000000004', 'arthur.matson@example.com', 'USER', null, 1),
    ('dev3', 'Kratos', 'GodOfWar', '$2a$10$zrzW9QTuVx640n8zg6428uAzLL/6HXQuriCm4Jkdp/7vI9ydyGwuG', '+10000000005', 'kratos228@example.com', 'USER', null, 1),
    ('qa1', 'Cristiano', 'Ronaldo', '$2a$10$uU7YUaxJG4DnPTpFFlamNeVPLBFCk5.lTUrh2LLaQKalw3JJkfdhK', '+10000000006', 'cr7@example.com', 'USER', null, 2),
    ('qa2', 'Ilon', 'Musk', '$2a$10$zU.DPkT8kKLq9nTSvhAMiubpBS26QcBOc.gqQdY1wBU0npEqk0aX2', '+10000000007', 'r54hu77@example.com', 'USER', null, 2),
    ('user1', 'Zack', 'Brown', '$2a$10$wjHuGW1xTIoXryaQ4.WvxeE33T.r97KqPOrf/lseSquGU.UM8Xx1C', '+10000000008', 'norris@example.com', 'USER', null, 3),
    ('user2', 'Max', 'Verstappen', '$2a$10$e2caAwgNDZ33HKjTMDhOhuKwJGAExlpH9QHDrkGyWbMncYy4A3gwm', '+10000000008', 'mad.max@example.com', 'USER', null, 3);