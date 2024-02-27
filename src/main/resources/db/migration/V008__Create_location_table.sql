CREATE table location(
    USER int,
    LATITUDE double not null,
    LONGITUDE double not null,
    FOREIGN KEY (USER) REFERENCES user(ID)
);