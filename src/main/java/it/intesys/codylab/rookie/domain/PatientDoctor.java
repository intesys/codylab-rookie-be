package it.intesys.codylab.rookie.domain;

import java.time.Instant;
import java.util.Objects;

public class PatientDoctor {
    private Patient patient;

    private Doctor doctor;
    private Instant date;

    public PatientDoctor() {

    }

    public PatientDoctor(Patient patient, Doctor doctor, Instant date) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
    }

    public PatientDoctor(Patient patient, Doctor doctor) {
        this.patient = patient;
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientDoctor that = (PatientDoctor) o;
        return patient.equals(that.patient) && doctor.equals(that.doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient, doctor);
    }
}
