
drop table Appointment;
drop table MajorCodes;
drop table GroupTable;
drop table Schedule;
drop table CourseScheduleLinkage;
drop table TakenCourses;
drop table IsPreReq;
drop table IsCoReq;
drop table MajorReq;
drop table CourseOffering;
drop table Course;
drop table UserTable;

create table UserTable (
    userID integer not null generated always 
        as identity (start with 1, increment by 1),
    email varchar(50) not null,
    ucoID varchar(10),
    password char(64) not null, /*SHA-256 Encrypted*/
    firstName varchar(20) not null,
    lastName varchar(20) not null,
    primary key(userID)    
);

create table Course (
    courseID integer not null generated always 
        as identity(start with 1, increment by 1),
    hours integer,
    dept varchar(30) not null,
    courseNumber integer not null,
    title varchar(200) not null,
    primary key(courseID)
);

create table CourseOffering (
    courseOfferingID integer not null generated always 
        as identity(start with 1, increment by 1),
    courseNumber integer not null,
    semester varchar(15),
    yearOffered integer,
    primary key(courseOfferingID)
);


create table Appointment(
    appointmentID integer not null generated always
        as identity(start with 1, increment by 1),
    appointmentDate Date not null,
    duration integer,
    userID integer,
    primary key(appointmentID)
);

create table MajorCodes(
    userID integer,
    majorCode varchar(4),
    primary key(userID, majorCode)
);

create table GroupTable(
    userID integer,
    groupname varchar(50),
    email varchar(50),
    groupID integer not null generated always 
        as identity (start with 1, increment by 1),
    primary key(groupID)
);

create table Schedule(
    scheduleID integer not null generated always 
        as identity (start with 1, increment by 1),
    userID integer,
    approved boolean,
    holdRemoved boolean,
    primary key(scheduleID)
);

create table CourseScheduleLinkage(
    scheduleID integer,
    courseID integer,
    primary key(scheduleID, courseID)
);

create table TakenCourses(
    courseID integer,
    userID integer,
    grade varchar(1),
    primary key(courseID, userID)
);

create table IsPreReq(
    mainCourseID  integer,
    preReqCourseID integer,
    primary key(mainCourseID, preReqCourseID)
);

create table IsCoReq(
    mainCourseID integer,
    coReqCourseID integer,
    primary key(mainCourseID, coReqCourseID)
);

create table MajorReq (
    majorCode integer,
    courseID integer,
    primary key(majorCode, courseID)
);