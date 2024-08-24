create index user_table_username_email_index
    on user_table (username, email);

create index the_group_id_user_create_index
    on the_group (id_user_create);

create index messenger_content_index
    on messenger (content);