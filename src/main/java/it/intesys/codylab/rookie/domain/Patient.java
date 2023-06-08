package it.intesys.codylab.rookie.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patient {
    private Long id;
    private Long opd;
    private Long idp;
    private String name;
    private String surname;
    private Long phoneNumber;
    private String address;
    private String email;
    private String avatar;
    private BloodGroup bloodGroup;
    private String notes;
    private Boolean chronicPatient;
    private Instant lastAdmission;
    private Doctor lastDoctorVisited;
    private List<PatientDoctor> doctors;
    private List<PatientRecord> patientRecords;

    public Patient() {
    }
    public Patient(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOpd() {
        return opd;
    }

    public void setOpd(Long opd) {
        this.opd = opd;
    }

    public Long getIdp() {
        return idp;
    }

    public void setIdp(Long idp) {
        this.idp = idp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getChronicPatient() {
        return chronicPatient;
    }

    public void setChronicPatient(Boolean chronicPatient) {
        this.chronicPatient = chronicPatient;
    }

    public Instant getLastAdmission() {
        return lastAdmission;
    }

    public void setLastAdmission(Instant lastAdmission) {
        this.lastAdmission = lastAdmission;
    }

    public Doctor getLastDoctorVisited() {
        return lastDoctorVisited;
    }

    public void setLastDoctorVisited(Doctor lastDoctorVisited) {
        this.lastDoctorVisited = lastDoctorVisited;
    }

    public List<PatientDoctor> getDoctors() {
        if (doctors == null)
            doctors = new ArrayList<>();
        return doctors;
    }

    public void setDoctors(List<PatientDoctor> doctors) {
        this.doctors = doctors;
    }

    public List<PatientRecord> getPatientRecords() {
        if (patientRecords == null)
            patientRecords = new ArrayList<>();
        return patientRecords;
    }

    public void setPatientRecords(List<PatientRecord> patientRecords) {
        this.patientRecords = patientRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
