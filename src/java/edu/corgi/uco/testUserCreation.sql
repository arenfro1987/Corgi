/*
    password: ppp
*/

insert into USER_TABLE (EMAIL, UCO_ID, PASSWORD, FIRST_NAME, LAST_NAME) values ('student@test.com', '123456789', 
'c4289629b08bc4d61411aaa6d6d4a0c3c5f8c1e848e282976e29b6bed5aeedc7', 'Student', 'Test');
insert into USER_TABLE (EMAIL, UCO_ID, PASSWORD, FIRST_NAME, LAST_NAME) values ('admin@test.com', '987654321', 
'c4289629b08bc4d61411aaa6d6d4a0c3c5f8c1e848e282976e29b6bed5aeedc7', 'Admin', 'Test');
insert into USER_TABLE (EMAIL, UCO_ID, PASSWORD, FIRST_NAME, LAST_NAME) values ('secretary@test.com', '741852963', 
'c4289629b08bc4d61411aaa6d6d4a0c3c5f8c1e848e282976e29b6bed5aeedc7', 'Secretary', 'Test');

insert into GROUP_TABLE (GROUPNAME, EMAIL) values ('student', 'student@test.com');
insert into GROUP_TABLE (GROUPNAME, EMAIL) values ('admin', 'admin@test.com');
insert into GROUP_TABLE (GROUPNAME, EMAIL) values ('secretary', 'secretary@test.com');