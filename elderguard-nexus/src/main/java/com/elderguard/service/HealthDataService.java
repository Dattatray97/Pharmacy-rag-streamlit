package com.elderguard.service;

import com.elderguard.entity.HealthData;
import com.elderguard.entity.Patient;
import com.elderguard.repository.HealthDataRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HealthDataService {

    private final HealthDataRepository healthDataRepository;

    public HealthDataService(HealthDataRepository healthDataRepository) {
        this.healthDataRepository = healthDataRepository;
    }

    public HealthData save(HealthData healthData) {
        return healthDataRepository.save(healthData);
    }

    public List<HealthData> findByPatient(Patient patient) {
        return healthDataRepository.findByPatientOrderByRecordedAtDesc(patient);
    }

    public List<HealthData> findRecent() {
        return healthDataRepository.findTop10ByOrderByRecordedAtDesc();
    }

    public Optional<HealthData> findLatestForPatient(Patient patient) {
        return healthDataRepository.findTopByPatientOrderByRecordedAtDesc(patient);
    }

    public List<HealthData> findAll() {
        return healthDataRepository.findAll();
    }

    public long count() {
        return healthDataRepository.count();
    }
}
