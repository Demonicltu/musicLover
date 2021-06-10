create table if not exists USER_ARTIST
(
    USER_ID   INT not null,
    ARTIST_ID INT not null,
    primary key (USER_ID, ARTIST_ID),
    constraint USER_ARTIST_ARTIST_ID_FK
        foreign key (ARTIST_ID) references ARTIST (ID),
    constraint USER_ARTIST_USER_ID_FK
        foreign key (USER_ID) references USER (ID)
);

create unique index USER_ARTIST_ARTIST_ID_USER_ID_UINDEX
    on USER_ARTIST (ARTIST_ID, USER_ID)