package org.example.repository;

import org.example.domain.Doctor;
import org.example.exceptions.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoctorRepository {
    @Autowired
    private JdbcTemplate db;
    public void save(Doctor doctor) throws NotFound {
        Long id = doctor.getId();
        if (id == null) {
            create(doctor);
        } else {
            update(doctor, id);
        }
    }

    private void update(Doctor doctor, Long id) {
        findById(id);
        db.update("update doctor set name = ?, surname = ?, phone_number = ?, address = ?, email = ?, avatar = ?, profession = ? " +
                        "where id = ?",
                doctor.getName(),
                doctor.getSurname(),
                doctor.getPhoneNumber(),
                doctor.getAddress(),
                doctor.getEmail(),
                doctor.getAvatar(),
                doctor.getProfession(),
                id);
    }

    private void create(Doctor doctor) {
        Long id = db.queryForObject("select nextval ('id_generator')", Long.class);
        db.update("insert into doctor (id, name, surname, phone_number, address, email, avatar, profession)" +
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
    }

    public List<Doctor> getDoctors(Pageable pageable, String name, String surname, String profession) {
        StringBuilder buffer = new StringBuilder("select * from doctor ");
        String whereOrAnd = "where";
        final String and = "and";

        List<Object> parameterList = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            buffer.append(whereOrAnd).append(' ').append("name = ? ");
            whereOrAnd = and;
            parameterList.add(name);
        }

        if (surname != null && !surname.isBlank()) {
            buffer.append(whereOrAnd).append(' ').append("surname = ? ");
            whereOrAnd = and;
            parameterList.add(surname);
        }

        if (profession != null && !profession.isBlank()) {
            buffer.append(whereOrAnd).append(' ').append("profession = ? ");
            whereOrAnd = and;
            parameterList.add(profession);
        }

        String query = page (buffer, pageable);

        Object[] parameters = parameterList.toArray();
        return db.query(query, this::map, parameters);
    }

    private String page(StringBuilder buffer, Pageable pageable) {
        Sort sort = pageable.getSort();
        buffer.append(' ');
        if (!sort.isEmpty()) {
            buffer.append("order by ");
            sort.stream()
                    .forEach(order -> {
                        String property = order.getProperty();
                        Sort.Direction direction = order.getDirection();
                        buffer.append(property);
                        if (direction == Sort.Direction.DESC)
                            buffer.append(' ').append("desc").append(' ');
                    });
        }
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        buffer.append("limit").append(' ').append(limit).append(' ')
                .append("offset").append(' ').append(offset).append(' ');
        return buffer.toString();
    }

    private Doctor map(ResultSet resultSet, int i) throws SQLException {
        Doctor doctor = new Doctor();
        Long id = resultSet.getLong("id");
        if (!resultSet.wasNull())
            doctor.setId(id);
        String name = resultSet.getString("name");
        if (!resultSet.wasNull())
            doctor.setName(name);
        String surname = resultSet.getString("surname");
        if (!resultSet.wasNull())
            doctor.setSurname(surname);
        String phoneNumber = resultSet.getString("phone_number");
        if (!resultSet.wasNull())
            doctor.setPhoneNumber(phoneNumber);
        String address = resultSet.getString("address");
        if (!resultSet.wasNull())
            doctor.setAddress(address);
        String email = resultSet.getString("email");
        if (!resultSet.wasNull())
            doctor.setEmail(email);
        String avatar = resultSet.getString("avatar");
        if (!resultSet.wasNull())
            doctor.setAvatar(avatar);
        String profession = resultSet.getString("profession");
        if (!resultSet.wasNull())
            doctor.setProfession(profession);
        return doctor;
    }

    public Doctor findById(Long id) throws NotFound {
        try {
            return db.queryForObject("select * from doctor where id = ?", this::map, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFound(Doctor.class, id);
        }
    }

    public void remove(Long id)  throws NotFound {
        findById(id);
        db.update("delete from doctor where id = ?", id);
    }
}
// Object... args
// var args vuol dire che Object e' un array e quindi numero parametri variabile