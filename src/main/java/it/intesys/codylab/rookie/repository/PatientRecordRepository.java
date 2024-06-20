package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.domain.PatientRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class PatientRecordRepository extends RookieRepository {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;

    // https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/jdbc.html
    public void save (PatientRecord patientRecord) {
        Long doctor = Optional.ofNullable(patientRecord.getDoctor()).map(Doctor::getId).orElse(null);
        Long patient = Optional.ofNullable(patientRecord.getPatient()).map(Patient::getId).orElse(null);
        if (patientRecord.getId() == null) {
            Long id = db.queryForObject("select nextval('id_generator')", Long.class);
            db.update("insert into patient_record (id, date, doctor_id, patient_id, reason_visit, treatment_made, type_visit) " +
                "values (?, ?, ?, ?, ?, ?, ?)",
                id,
                Timestamp.from(patientRecord.getDate()),
                doctor,
                patient,
                patientRecord.getReasonVisit(),
                patientRecord.getTreatmentMade(),
                patientRecord.getTypeVisit());
            patientRecord.setId(id);
        } else {
            db.update("update patient_record set date = ?, doctor_id = ?, patient_id = ?, reason_visit = ?, treatment_made = ?, type_visit  = ? " +
                "where id = ?",
                Timestamp.from(patientRecord.getDate()),
                doctor,
                patient,
                patientRecord.getReasonVisit(),
                patientRecord.getTreatmentMade(),
                patientRecord.getTypeVisit(),
                patientRecord.getId());
        }
    }

    private PatientRecord map(ResultSet rs, int rownum) throws SQLException {
        PatientRecord patientRecord = new PatientRecord();
        Timestamp date = rs.getTimestamp("date");
        if (!rs.wasNull())
            patientRecord.setDate(date.toInstant());
        long doctor = rs.getLong("doctor_id");
        if (!rs.wasNull())
            patientRecord.setDoctor(doctorRepository.findById(doctor));
        long patient = rs.getLong("patient_id");
        if (!rs.wasNull())
            patientRecord.setPatient(patientRepository.findById(patient));
        long id = rs.getLong("id");
        if (!rs.wasNull())
            patientRecord.setId(id);
        String reasonVisit = rs.getString("reason_visit");
        if (!rs.wasNull())
            patientRecord.setReasonVisit(reasonVisit);
        String treatmentMade = rs.getString("treatment_made");
        if (!rs.wasNull())
            patientRecord.setTreatmentMade(treatmentMade);
        String typeVisit = rs.getString("type_visit");
        if (!rs.wasNull())
            patientRecord.setTypeVisit(typeVisit);
        return patientRecord;
    }
    public PatientRecord findById(Long id) {
        try {
            return db.queryForObject("select * from patient_record where id = ? ", this::map, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        int update = db.update("delete from patient_record where id = ? ", id);
        if (update == 0)
            throw new NoSuchElementException(String.valueOf(id));
    }
    public List<PatientRecord> findByPatient(Patient patient) {
        return db.query("select * from patient_record where patient_id = ? order by date desc", this::map, patient.getId());
    }

    public List<PatientRecord> findLatestByPatient(Patient patient, int limit) {
        StringBuilder queryBuffer = new StringBuilder("select * from patient_record where patient_id = ?");

        List<Object> parameters = List.of(patient.getId());

        PageRequest pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.desc("date")));
        String query = pagingQuery(pageable, queryBuffer);

        return db.query(query, this::map, parameters.toArray(Object[]::new));
    }

}
