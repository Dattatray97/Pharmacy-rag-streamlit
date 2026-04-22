package com.elderguard.service;

import com.elderguard.entity.EmergencyAlert;
import com.elderguard.entity.Patient;
import com.elderguard.repository.EmergencyAlertRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyAlertService {

    private final EmergencyAlertRepository alertRepository;

    public EmergencyAlertService(EmergencyAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public EmergencyAlert save(EmergencyAlert alert) {
        return alertRepository.save(alert);
    }

    public List<EmergencyAlert> findAll() {
        return alertRepository.findAllByOrderByTriggeredAtDesc();
    }

    public List<EmergencyAlert> findByPatient(Patient patient) {
        return alertRepository.findByPatientOrderByTriggeredAtDesc(patient);
    }

    public List<EmergencyAlert> findActive() {
        return alertRepository.findByStatus(EmergencyAlert.AlertStatus.ACTIVE);
    }

    public List<EmergencyAlert> findRecent() {
        return alertRepository.findTop10ByOrderByTriggeredAtDesc();
    }

    public Optional<EmergencyAlert> findById(Long id) {
        return alertRepository.findById(id);
    }

    public EmergencyAlert resolve(Long id, String resolvedBy) {
        EmergencyAlert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setStatus(EmergencyAlert.AlertStatus.RESOLVED);
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolvedBy(resolvedBy);
        return alertRepository.save(alert);
    }

    public long countActive() {
        return alertRepository.countByStatus(EmergencyAlert.AlertStatus.ACTIVE);
    }
}
