-- auto-generated definition
create table user_table
(
    id               bigint auto_increment
        primary key,
    address          varchar(255)  null,
    avatar           varchar(1000) null,
    confirm_password varchar(255)  null,
    cover            varchar(1000) null,
    create_at        datetime      null,
    date_of_birth    datetime      null,
    education        varchar(255)  null,
    email            varchar(255)  null,
    enabled          bit           not null,
    favorite         varchar(500)  null,
    full_name        varchar(255)  null,
    gender           varchar(255)  null,
    password         varchar(255)  null,
    phone            varchar(50)   null,
    status           varchar(255)  null,
    username         varchar(200)  not null,
    constraint UK_en3wad7p8qfu8pcmh62gvef6v
        unique (username)
);

-- auto-generated definition
create table the_group
(
    id             bigint auto_increment
        primary key,
    avatar_group   varchar(1000) null,
    cover_group    varchar(1000) null,
    create_at      datetime      null,
    create_by      varchar(255)  null,
    group_name     varchar(255)  null,
    id_user_create bigint        null,
    number_user    bigint        null,
    status         varchar(255)  null,
    subtype        varchar(255)  null,
    type           varchar(255)  null
);

-- auto-generated definition
create table post2
(
    id              bigint auto_increment
        primary key,
    content         varchar(1000) not null,
    create_at       datetime      null,
    edit_at         datetime      null,
    icon_heart      bigint        null,
    image           varchar(1000) null,
    is_delete       bit           not null,
    number_dis_like bigint        null,
    number_like     bigint        null,
    status          varchar(255)  null,
    user_id         bigint        null,
    constraint FKsye56ja11mm8ckd8cfe2672wa
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table comment
(
    id              bigint auto_increment
        primary key,
    content         varchar(500)  not null,
    create_at       datetime      null,
    delete_at       datetime      null,
    edit_at         datetime      null,
    image           varchar(1000) null,
    is_delete       bit           not null,
    number_dis_like bigint        null,
    number_like     bigint        null,
    post_id         bigint        null,
    user_id         bigint        null,
    constraint FK99qtupwiidpsy7g0o8ppdgaur
        foreign key (user_id) references user_table (id),
    constraint FKh5r19qe768uiwbclvmp74i6wo
        foreign key (post_id) references post2 (id)
);

-- auto-generated definition
create table answer_comment
(
    id         bigint auto_increment
        primary key,
    content    varchar(500)  not null,
    create_at  datetime      null,
    delete_at  datetime      null,
    edit_at    datetime      null,
    image      varchar(1000) null,
    is_delete  bit           not null,
    comment_id bigint        null,
    user_id    bigint        null,
    constraint FK14803j2qwcis4cu5iff7hudr0
        foreign key (user_id) references user_table (id),
    constraint FK1eh7k49vay6vb5bxd7tmfqjw4
        foreign key (comment_id) references comment (id)
);

-- auto-generated definition
create table black_list
(
    id                  bigint auto_increment
        primary key,
    create_at           datetime     null,
    edit_at             datetime     null,
    id_user_on_the_list bigint       null,
    id_user_send_block  bigint       null,
    status              varchar(255) null
);

-- auto-generated definition
create table conversation
(
    id          bigint auto_increment
        primary key,
    create_at   datetime null,
    receiver_id bigint   null,
    sender_id   bigint   null,
    constraint FKj0diew5cfwp3o2bv66gecok30
        foreign key (receiver_id) references user_table (id),
    constraint FKp516frvljunejikjowf7g2j2n
        foreign key (sender_id) references user_table (id)
);

-- auto-generated definition
create table conversation_delete_time
(
    id              bigint auto_increment
        primary key,
    id_conversation bigint   null,
    id_delete       bigint   null,
    time_delete     datetime null
);

-- auto-generated definition
create table dis_like
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    post_id   bigint   null,
    user_id   bigint   null,
    constraint FK48avq9ytpi8saiufmqia9qdjc
        foreign key (post_id) references post2 (id),
    constraint FKfupim88hwci1fufmah3eicmpx
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table dis_like_comment
(
    id         bigint auto_increment
        primary key,
    create_at  datetime null,
    comment_id bigint   null,
    user_id    bigint   null,
    constraint FK6shg9ouca1du1ad6xoq9iuew0
        foreign key (user_id) references user_table (id),
    constraint FKqvybg9xe2wp7vqybh7f8uoo0r
        foreign key (comment_id) references comment (id)
);

-- auto-generated definition
create table follow_watching
(
    id             bigint auto_increment
        primary key,
    create_at      datetime     null,
    id_user        bigint       null,
    id_user_target bigint       null,
    status         varchar(255) null
);

-- auto-generated definition
create table friend_relation
(
    id            bigint auto_increment
        primary key,
    id_friend     bigint       null,
    id_user       bigint       null,
    status_friend varchar(255) null,
    friend_id     bigint       null,
    user_login_id bigint       null,
    constraint FKlbppq8ncy32c6r70vpueluhlo
        foreign key (user_login_id) references user_table (id),
    constraint FKmngs1jd98f4wtupeq8xa90hqk
        foreign key (friend_id) references user_table (id)
);

-- auto-generated definition
create table group_participant
(
    id           bigint auto_increment
        primary key,
    create_at    datetime     null,
    status       varchar(255) null,
    the_group_id bigint       null,
    user_id      bigint       null,
    constraint FK83wpp3oxs6gq3fjynloc79r92
        foreign key (user_id) references user_table (id),
    constraint FKc75upvb2jyvudwqwm6cawrpmg
        foreign key (the_group_id) references the_group (id)
);

-- auto-generated definition
create table group_post
(
    id           bigint auto_increment
        primary key,
    content      varchar(1000) not null,
    create_at    datetime      null,
    create_by    varchar(255)  null,
    delete_at    datetime      null,
    edit_at      datetime      null,
    edit_by      varchar(255)  null,
    image        varchar(255)  null,
    status       varchar(255)  null,
    the_group_id bigint        null,
    user_id      bigint        null,
    constraint FK2vivmoxhtl36olsp6dpy3tc24
        foreign key (the_group_id) references the_group (id),
    constraint FK8ckpj2ashg0rn4hef0ngye35w
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table hide_post
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    id_post   bigint   null,
    id_user   bigint   null
);

-- auto-generated definition
create table icon_heart
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    post_id   bigint   null,
    user_id   bigint   null,
    constraint FKaudse7p7v7w8jrk4u8pk30wsy
        foreign key (user_id) references user_table (id),
    constraint FKauqq09s8thcgx09ooa6bp8l52
        foreign key (post_id) references post2 (id)
);

