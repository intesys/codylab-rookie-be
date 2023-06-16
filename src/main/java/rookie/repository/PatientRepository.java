package rookie.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rookie.domain.Doctor;
import rookie.domain.Patient;
import rookie.domain.BloodGroup;
import rookie.exceptions.NotFound;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository extends RookieRepository {
    @Autowired
    private JdbcTemplate db;
    @Autowired
    private DoctorRepository doctorRepository;
    public void save(Patient patient) throws NotFound{
        Long id = patient.getId();
        if(id == null){
            create(patient);
        }else{
            update(patient, id);
        }

    }
    private void create(Patient patient){
        Long id = db.queryForObject("select nextval('id_generator')", Long.class);
        db.update("insert into patient (id, address, avatar, blood_group, chronic_patient, email, idp, last_admission, " +
                "last_doctor_visited_id, name, note, opd, phone_number, surname) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id,
                patient.getAddress(),
                patient.getAvatar(),
                Optional.ofNullable(patient.getBloodGroup()).map(BloodGroup::name).orElse(null),
                patient.getChronicPatient(),
                patient.getEmail(),
                patient.getIdp(),
                patient.getLastAdmission(),
                Optional.ofNullable(patient.getLastDoctorVisited()).map(Doctor::getId).orElse(null),
                patient.getName(),
                patient.getNote(),
                patient.getOpd(),
                patient.getPhoneNumber(),
                patient.getSurname());
        patient.setId(id);
    }
    private void update(Patient patient, Long id) {
        findById(id);
        db.update("update patient set address = ?, avatar = ?, blood_group = ?, chronic_patient = ?, email = ?, idp = ?, " +
                        "last_admission = ?, last_doctor_visited_id = ?, name = ?, note = ?, opd = ?, phone_number = ?, surname = ? " +
                        "where id = ?",
                patient.getAddress(),
                patient.getAvatar(),
                Optional.ofNullable(patient.getBloodGroup()).map(BloodGroup::name).orElse(null),
                patient.getChronicPatient(),
                patient.getEmail(),
                patient.getIdp(),
                patient.getLastAdmission(),
                Optional.ofNullable(patient.getLastDoctorVisited()).map(Doctor::getId).orElse(null),
                patient.getName(),
                patient.getNote(),
                patient.getOpd(),
                patient.getPhoneNumber(),
                patient.getSurname(),
                id);
    }

    public List<Patient> getPatients(Pageable pageable, String text, Long id, Long opd, Long idp, Long doctorId) {
        StringBuilder buffer = new StringBuilder("select * from patient ");
        String whereOrAnd = "where";
        final String and = "and";
        List<Object> parameterList = new ArrayList<>();

        if (text != null && !text.isBlank()) {
            buffer.append(whereOrAnd).append(' ').append("name like '%?%' or surname like '%?%' or email like '%?%' ");
            whereOrAnd = and;
            parameterList.addAll(List.of(text, text, text));
        }

        if (id != null) {
            buffer.append(whereOrAnd).append(' ').append("id = ? ");
            whereOrAnd = and;
            parameterList.add(id);
        }

        if (opd != null) {
            buffer.append(whereOrAnd).append(' ').append("opd = ? ");
            whereOrAnd = and;
            parameterList.add(opd);
        }

        if (idp != null) {
            buffer.append(whereOrAnd).append(' ').append("idp = ? ");
            whereOrAnd = and;
            parameterList.add(idp);
        }

        if (doctorId != null) {
            buffer.append(whereOrAnd).append(' ').append("doctorId = ? ");
            whereOrAnd = and;
            parameterList.add(doctorId);
        }


        String query = page(buffer, pageable);
        Object[] parameters = parameterList.toArray();
        return db.query(query, this::map, parameters);

    }

    private Patient map(ResultSet resultSet, int i) throws SQLException {
        Patient patient = new Patient();
        Long id = resultSet.getLong("id");
        if (!resultSet.wasNull())
            patient.setId(id);
        String address = resultSet.getString("address");
        if (!resultSet.wasNull())
            patient.setAddress(address);
        String avatar  = resultSet.getString("avatar");
        if (!resultSet.wasNull())
            patient.setAvatar(avatar);
        String bloodGroup  = resultSet.getString("blood_group");
        if (!resultSet.wasNull())
            patient.setBloodGroup(BloodGroup.valueOf(bloodGroup));
        Boolean chronicPatient  = resultSet.getBoolean("chronic_patient");
        if (!resultSet.wasNull())
            patient.setChronicPatient(chronicPatient);
        String email  = resultSet.getString("email");
        if (!resultSet.wasNull())
            patient.setEmail(email);
        Long idp = resultSet.getLong("idp");
        if (!resultSet.wasNull())
            patient.setIdp(idp);
        Timestamp lastAdmission = resultSet.getTimestamp("last_admission");
        if (!resultSet.wasNull())
            patient.setLastAdmission(lastAdmission.toInstant());
        Long lastDoctorVisitedId  = resultSet.getLong("last_doctor_visited_id");
        if (!resultSet.wasNull())
            patient.setLastDoctorVisited(doctorRepository.findById(lastDoctorVisitedId));
        String name = resultSet.getString("name");
        if (!resultSet.wasNull())
            patient.setName(name);
        String note  = resultSet.getString("note");
        if (!resultSet.wasNull())
            patient.setNote(note);
        Long opd = resultSet.getLong("opd");
        if (!resultSet.wasNull())
            patient.setOpd(opd);
        Long phoneNumber = resultSet.getLong("phone_number");
        if (!resultSet.wasNull())
            patient.setPhoneNumber(phoneNumber);
        String surname = resultSet.getString("surname");
        if (!resultSet.wasNull())
            patient.setSurname(surname);
        return patient;
    }

    public Patient findById(Long id) throws NotFound {
        try{
            return db.queryForObject("select * from patient where id = ?", this::map, id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFound(Patient.class, id);
        }
    }

    public void remove(Long id) throws NotFound{
        findById(id);
        db.update("delete from patient where id = ?", id);
    }
}
