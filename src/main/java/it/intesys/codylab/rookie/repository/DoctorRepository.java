package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    public void saveDoctor(Doctor doctor) {
        Long id = db.queryForObject("select nextval('id_generator')", Long.class);
        db.update("insert into doctor (id, name, surname, phone_number, address, email, avatar, profession) " + "values (?, ?, ?, ?, ?, ?, ?, ?)", id,
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
        StringBuilder buffer = new StringBuilder("select * from doctor");
        String whereOrAnd = "where";
        final String and= "and";
        List < Object> parameterList = new ArrayList<>();


        if(name!= null && !name.isBlank()){
            buffer.append(whereOrAnd).append(' ').append("name = ? ");
            whereOrAnd = and;
            parameterList.add(name);
        }

        if(surname!= null && !surname.isBlank()){
            buffer.append(whereOrAnd).append(' ').append("surname = ?");
            whereOrAnd = and;
            parameterList.add(surname);
        }
        if(profession!= null && !profession.isBlank()){
            buffer.append(whereOrAnd).append(' ').append("profession = ?");
            whereOrAnd = and;
            parameterList.add(profession);
        }


        String query = buffer.toString();

        Object[] parameters = parameterList.toArray();
        //parameters diventerà Args
        return  db.query(query ,this::map, parameters);
    }

    private Doctor map(ResultSet resultSet, int i) throws SQLException {
        //posizionamento su prima riga resultSet.next()
        Doctor doctor = new Doctor();

        Long id= resultSet.getLong("id");
        if(!resultSet.wasNull())
            doctor.setId(id);
       String name= resultSet.getString("name");
        if(!resultSet.wasNull())
            doctor.setName(name);
        String surname= resultSet.getString("surname");
        if(!resultSet.wasNull())
            doctor.setSurname(surname);
        String phoneNumber= resultSet.getString("phoneNumber");
        if(!resultSet.wasNull())
            doctor.setPhoneNumber(phoneNumber);
        String address= resultSet.getString("address");
        if(!resultSet.wasNull())
            doctor.setAddress(address);
        String email= resultSet.getString("email");
        if(!resultSet.wasNull())
            doctor.setEmail(email);
        String avatar= resultSet.getString("avatar");
        if(!resultSet.wasNull())
            doctor.setAvatar(avatar);
        String profession = resultSet.getString("profession");
        if(!resultSet.wasNull())
            doctor.setProfession(profession);

        return  doctor;

    }
}
