create table my_social_web.answer_comment
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
    user_id    bigint        null
)
    engine = MyISAM;

create index FK14803j2qwcis4cu5iff7hudr0
    on my_social_web.answer_comment (user_id);

create index FK1eh7k49vay6vb5bxd7tmfqjw4
    on my_social_web.answer_comment (comment_id);

create table my_social_web.black_list
(
    id                  bigint auto_increment
        primary key,
    create_at           datetime    null,
    edit_at             datetime    null,
    id_user_on_the_list bigint      null,
    id_user_send_block  bigint      null,
    status              varchar(20) null
)
    engine = MyISAM;

create table my_social_web.comment
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
    user_id         bigint        null
)
    engine = MyISAM;

create index FK99qtupwiidpsy7g0o8ppdgaur
    on my_social_web.comment (user_id);

create index FKh5r19qe768uiwbclvmp74i6wo
    on my_social_web.comment (post_id);

create table my_social_web.conversation
(
    id          bigint auto_increment
        primary key,
    create_at   datetime null,
    receiver_id bigint   null,
    sender_id   bigint   null
)
    engine = MyISAM;

create index FKj0diew5cfwp3o2bv66gecok30
    on my_social_web.conversation (receiver_id);

create index FKp516frvljunejikjowf7g2j2n
    on my_social_web.conversation (sender_id);

create table my_social_web.conversation_delete_time
(
    id              bigint auto_increment
        primary key,
    id_conversation bigint   null,
    id_delete       bigint   null,
    time_delete     datetime null
)
    engine = MyISAM;

create table my_social_web.dis_like
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    post_id   bigint   null,
    user_id   bigint   null
)
    engine = MyISAM;

create index FK48avq9ytpi8saiufmqia9qdjc
    on my_social_web.dis_like (post_id);

create index FKfupim88hwci1fufmah3eicmpx
    on my_social_web.dis_like (user_id);

create table my_social_web.dis_like_comment
(
    id         bigint auto_increment
        primary key,
    create_at  datetime null,
    comment_id bigint   null,
    user_id    bigint   null
)
    engine = MyISAM;

create index FK6shg9ouca1du1ad6xoq9iuew0
    on my_social_web.dis_like_comment (user_id);

create index FKqvybg9xe2wp7vqybh7f8uoo0r
    on my_social_web.dis_like_comment (comment_id);

create table my_social_web.flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index flyway_schema_history_s_idx
    on my_social_web.flyway_schema_history (success);

create table my_social_web.follow_watching
(
    id             bigint auto_increment
        primary key,
    create_at      datetime    null,
    id_user        bigint      null,
    id_user_target bigint      null,
    status         varchar(30) null
)
    engine = MyISAM;

create table my_social_web.friend_relation
(
    id            bigint auto_increment
        primary key,
    id_friend     bigint      null,
    id_user       bigint      null,
    status_friend varchar(20) null,
    friend_id     bigint      null,
    user_login_id bigint      null
)
    engine = MyISAM;

create index FKlbppq8ncy32c6r70vpueluhlo
    on my_social_web.friend_relation (user_login_id);

create index FKmngs1jd98f4wtupeq8xa90hqk
    on my_social_web.friend_relation (friend_id);

create table my_social_web.group_participant
(
    id           bigint auto_increment
        primary key,
    create_at    datetime    null,
    status       varchar(20) null,
    the_group_id bigint      null,
    user_id      bigint      null
)
    engine = MyISAM;

create index FK83wpp3oxs6gq3fjynloc79r92
    on my_social_web.group_participant (user_id);

create index FKc75upvb2jyvudwqwm6cawrpmg
    on my_social_web.group_participant (the_group_id);

create table my_social_web.group_post
(
    id           bigint auto_increment
        primary key,
    content      varchar(500)  not null,
    create_at    datetime      null,
    create_by    varchar(255)  null,
    delete_at    datetime      null,
    edit_at      datetime      null,
    edit_by      varchar(255)  null,
    image        varchar(1000) null,
    status       varchar(20)   null,
    the_group_id bigint        null,
    user_id      bigint        null
)
    engine = MyISAM;

create index FK2vivmoxhtl36olsp6dpy3tc24
    on my_social_web.group_post (the_group_id);

create index FK8ckpj2ashg0rn4hef0ngye35w
    on my_social_web.group_post (user_id);

