create database playlist;
use playlist;

create user 'playlist-admin'@'%' identified by 'pl@yP@ss';
grant all privileges on playlist.* to 'playlist-admin'@'%';

-- User Table (Alias는 한글 기준 10글자)
create table User (
    UID int(0) not null auto_increment,
    Alias varchar(30) not null,
    primary key (UID)
);

create table UserAuth (
    UID int(0) not null,
    ID varchar(15) null,
    PW char(41) null,
    Google varchar(255) null, -- 길이 255
    Naver varchar(64) null,
    Kakao varchar(64) null,
    foreign key (UID) references User (UID)
        on update cascade
        on delete cascade
);

-- Notice Table (Subject은 한글 기준 50글자, Content는 Text 타입)
create table Notice (
    NID int(0) not null auto_increment,
    Subject varchar(150) not null,
    Content text not null,
    Author int(0) not null,
    CDate datetime default now(),
    View smallint(0) default 0,
    primary key (NID),
    foreign key (Author) references User (UID)
        on update cascade
        on delete cascade
);

-- Playlist Table (Title은 한글 기준 20자)
create table Playlist (
    PID int(0) not null auto_increment,
    Owner int(0) not null,
    IDX tinyint(0) not null,
    Title varchar(60) not null,
    primary key (PID),
    foreign key (Owner) references User (UID)
        on update cascade
        on delete cascade
);

-- PlaylistBoard Table
create table PlaylistBoard (
    PBID int(0) not null auto_increment,
    PID int(0) not null,
    IDX tinyint(0) not null,
    Name tinytext not null,
    Url tinytext not null,
    VID varchar(20) not null,
    Thumb tinytext not null,
    Type tinyint(0) not null,
    primary key (PBID),
    foreign key (PID) references Playlist (PID)
        on update cascade
        on delete cascade
);

create table PlaylistLog (
    LID int(0) not null auto_increment,
    UID int(0) not null,
    PID int(0) not null,
    PBID int(0) not null,
    CDate datetime default now(),
    primary key (LID),
    foreign key (UID) references User (UID)
        on update cascade
        on delete cascade,
    foreign key (PID) references Playlist (PID)
        on update cascade
        on delete cascade,
    foreign key (PBID) references PlaylistBoard (PBID)
        on update cascade
        on delete cascade
);
