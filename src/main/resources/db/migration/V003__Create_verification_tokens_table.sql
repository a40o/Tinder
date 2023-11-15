SELECT CURRENT_TIMESTAMP;

CREATE TABLE token(
    ID int not null AUTO_INCREMENT PRIMARY KEY,
    USER_ID int,
    TOKEN varchar(50) DEFAULT (uuid()),
    EXPIRATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
    CREATED_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES user(ID)
);