create table my_social_web.hide_post
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    id_post   bigint   null,
    id_user   bigint   null
)
    engine = MyISAM;

create table my_social_web.icon_heart
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    post_id   bigint   null,
    user_id   bigint   null
)
    engine = MyISAM;

create index FKaudse7p7v7w8jrk4u8pk30wsy
    on my_social_web.icon_heart (user_id);

create index FKauqq09s8thcgx09ooa6bp8l52
    on my_social_web.icon_heart (post_id);

create table my_social_web.image
(
    id         bigint auto_increment
        primary key,
    create_at  datetime    null,
    delete_at  datetime    null,
    link_image longtext    null,
    status     varchar(20) null,
    id_user    bigint      null
)
    engine = MyISAM;

create table my_social_web.image_group
(
    id              bigint auto_increment
        primary key,
    create_at       datetime    null,
    delete_at       datetime    null,
    id_the_group    bigint      null,
    id_user_up_load bigint      null,
    link_image      longtext    null,
    status          varchar(20) null
)
    engine = MyISAM;

create table my_social_web.last_user_login
(
    id         bigint auto_increment
        primary key,
    avatar     varchar(1000) null,
    full_name  varchar(150)  null,
    id_user    bigint        null,
    ip_address varchar(255)  null,
    login_time datetime      null,
    user_name  varchar(255)  null
)
    engine = MyISAM;

create table my_social_web.life_events
(
    id        bigint auto_increment
        primary key,
    create_at datetime     null,
    edit_at   datetime     null,
    status    varchar(255) null,
    timeline  datetime     null,
    work      varchar(255) null,
    user_id   bigint       null
)
    engine = MyISAM;

create index FKltg7p84uq2tlnk63my40rvt8q
    on my_social_web.life_events (user_id);

create table my_social_web.like_comment
(
    id         bigint auto_increment
        primary key,
    create_at  datetime null,
    comment_id bigint   null,
    user_id    bigint   null
)
    engine = MyISAM;

create index FKh0r3rvwkfrav930797rs2d9y1
    on my_social_web.like_comment (comment_id);

create index FKooved5rs2qy8n8vslajige7tk
    on my_social_web.like_comment (user_id);

create table my_social_web.like_post
(
    id        bigint auto_increment
        primary key,
    create_at datetime null,
    post_id   bigint   null,
    user_id   bigint   null
)
    engine = MyISAM;

create index FKd55unp8y2tbf5p8tiesotxie7
    on my_social_web.like_post (post_id);

create index FKn5u569olo0auoikr65hh59vi6
    on my_social_web.like_post (user_id);

create table my_social_web.messenger
(
    id                          bigint auto_increment
        primary key,
    content                     varchar(300)  null,
    create_at                   datetime      null,
    format                      varchar(255)  null,
    image                       varchar(1000) null,
    conversation_id             bigint        null,
    conversation_delete_time_id bigint        null,
    sender_id                   bigint        null
)
    engine = MyISAM;

create index FK1u6uybnnkgbq043y2g6h6g4jn
    on my_social_web.messenger (conversation_id);

create index FK4vjna8ajpamho8g0dkamwphw7
    on my_social_web.messenger (sender_id);

create index FKgqcy2gsdewchx4p9gxde57nqs
    on my_social_web.messenger (conversation_delete_time_id);

create table my_social_web.notification
(
    id         bigint auto_increment
        primary key,
    create_at  datetime     null,
    status     varchar(20)  null,
    title      varchar(255) null,
    type       varchar(255) null,
    type_id    bigint       null,
    action_id  bigint       null,
    send_to_id bigint       null
)
    engine = MyISAM;

create index FK2jj22esp65yx46n8sg3tmy5hq
    on my_social_web.notification (send_to_id);

create index FK8why3j4unwf9r70lddksciy4w
    on my_social_web.notification (action_id);

create table my_social_web.post2
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
    status          varchar(20)   null,
    user_id         bigint        null
)
    engine = MyISAM;

create index FKsye56ja11mm8ckd8cfe2672wa
    on my_social_web.post2 (user_id);

create index post2_status_index
    on my_social_web.post2 (status);

