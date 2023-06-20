package it.intesys.codylab.rookie.dto;

import java.util.List;

public class DoctorDTO {
    private Long ID;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private  String address;
    private String phoneNumber;
    private String profession;

    public List<PatientDTO> getLatestPatients() {
        return latestPatients;
    }

    public void setLatestPatients(List<PatientDTO> latestPatients) {
        this.latestPatients = latestPatients;
    }

    private List<PatientDTO> latestPatients;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


}
