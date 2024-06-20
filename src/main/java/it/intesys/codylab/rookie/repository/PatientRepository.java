package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.BloodGroup;
import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.domain.PatientDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class PatientRepository extends RookieRepository {
    @Autowired
    DoctorRepository doctorRepository;

    // https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/jdbc.html
    public void save (Patient patient) {
        Long lastDoctorVisitedId = patient.getLastDoctorVisited() != null ? patient.getLastDoctorVisited().getId() : null;
        if (patient.getId() == null) {
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
        } else {
            db.update("update patient set opd = ?, idp = ?, name = ?, surname = ?, phone_number = ?, address = ?, " +
                            "email = ?, avatar = ?, blood_group = ?, notes = ?, chronic_patient = ?, last_admission = ?, last_doctor_visited_id  = ? " +
                            "where id = ?",
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
                    lastDoctorVisitedId,
                    patient.getId());
        }
        if (lastDoctorVisitedId != null)
            patient.setLastDoctorVisited(doctorRepository.findById(lastDoctorVisitedId));

        List<PatientDoctor> patientDoctors = patient.getDoctors();
        if (patientDoctors != null) {
            List<PatientDoctor> currentDoctors = doctorRepository.findByPatient(patient)
                    .stream()
                    .map(doctor -> new PatientDoctor(patient, doctor))
                    .toList();

            List<PatientDoctor> insertions = subtract(patientDoctors, currentDoctors);
            List<PatientDoctor> updates = intersect(patientDoctors, currentDoctors);
            List<PatientDoctor> deletions = subtract(currentDoctors, patientDoctors);
            db.batchUpdate("insert into patient_doctor (patient_id, doctor_id, date) values (?, ?, ?)", insertions, 100, (statement, op) -> {
                statement.setLong(1, op.getPatient().getId());
                statement.setLong(2, op.getDoctor().getId());
                statement.setTimestamp(3, Timestamp.from(op.getDate()));
            });
            db.batchUpdate("update patient_doctor set date = ? where patient_id = ? and doctor_id = ?", updates, 100, (statement, op) -> {
                statement.setTimestamp(1, Timestamp.from(op.getDate()));
                statement.setLong(2, op.getPatient().getId());
                statement.setLong(3, op.getDoctor().getId());
            });
            db.batchUpdate("delete from patient_doctor where patient_id = ? and doctor_id = ?", deletions, 100, (statement, op) -> {
                statement.setLong(1, op.getPatient().getId());
                statement.setLong(2, op.getDoctor().getId());
            });
        }
    }


    public Page<Patient> findAll(Pageable pageable, String text, Long id, Long opd, Long idp, Doctor doctor) {
        StringBuilder queryBuffer = new StringBuilder("select * from patient a ");
        String whereOrAnd = "where ";
        final String and = "and ";
        List<Object> parameters = new ArrayList<>();

        if (doctor != null) {
            queryBuffer.append(" join patient_doctor b on b.patient_id = a.id ");
        }

        if (text != null && !text.isBlank()) {
            List<Object> words = Arrays.asList(text.split("\\s+"));
            parameters.addAll(Collections.nCopies(3, words)
                    .stream()
                    .flatMap(Collection::stream).toList());
            String inSql = String.join(",", Collections.nCopies(words.size(), "?"));
            queryBuffer.append(whereOrAnd).append(String.format("a.name in (%1$s) or a.surname in (%1$s) or a.email in (%1$s) ", inSql));
            whereOrAnd = and;
        }

        if (id != null) {
            queryBuffer.append(whereOrAnd).append("id = ? ");
            parameters.add(id);
            whereOrAnd = and;
        }

        if (opd != null) {
            queryBuffer.append(whereOrAnd).append("opd = ? ");
            parameters.add(opd);
            whereOrAnd = and;
        }

        if (idp != null) {
            queryBuffer.append(whereOrAnd).append("idp = ? ");
            parameters.add(idp);
            whereOrAnd = and;
        }

        if (doctor != null) {
            queryBuffer.append(whereOrAnd).append("doctor_id = ? ");
            parameters.add(doctor.getId());
        }

        String query = pagingQuery(pageable, queryBuffer);

        List<Patient> patients = db.query(query, this::map, parameters.toArray(Object[]::new));
        return new PageImpl<>(patients, pageable, 0);
    }

    public Page<Patient> findLatestByDoctor(Doctor doctor, int limit) {
        StringBuilder queryBuffer = new StringBuilder("select a.* from patient a " +
                "join patient_doctor b on a.id = b.patient_id " +
                "where b.doctor_id = ? ");

        List<Object> parameters = List.of(doctor.getId());

        PageRequest pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.desc("date")));
        String query = pagingQuery(pageable, queryBuffer);

        List<Patient> patients = db.query(query, this::map, parameters.toArray(Object[]::new));
        return new PageImpl<>(patients, pageable, 0);
    }

    private Patient map(ResultSet rs, int rownum) throws SQLException {
        Patient patient = new Patient();
        Long id = rs.getLong("id");
        if (!rs.wasNull())
            patient.setId(id);
        String address = rs.getString("address");
        if (!rs.wasNull())
            patient.setAddress(address);
        String avatar = rs.getString("avatar");
        if (!rs.wasNull())
            patient.setAvatar(avatar);
        String bloodGroup = rs.getString("blood_group");
        if (!rs.wasNull())
            patient.setBloodGroup(BloodGroup.valueOf(bloodGroup));
        boolean chronicPatient = rs.getBoolean("chronic_patient");
        if (!rs.wasNull())
            patient.setChronicPatient(chronicPatient);
        String email = rs.getString("email");
        if (!rs.wasNull())
            patient.setEmail(email);
        long idp = rs.getLong("idp");
        if (!rs.wasNull())
            patient.setIdp(idp);
        Timestamp lastAdmission = rs.getTimestamp("last_admission");
        if (!rs.wasNull())
            patient.setLastAdmission(lastAdmission.toInstant());
        long lastDoctorVisited = rs.getLong("last_doctor_visited_id");
        if (!rs.wasNull())
            patient.setLastDoctorVisited(doctorRepository.findById(lastDoctorVisited));
        String name = rs.getString("name");
        if (!rs.wasNull())
            patient.setName(name);
        String notes = rs.getString("notes");
        if (!rs.wasNull())
            patient.setNotes(notes);
        long opd = rs.getLong("opd");
        if (!rs.wasNull())
            patient.setOpd(opd);
        long phoneNumber = rs.getLong("phone_number");
        if (!rs.wasNull())
            patient.setPhoneNumber(phoneNumber);
        String surname = rs.getString("surname");
        if (!rs.wasNull())
            patient.setSurname(surname);
        return patient;
    }

    public Patient findById(Long id) {
        try {
            return db.queryForObject("select * from patient where id = ? ", this::map, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        int update = db.update("delete from patient where id = ? ", id);
        if (update == 0)
            throw new NoSuchElementException(String.valueOf(id));
    }

}
