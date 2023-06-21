CREATE TABLE patient_record (
    id bigint not null,
    doctor_id bigint not null,
    patient_id bigint not null,
    date timestamp not null,
	type_visit varchar(64) not null,
	reason_visit varchar(1024) not null,
	treatment_made varchar(1024) not null,
	CONSTRAINT patient_record_pk PRIMARY KEY (id),
    CONSTRAINT fk_patient_record_doctor FOREIGN KEY(doctor_id) REFERENCES doctor(id),
    CONSTRAINT k_patient_record_patient FOREIGN KEY(patient_id) REFERENCES patient(id)
);