package com.elderguard.service;

import com.elderguard.entity.Patient;
import com.elderguard.entity.User;
import com.elderguard.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> findAll() {
        return patientRepository.findAllByOrderByRegisteredAtDesc();
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    public long count() {
        return patientRepository.count();
    }

    public long countActive() {
        return patientRepository.countByStatus("Active");
    }
}
