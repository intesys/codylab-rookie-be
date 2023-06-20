CREATE TABLE doctor (
	id int8 NOT NULL,
	name varchar(128) NULL,
	surname varchar(128) NULL,
	phone_number varchar(32) NULL,
	address varchar(1024) NULL,
	email varchar(256) NULL,
	avatar varchar(128) NULL,
	profession varchar(128) NULL,
	CONSTRAINT doctor_pkey PRIMARY KEY (id)
);
