CREATE TABLE USER(
    ID int not null AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    EMAIL varchar(50) not null,
    PASSWORD varchar(50) not null,
    GENDER varchar(50) not null
);