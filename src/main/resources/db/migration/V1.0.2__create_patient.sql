CREATE TABLE patient (
    id bigint not null,
        opd bigint,
        idp bigint,
    	name varchar(128),
    	surname varchar(128),
    	phone_number bigint,
    	address varchar(1024),
    	email varchar(256),
    	avatar varchar(128),
        blood_group varchar (32),
        note varchar (2048),
        chronic_patient boolean,
        last_admission timestamp,
        last_doctor_visited_id bigint,
    	CONSTRAINT patient_pkey PRIMARY KEY (id),
        CONSTRAINT fk_doctor FOREIGN KEY(last_doctor_visited_id) REFERENCES doctor(id)
)