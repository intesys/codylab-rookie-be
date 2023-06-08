package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRepository extends RookieRepository {
    @Autowired
    DoctorRepository doctorRepository;

    // https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/jdbc.html
    public void save (Patient patient) {
        Long lastDoctorVisitedId = patient.getLastDoctorVisited() != null ? patient.getLastDoctorVisited().getId() : null;
        Long id = db.queryForObject("select nextval('id_generator')", Long.class);
        db.update("insert into patient (id, opd, idp, name, surname, phone_number, address, email, avatar, blood_group, notes, chronic_patient, last_admission, last_doctor_visited_id) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            id,
            patient.getOpd(),
            patient.getIdp(),
            patient.getName(),
            patient.getSurname(),
            patient.getPhoneNumber(),
            patient.getAddress(),
            patient.getEmail(),
            patient.getAvatar(),
            patient.getBloodGroup() != null? patient.getBloodGroup().toString(): null,
            patient.getNotes(),
            patient.getChronicPatient(),
            patient.getLastAdmission(),
                lastDoctorVisitedId);
        patient.setId(id);
        if (lastDoctorVisitedId != null)
            patient.setLastDoctorVisited(doctorRepository.findById(lastDoctorVisitedId));
    }
}