create table my_social_web.report_violations
(
    id             bigint auto_increment
        primary key,
    content        varchar(255) null,
    create_at      datetime     null,
    edit_at        datetime     null,
    id_user_report bigint       null,
    id_violate     bigint       null,
    reason         varchar(255) null,
    status         varchar(20)  null,
    type           varchar(255) null
)
    engine = MyISAM;

create table my_social_web.roles
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
)
    engine = MyISAM;

create table my_social_web.saved
(
    id          bigint auto_increment
        primary key,
    content     varchar(255) null,
    group_name  varchar(255) null,
    id_post     bigint       null,
    id_user     bigint       null,
    image_post  varchar(255) null,
    save_date   datetime     null,
    status      varchar(20)  null,
    type        varchar(255) null,
    user_create varchar(255) null
)
    engine = MyISAM;

create table my_social_web.short_news
(
    id        bigint auto_increment
        primary key,
    content   varchar(120)  null,
    create_at datetime      null,
    expired   int           not null,
    image     varchar(1000) null,
    is_delete bit           not null,
    remaining int           not null,
    status    varchar(20)   null,
    to_day    datetime      null,
    user_id   bigint        null
)
    engine = MyISAM;

create index FKpqnoj3hl9bofyh9bnsgkcp6of
    on my_social_web.short_news (user_id);

create table my_social_web.test_data
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
)
    engine = MyISAM;

create table my_social_web.test_data2
(
    id           bigint auto_increment
        primary key,
    test_field1  varchar(500) null,
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
    test_field2  varchar(500) null,
    test_field20 varchar(255) null,
    test_field21 varchar(255) null,
    test_field22 varchar(255) null,
    test_field3  varchar(100) null,
    test_field4  varchar(255) null,
    test_field5  varchar(100) null,
    test_field6  varchar(100) null,
    test_field7  varchar(100) null,
    test_field8  varchar(100) null,
    test_field9  varchar(100) null
)
    engine = MyISAM;

create index test_data2_test_field3_index
    on my_social_web.test_data2 (test_field3);

create table my_social_web.the_group
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
    status         varchar(20)   null,
    subtype        varchar(255)  null,
    type           varchar(255)  null
)
    engine = MyISAM;

create table my_social_web.user_complaints
(
    id           bigint auto_increment
        primary key,
    email        varchar(255) null,
    full_name    varchar(255) null,
    username     varchar(255) null,
    content      varchar(255) null,
    create_by    varchar(255) null,
    created_at   datetime     null,
    edit_by      varchar(255) null,
    edited_at    datetime     null,
    number_phone varchar(30)  null,
    status       varchar(30)  null,
    type         varchar(255) null
)
    engine = MyISAM;

create table my_social_web.user_description
(
    id          bigint auto_increment
        primary key,
    create_at   datetime     null,
    description varchar(500) null,
    edit_at     datetime     null,
    status      varchar(20)  null,
    user_id     bigint       null
)
    engine = MyISAM;

create index FKb214rtj0f461a97weydgwlgfr
    on my_social_web.user_description (user_id);

create table my_social_web.user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
)
    engine = MyISAM;

create index FKt7e7djp752sqn6w22i6ocqy6q
    on my_social_web.user_role (role_id);

create table my_social_web.user_table
(
    id               bigint auto_increment
        primary key,
    address          varchar(255)  null,
    avatar           varchar(1000) null,
    confirm_password varchar(100)  null,
    cover            varchar(1000) null,
    create_at        datetime      null,
    date_of_birth    datetime      null,
    education        varchar(200)  null,
    email            varchar(200)  null,
    enabled          bit           not null,
    favorite         varchar(500)  null,
    full_name        varchar(255)  null,
    gender           varchar(15)   null,
    password         varchar(100)  null,
    phone            varchar(20)   null,
    status           varchar(20)   null,
    username         varchar(100)  not null,
    constraint UK_en3wad7p8qfu8pcmh62gvef6v
        unique (username)
)
    engine = MyISAM;

create index user_table_status_index
    on my_social_web.user_table (status);

create table my_social_web.verification_token
(
    id           bigint auto_increment
        primary key,
    created_date datetime     null,
    expiry_date  datetime     null,
    token        varchar(255) null,
    user_id      bigint       not null
)
    engine = MyISAM;

create index FK140t81x9fnhachhvw7b2eaeo6
    on my_social_web.verification_token (user_id);

