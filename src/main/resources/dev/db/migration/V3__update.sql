alter table group_post
    modify image varchar(1000) null;

alter table group_post
    modify content varchar(500) not null;

alter table user_complaints
    modify number_phone varchar(30) null;

alter table user_complaints
    modify status varchar(30) null;

