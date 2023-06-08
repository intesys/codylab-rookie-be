package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class DoctorRepository extends RookieRepository {
    // https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/jdbc.html
    public void save (Doctor doctor) {
        if (doctor.getId() == null) {
            Long id = db.queryForObject("select nextval('id_generator')", Long.class);
            db.update("insert into doctor (id, name, surname, phone_number, address, email, avatar, profession) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?)",
                id,
                doctor.getName(),
                doctor.getSurname(),
                doctor.getPhoneNumber(),
                doctor.getAddress(),
                doctor.getEmail(),
                doctor.getAvatar(),
                doctor.getProfession());
            doctor.setId(id);
        } else {
            db.update("update doctor set name = ?, surname = ?, phone_number = ?, address = ?, email = ?, avatar = ?, profession = ? " +
                            "where id = ?",
                doctor.getName(),
                doctor.getSurname(),
                doctor.getPhoneNumber(),
                doctor.getAddress(),
                doctor.getEmail(),
                doctor.getAvatar(),
                doctor.getProfession(),
                doctor.getId());

        }
    }

    public Page<Doctor> findAll(Pageable pageable, String name, String surname, String profession) {
        StringBuilder queryBuffer = new StringBuilder();
        List<Object> parameters = new ArrayList<>();
        String whereOrAnd = "where ";
        final String and = "and ";
        String orderSep = "";

        queryBuffer.append("select * from doctor ");
        if (name != null) {
            queryBuffer.append(whereOrAnd).append("name = ? ");
            parameters.add(name);
            whereOrAnd = and;
        }
        if (surname != null) {
            queryBuffer.append(whereOrAnd).append("surname = ? ");
            parameters.add(surname);
            whereOrAnd = and;
        }
        if (profession != null) {
            queryBuffer.append(whereOrAnd).append("profession = ? ");
            parameters.add(profession);
        }

        String query = pagingQuery(pageable, queryBuffer);

        List<Doctor> doctors = db.query(query, this::map, parameters.toArray(Object[]::new));
        return new PageImpl<>(doctors, pageable, 0);
    }

    private Doctor map(ResultSet rs, int rownum) throws SQLException {
        Doctor doctor = new Doctor();
        String address = rs.getString("address");
        if (!rs.wasNull())
            doctor.setAddress(address);
        String avatar = rs.getString("avatar");
        if (!rs.wasNull())
            doctor.setAvatar(avatar);
        String email = rs.getString("email");
        if (!rs.wasNull())
            doctor.setEmail(email);
        long id = rs.getLong("id");
        if (!rs.wasNull())
            doctor.setId(id);
        String name = rs.getString("name");
        if (!rs.wasNull())
            doctor.setName(name);
        String phoneNumber = rs.getString("phone_number");
        if (!rs.wasNull())
            doctor.setPhoneNumber(phoneNumber);
        String surname = rs.getString("surname");
        if (!rs.wasNull())
            doctor.setSurname(surname);
        String profession = rs.getString("profession");
        if (!rs.wasNull())
            doctor.setProfession(profession);
        return doctor;
    }

    public Doctor findById(Long id) {
        return db.queryForObject("select * from doctor where id = ? ", this::map, id);
    }

    public void deleteById(Long id) {
        int update = db.update("delete from doctor where id = ? ", id);
        if (update == 0)
            throw new NoSuchElementException(String.valueOf(id));
    }
    public List<Doctor> findByPatient(Patient patient) {
        return db.query("select * from doctor where id in (select doctor_id from patient_doctor where patient_id = ?)", this::map, patient.getId());
    }
}
