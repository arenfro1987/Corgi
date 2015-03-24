create table USER_TABLE (
    USER_ID integer not null generated always 
        as identity (start with 1, increment by 1),
    EMAIL varchar(50) not null,
    UCO_ID varchar(10),
    PASSWORD char(64) not null, /*SHA-256 Encrypted*/
    FIRST_NAME varchar(20) not null,
    LAST_NAME varchar(20) not null,
    primary key(USER_ID)    
);
/*
    studentgroup, secretarygroup, admingroup
*/
create table GROUP_TABLE (    
    GROUPNAME varchar(20),
    EMAIL varchar(50) not null,
    USER_ID integer,
    primary key(GROUPNAME, USER_ID),
    foreign key(USER_ID, EMAIL) references USER_TABLE(USER_ID, EMAIL)
);

create table COURSE (
    DEPARTMENT varchar(4),
    COURSE_NUMBER varchar(4),
    primary key(DEPARTMENT, COURSE_NUMBER)
);

create table HAS_TAKEN (
    USER_ID integer,
    DEPARTMENT varchar(4),
    COURSE_NUMBER varchar(4),
    primary key(USER_ID, DEPARTMENT, COURSE_NUMBER),
    foreign key(USER_ID) references USER_TABLE(USER_ID),
    foreign key(DEPARTMENT, COURSE_NUMBER) references COURSE(DEPARTMENT, COURSE_NUMBER)
);

create table COURSE_OFFERING (
    CRN varchar(10),
    DEPARTMENT varchar(4),
    COURSE_NUMBER varchar(4),
    primary key(CRN, DEPARTMENT, COURSE_NUMBER),
    foreign key(DEPARTMENT, COURSE_NUMBER) references COURSE(DEPARTMENT, COURSE_NUMBER)
);

create table COURSE_PREREQUISITE (
    PRE_DEPARTMENT varchar(4),
    PRE_COURSE_NUMBER varchar(4),
    POST_DEPARTMENT varchar(4),
    POST_COURSE_NUMBER varchar(4),
    primary key(PRE_DEPARTMENT, PRE_COURSE_NUMBER, POST_DEPARTMENT, POST_COURSE_NUMBER),
    foreign key(PRE_DEPARTMENT, PRE_COURSE_NUMBER) references COURSE(DEPARTMENT, COURSE_NUMBER ),
    foreign key(POST_DEPARTMENT, POST_COURSE_NUMBER) references COURSE(DEPARTMENT, COURSE_NUMBER)
);
/*
    proposed schedule for the next semester
*/
create table PROPOSED_SCHEDULE (
    USER_ID integer,
    CRN varchar(10),
    DEPARTMENT varchar(4),
    COURSE_NUMBER varchar(4),
    primary key(USER_ID, CRN),
    foreign key(USER_ID) references USER_TABLE(USER_ID),
    foreign key(CRN, DEPARTMENT, COURSE_NUMBER) references COURSE_OFFERING(CRN, DEPARTMENT, COURSE_NUMBER)
);


create table MEETING (
    MEETING_ID integer not null generated always 
        as identity (start with 1, increment by 1),
    MEETING_DATE date,
    MEETING_TIME time,
    PROFESSOR integer,
    STUDENT integer,
    primary key(MEETING_ID),
    foreign key(PROFESSOR) references USER_TABLE(USER_ID),
    foreign key(STUDENT) references USER_TABLE(USER_ID)
);
