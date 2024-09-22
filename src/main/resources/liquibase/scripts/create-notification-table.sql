-- liquibase formatted sql

-- changeset exever:1

create table notification_task (
id              serial not null primary key,
chat_id         bigint not null,
date_created    timestamp not null,
date_to_send    timestamp not null,
note            text not null,
status          varchar(15) not null default 'CREATED',
date_sent       timestamp
);

create index date_to_send_index on notification_task (date_to_send) where status = 'SCHEDULED';