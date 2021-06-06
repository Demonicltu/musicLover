create table if not exists FAVOURITE_ARTIST
(
    ID      LONG    not null,
    ARTIST  VARCHAR not null,
    USER_ID LONG    not null,
    constraint FAVOURITE_ARTIST_PK
        primary key (ID)
);

create index FAVOURITE_ARTIST_USER_ID_INDEX
    on FAVOURITE_ARTIST (USER_ID);
