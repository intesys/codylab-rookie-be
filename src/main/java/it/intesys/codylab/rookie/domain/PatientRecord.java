package it.intesys.codylab.rookie.domain;

import java.time.Instant;
import java.util.Objects;

public class PatientRecord {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private Instant date;
    private String typeVisit;
    private String reasonVisit;
    private String treatmentMade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTypeVisit() {
        return typeVisit;
    }

    public void setTypeVisit(String typeVisit) {
        this.typeVisit = typeVisit;
    }

    public String getReasonVisit() {
        return reasonVisit;
    }

    public void setReasonVisit(String reasonVisit) {
        this.reasonVisit = reasonVisit;
    }

    public String getTreatmentMade() {
        return treatmentMade;
    }

    public void setTreatmentMade(String treatmentMade) {
        this.treatmentMade = treatmentMade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecord that = (PatientRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
