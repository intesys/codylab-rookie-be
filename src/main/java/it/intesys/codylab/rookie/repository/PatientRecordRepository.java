package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.domain.PatientRecord;
import it.intesys.codylab.rookie.exceptions.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRecordRepository extends RookieRepository {
    @Autowired
    private JdbcTemplate db;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void save(PatientRecord patientRecord) throws NotFound {
        Long id = patientRecord.getId();
        if (id == null) {
            create(patientRecord);
        } else {
            update(patientRecord, id);
        }
    }

    private void update(PatientRecord patientRecord, Long id) {
        findById(id);
        db.update("update patient_record set date = ?, doctor_id = ?, patient_id = ?, reason_visit = ?, treatment_made = ?, type_visit = ? " +
                        "where id = ?",
                Optional.ofNullable(patientRecord.getDate()).map(Timestamp::from).orElse(null),
                Optional.ofNullable(patientRecord.getDoctor()).map(Doctor::getId).orElse(null),
                Optional.ofNullable(patientRecord.getPatient()).map(Patient::getId).orElse(null),
                patientRecord.getReasonVisit(),
                patientRecord.getTreatmentMade(),
                patientRecord.getTypeVisit(),
                id);
    }

    private void create(PatientRecord patientRecord) {
        Long id = db.queryForObject("select nextval ('id_generator')", Long.class);
        db.update("insert into patient_record (id, date, doctor_id, patient_id, reason_visit, treatment_made, type_visit)" +
                        "values (?, ?, ?, ?, ?, ?, ?)",
                id,
                Optional.ofNullable(patientRecord.getDate()).map(Timestamp::from).orElse(null),
                Optional.ofNullable(patientRecord.getDoctor()).map(Doctor::getId).orElse(null),
                Optional.ofNullable(patientRecord.getPatient()).map(Patient::getId).orElse(null),
                patientRecord.getReasonVisit(),
                patientRecord.getTreatmentMade(),
                patientRecord.getTypeVisit());
        patientRecord.setId(id);
    }

    private PatientRecord map(ResultSet resultSet, int i) throws SQLException {
        PatientRecord patientRecord = new PatientRecord();
        Long id = resultSet.getLong("id");
        if (!resultSet.wasNull())
            patientRecord.setId(id);
        Timestamp date = resultSet.getTimestamp("date");
        if (!resultSet.wasNull())
            patientRecord.setDate(date.toInstant());
        Long doctorId = resultSet.getLong("doctor_id");
        if (!resultSet.wasNull())
            patientRecord.setDoctor(doctorRepository.findById(doctorId));
        Long patientId = resultSet.getLong("patient_id");
        if (!resultSet.wasNull())
            patientRecord.setPatient(patientRepository.findById(patientId));
        String reasonVisit = resultSet.getString("reason_visit");
        if (!resultSet.wasNull())
            patientRecord.setReasonVisit(reasonVisit);
        String treatmentMade = resultSet.getString("treatment_made");
        if (!resultSet.wasNull())
            patientRecord.setTreatmentMade(treatmentMade);
        String typeVisit = resultSet.getString("type_visit");
        if (!resultSet.wasNull())
            patientRecord.setTypeVisit(typeVisit);
        return patientRecord;
    }

    public PatientRecord findById(Long id) throws NotFound {
        try {
            return db.queryForObject("select * from patient_record where id = ?", this::map, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFound(PatientRecord.class, id);
        }
    }

    public void remove(Long id)  throws NotFound {
        findById(id);
        db.update("delete from patient_record where id = ?", id);
    }

    public List<PatientRecord> findByPatient(Patient patient) {
        return db.query("select * from patient_record where patient_id = ?", this::map, patient.getId());
    }

    public List<PatientRecord> findLatestRecordByPatient(Patient patient) {
        StringBuilder queryBuffer = new StringBuilder("select * from patient_record where patient_id = ? order by date desc");
        String query = page(queryBuffer, Pageable.ofSize(LATEST_RECORD_SIZE));
        return db.query(query, this::map, patient.getId());
    }
}