-- auto-generated definition
create table image
(
    id         bigint auto_increment
        primary key,
    create_at  datetime     null,
    delete_at  datetime     null,
    id_user    bigint       null,
    link_image longtext     null,
    status     varchar(255) null
);

-- auto-generated definition
create table image_group
(
    id              bigint auto_increment
        primary key,
    create_at       datetime     null,
    delete_at       datetime     null,
    id_the_group    bigint       null,
    id_user_up_load bigint       null,
    link_image      longtext     null,
    status          varchar(255) null
);

-- auto-generated definition
create table last_user_login
(
    id         bigint auto_increment
        primary key,
    avatar     varchar(1000) null,
    full_name  varchar(255)  null,
    id_user    bigint        null,
    ip_address varchar(255)  null,
    login_time datetime      null,
    user_name  varchar(255)  null
);

-- auto-generated definition
create table life_events
(
    id        bigint auto_increment
        primary key,
    create_at datetime     null,
    edit_at   datetime     null,
    status    varchar(255) null,
    timeline  datetime     null,
    work      varchar(255) null,
    user_id   bigint       null,
    constraint FKltg7p84uq2tlnk63my40rvt8q
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table like_comment
(
    id         bigint auto_increment
        primary key,
    create_at  datetime null,
    comment_id bigint   null,
    user_id    bigint   null,
    constraint FKh0r3rvwkfrav930797rs2d9y1
        foreign key (comment_id) references comment (id),
    constraint FKooved5rs2qy8n8vslajige7tk
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table like_post
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    post_id   bigint   null,
    user_id   bigint   null,
    constraint FKd55unp8y2tbf5p8tiesotxie7
        foreign key (post_id) references post2 (id),
    constraint FKn5u569olo0auoikr65hh59vi6
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table messenger
(
    id                          bigint auto_increment
        primary key,
    content                     varchar(300)  null,
    create_at                   datetime      null,
    format                      varchar(255)  null,
    image                       varchar(1000) null,
    conversation_id             bigint        null,
    conversation_delete_time_id bigint        null,
    sender_id                   bigint        null,
    constraint FK1u6uybnnkgbq043y2g6h6g4jn
        foreign key (conversation_id) references conversation (id),
    constraint FK4vjna8ajpamho8g0dkamwphw7
        foreign key (sender_id) references user_table (id),
    constraint FKgqcy2gsdewchx4p9gxde57nqs
        foreign key (conversation_delete_time_id) references conversation_delete_time (id)
);

-- auto-generated definition
create table notification
(
    id         bigint auto_increment
        primary key,
    create_at  datetime     null,
    status     varchar(255) null,
    title      varchar(255) null,
    type       varchar(255) null,
    type_id    bigint       null,
    action_id  bigint       null,
    send_to_id bigint       null,
    constraint FK2jj22esp65yx46n8sg3tmy5hq
        foreign key (send_to_id) references user_table (id),
    constraint FK8why3j4unwf9r70lddksciy4w
        foreign key (action_id) references user_table (id)
);

-- auto-generated definition
create table report_violations
(
    id             bigint auto_increment
        primary key,
    content        varchar(255) null,
    create_at      datetime     null,
    edit_at        datetime     null,
    id_user_report bigint       null,
    id_violate     bigint       null,
    reason         varchar(255) null,
    status         varchar(255) null,
    type           varchar(255) null
);

-- auto-generated definition
create table roles
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);

-- auto-generated definition
create table saved
(
    id          bigint auto_increment
        primary key,
    content     varchar(255) null,
    group_name  varchar(255) null,
    id_post     bigint       null,
    id_user     bigint       null,
    image_post  varchar(255) null,
    save_date   datetime     null,
    status      varchar(255) null,
    type        varchar(255) null,
    user_create varchar(255) null
);

-- auto-generated definition
create table short_news
(
    id        bigint auto_increment
        primary key,
    content   varchar(120)  null,
    create_at datetime      null,
    expired   int           not null,
    image     varchar(1000) null,
    is_delete bit           not null,
    remaining int           not null,
    status    varchar(255)  null,
    to_day    datetime      null,
    user_id   bigint        null,
    constraint FKpqnoj3hl9bofyh9bnsgkcp6of
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table test_data
(
    id          bigint auto_increment
        primary key,
    address     varchar(255) null,
    country     varchar(255) null,
    education   varchar(255) null,
    first_name  varchar(255) null,
    last_name   varchar(255) null,
    license     varchar(255) null,
    passport    varchar(255) null,
    phone       varchar(255) null,
    religion    varchar(255) null,
    test_field1 varchar(255) null,
    test_field2 varchar(255) null,
    test_field3 varchar(255) null,
    test_field4 varchar(255) null,
    test_field5 varchar(255) null,
    test_field6 varchar(255) null,
    test_field7 varchar(255) null,
    test_field8 varchar(255) null,
    vaccination varchar(255) null
);

-- auto-generated definition
create table test_data2
(
    id           bigint auto_increment
        primary key,
    test_field1  varchar(255) null,
    test_field10 varchar(255) null,
    test_field11 varchar(255) null,
    test_field12 varchar(255) null,
    test_field13 varchar(255) null,
    test_field14 varchar(255) null,
    test_field15 varchar(255) null,
    test_field16 varchar(255) null,
    test_field17 varchar(255) null,
    test_field18 varchar(255) null,
    test_field19 varchar(255) null,
    test_field2  varchar(255) null,
    test_field20 varchar(255) null,
    test_field21 varchar(255) null,
    test_field22 varchar(255) null,
    test_field3  varchar(255) null,
    test_field4  varchar(255) null,
    test_field5  varchar(255) null,
    test_field6  varchar(255) null,
    test_field7  varchar(255) null,
    test_field8  varchar(255) null,
    test_field9  varchar(255) null
);

-- auto-generated definition
create table user_complaints
(
    id           bigint auto_increment
        primary key,
    content      varchar(255) null,
    create_by    varchar(255) null,
    created_at   datetime     null,
    edit_by      varchar(255) null,
    edited_at    datetime     null,
    email        varchar(255) null,
    full_name    varchar(255) null,
    number_phone varchar(255) null,
    status       varchar(255) null,
    type         varchar(255) null,
    username     varchar(255) null
);

-- auto-generated definition
create table user_description
(
    id          bigint auto_increment
        primary key,
    create_at   datetime     null,
    description varchar(500) null,
    edit_at     datetime     null,
    status      varchar(255) null,
    user_id     bigint       null,
    constraint FKb214rtj0f461a97weydgwlgfr
        foreign key (user_id) references user_table (id)
);

-- auto-generated definition
create table user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint FK5cwpllhe458f6j6fb7rmhfyt2
        foreign key (user_id) references user_table (id),
    constraint FKt7e7djp752sqn6w22i6ocqy6q
        foreign key (role_id) references roles (id)
);

-- auto-generated definition
create table verification_token
(
    id           bigint auto_increment
        primary key,
    created_date datetime     null,
    expiry_date  datetime     null,
    token        varchar(255) null,
    user_id      bigint       not null,
    constraint FK140t81x9fnhachhvw7b2eaeo6
        foreign key (user_id) references user_table (id)
);

