package rookie.dto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatientDTO {
    private Long id;
    private Long opd;
    private Long idp;
    private String name;
    private String surname;
    private Long phoneNumber;
    private String address;
    private String email;
    private String avatar;
    private BloodGroupDTO bloodGroup;
    private String note;
    private Boolean chronicPatient;
    private Instant lastAdmission;
    private Long lastDoctorVisitedId;
    private List<PatientRecordDTO> patientRecords;
    private List<Long> doctorIds;

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

    public List<Long> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<Long> doctorIds) {
        this.doctorIds = doctorIds;
    }
}
