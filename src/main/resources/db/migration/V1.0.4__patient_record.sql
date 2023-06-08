CREATE TABLE patient_record (
    id bigint primary key,
    patient_id bigint,
    doctor_id bigint,
    date timestamp,
    type_visit varchar(128),
    reason_visit varchar (1024),
    treatment_made varchar (1024)
)
