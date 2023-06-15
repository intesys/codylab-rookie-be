CREATE TABLE doctor (
    id int8 NOT NULL,
    name varchar(128) NULL,
    surname varchar(128) null,
    phone_number varchar(128) null,
    address varchar(1024) null,
    email varchar(256) null,
    avatar varchar(128) null,
    profession varchar(128) null,
    CONSTRAINT doctor_pkey PRIMARY KEY (id)
)
