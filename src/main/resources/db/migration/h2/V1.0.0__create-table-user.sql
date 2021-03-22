create table users
(
    id         bigint auto_increment,
    username   varchar(255) /*not null*/,
    name       varchar(255) /*not null*/,
    email      varchar(255) /*not null*/,
    birth_date date /*not null*/,
    sex        varchar(10) /*not null*/
);

alter table users
    add constraint USER_PK
        primary key (id);

create unique index USER_ID_UINDEX
    on users (id);

-- create unique index USER_USERNAME_UINDEX
--     on users (username);

