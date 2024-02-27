CREATE TABLE friends(
    USER_ID int not null,
    FRIEND_ID int not null,
    FOREIGN KEY (USER_ID) REFERENCES user(ID),
    FOREIGN KEY (FRIEND_ID) REFERENCES user(ID)
);