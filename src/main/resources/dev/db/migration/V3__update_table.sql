alter table group_post
    modify image varchar(1000) null;

alter table group_post
    modify content varchar(500) not null;

alter table user_complaints
    modify number_phone varchar(30) null;

alter table user_complaints
    modify status varchar(30) null;

alter table follow_watching
    modify status varchar (30) null;

alter table user_table
    modify date_of_birth datetime null;
