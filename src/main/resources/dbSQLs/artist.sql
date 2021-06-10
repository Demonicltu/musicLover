create table if not exists ARTIST
(
    ID            LONG    not null,
    AMG_ARTIST_ID LONG    not null,
    ARTIST_NAME   VARCHAR not null,
    constraint ARTIST_PK
        primary key (ID)
);