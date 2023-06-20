package rookie.dto;

import java.util.List;

public class DoctorDTO {
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String email;
    private String avatar;
    private String profession;
    private List<PatientDTO> latestPatients;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public List<PatientDTO> getLatestPatients() {
        return latestPatients;
    }

    public void setLatestPatients(List<PatientDTO> latestPatients) {
        this.latestPatients = latestPatients;
    }
}
