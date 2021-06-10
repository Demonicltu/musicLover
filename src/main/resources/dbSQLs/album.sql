create table if not exists ALBUM
(
    ID                 LONG        not null,
    COLLECTION_NAME    VARCHAR     not null,
    COLLECTION_PRICE   FLOAT       not null,
    CURRENCY           VARCHAR(3)  not null,
    TRACK_COUNT        INT         not null,
    COPYRIGHT          VARCHAR     not null,
    COUNTRY            VARCHAR     not null,
    PRIMARY_GENRE_NAME VARCHAR     not null,
    RELEASE_DATE       VARCHAR(10) not null,
    ARTIST_ID          LONG        not null,
    CREATED            TIMESTAMP   not null,
    UPDATED            TIMESTAMP   not null,
    constraint ALBUM_PK
        primary key (ID),
    constraint ALBUM_ARTIST_ID_FK
        foreign key (ARTIST_ID) references ARTIST (ID)
);

create unique index ALBUM_COLLECTION_NAME_ARTIST_ID_UINDEX
    on ALBUM (COLLECTION_NAME, ARTIST_ID);

create index ALBUM_ARTIST_ID_INDEX
    on ALBUM (ARTIST_ID);