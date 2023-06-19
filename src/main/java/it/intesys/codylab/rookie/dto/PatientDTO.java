package it.intesys.codylab.rookie.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PatientDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long opd;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long idp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String surname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BloodGroupDTO bloodGroup;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String note;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean chronicPatient;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant lastAdmission;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long lastDoctorVisitedId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PatientRecordDTO> patientRecords;

/*
        patientRecords:
          type: array
          items:
            $ref: '#/components/schemas/PatientRecordDTO'
        doctorIds:
          type: array
          items:
            type: integer
            format: int64
 */

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

    public Instant getLastAdmission() {
        return lastAdmission;
    }

    public void setLastAdmission(Instant lastAdmission) {
        this.lastAdmission = lastAdmission;
    }

    public Long getLastDoctorVisitedId() {
        return lastDoctorVisitedId;
    }

    public void setLastDoctorVisitedId(Long lastDoctorVisitedId) {
        this.lastDoctorVisitedId = lastDoctorVisitedId;
    }

    public List<PatientRecordDTO> getPatientRecords() {
        if (patientRecords == null)
            patientRecords = new ArrayList<>();
        return patientRecords;
    }

    public void setPatientRecords(List<PatientRecordDTO> patientRecords) {
        this.patientRecords = patientRecords;
    }
}
