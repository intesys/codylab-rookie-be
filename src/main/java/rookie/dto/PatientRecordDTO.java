package rookie.dto;

import java.time.Instant;

public class PatientRecordDTO {
    private Long id;
    private Long patientId;
    private DoctorDTO doctor;
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
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
}
