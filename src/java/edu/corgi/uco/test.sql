
create table UserTable(
    userID integer not null generated always 
        as identity (start with 1, increment by 1),
    email varchar(50) not null,
    ucoID varchar(10),
    password char(64) not null, /*SHA-256 Encrypted*/
    firstName varchar(20) not null,
    lastName varchar(20) not null
    primary key(userID)    
);