create table if not exists USER
(
    ID       LONG    not null,
    NAME     VARCHAR not null,
    USERNAME VARCHAR not null,
    PASSWORD VARCHAR not null,
    UUID     VARCHAR not null,
    constraint USER_PK
        primary key (ID)
);

create index USER_USERNAME_PASSWORD_INDEX
    on USER (USERNAME, PASSWORD);

create index USER_UUID_INDEX
    on USER (UUID);
