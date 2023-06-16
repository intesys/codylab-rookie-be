package it.intesys.codylab.rookie.dto;

import java.time.Instant;

public class PatientDTO {
    private Long id;
    private Long opd;
    private Long idp;
    private String name;
    private String surname;
    private  String address;
    private Long phoneNumber;
    private String avatar;
    private BloodGroupDTO bloodGroup;
    private String note;
    private Boolean chronicPatient;
    private Instant lastAdmissions;
    private Long lastDoctorVisitedId;
    private  String email;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BloodGroupDTO getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroupDTO bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getChronicPatient() {
        return chronicPatient;
    }

    public void setChronicPatient(Boolean chronicPatient) {
        this.chronicPatient = chronicPatient;
    }

    public Instant getLastAdmissions() {
        return lastAdmissions;
    }

    public void setLastAdmissions(Instant lastAdmissions) {
        this.lastAdmissions = lastAdmissions;
    }

    public Long getLastDoctorVisitedId() {
        return lastDoctorVisitedId;
    }

    public void setLastDoctorVisitedId(Long lastDoctorVisitedId) {
        this.lastDoctorVisitedId = lastDoctorVisitedId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
