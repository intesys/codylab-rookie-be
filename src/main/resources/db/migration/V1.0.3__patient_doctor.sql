CREATE TABLE patient_doctor (
    patient_id bigint references patient (id),
    doctor_id bigint references doctor (id),
    date timestamp,
    primary key (patient_id, doctor_id)
);

create index visit_index on patient_doctor (patient_id, date);

