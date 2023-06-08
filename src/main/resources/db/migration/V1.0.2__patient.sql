CREATE TABLE patient (
    id bigint primary key,
    opd bigint,
    idp bigint,
    name varchar(128),
    surname varchar(128),
    phone_number bigint,
    address varchar(1024),
    email varchar(256),
    avatar varchar(128),
    blood_group varchar(32),
    notes varchar(1024),
    chronic_patient boolean,
    last_admission timestamp,
    last_doctor_visited_id bigint references doctor (id)
)
