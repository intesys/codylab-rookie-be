package rookie.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rookie.domain.Doctor;

@Repository
public class DoctorRepository {
    @Autowired
    JdbcTemplate db;
    public void save(Doctor doctor) {
        Long id=db.queryForObject("select nextval('id_generator')",long.class);
        db.update("insert into doctor (id, name, surname, phone_number, address, email, avatar, profession)"+
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
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
}
