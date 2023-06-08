package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.BloodGroup;
import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.dto.BloodGroupDTO;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    PatientRecordMapper patientRecordMapper;
    public Patient toEntity(PatientDTO patientDTO) {
        if (patientDTO == null)
            return null;

        Patient patient = new Patient();
        patient.setId(patientDTO.getId());
        patient.setAddress(patientDTO.getAddress());
        patient.setAvatar(patientDTO.getAvatar());
        if (patientDTO.getBloodGroup() != null)
            patient.setBloodGroup(BloodGroup.valueOf(patientDTO.getBloodGroup().name()));
        patient.setChronicPatient(patientDTO.getChronicPatient());
        patient.setEmail(patientDTO.getEmail());
        patient.setIdp(patientDTO.getIdp());
        patient.setLastAdmission(patientDTO.getLastAdmission());
        Long lastDoctorVisitedId = patientDTO.getLastDoctorVisitedId();
        if (lastDoctorVisitedId != null)
            patient.setLastDoctorVisited(doctorRepository.findById(lastDoctorVisitedId));
        patient.setName(patientDTO.getName());
        patient.setNotes(patientDTO.getNotes());
        patient.setOpd(patientDTO.getOpd());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patient.setSurname(patientDTO.getSurname());

        return patient;
    }

    public PatientDTO toDTO(Patient patient) {
        if (patient == null)
            return null;

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setAvatar(patient.getAvatar());
        if (patient.getBloodGroup() != null)
            patientDTO.setBloodGroup(BloodGroupDTO.valueOf(patient.getBloodGroup().name()));
        patientDTO.setChronicPatient(patient.getChronicPatient());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setIdp(patient.getIdp());
        patientDTO.setLastAdmission(patient.getLastAdmission());
        Doctor lastDoctorVisited = patient.getLastDoctorVisited();
        if (lastDoctorVisited != null)
            patientDTO.setLastDoctorVisitedId(lastDoctorVisited.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setNotes(patient.getNotes());
        patientDTO.setOpd(patient.getOpd());
        patientDTO.setPhoneNumber(patient.getPhoneNumber());
        patientDTO.setSurname(patient.getSurname());
        if (patient.getPatientRecords() != null)
            patientDTO.setPatientRecords(patient.getPatientRecords()
                    .stream()
                    .map(patientRecordMapper::toDTO)
                    .toList());

        return patientDTO;
    }

}
