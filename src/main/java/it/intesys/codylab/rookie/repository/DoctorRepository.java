package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorRepository extends RookieRepository {
    // https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/jdbc.html
    public void save (Doctor doctor) {
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
    }
}
