package com.elderguard.repository;

import com.elderguard.entity.EmergencyAlert;
import com.elderguard.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, Long> {
    List<EmergencyAlert> findAllByOrderByTriggeredAtDesc();
    List<EmergencyAlert> findByPatientOrderByTriggeredAtDesc(Patient patient);
    List<EmergencyAlert> findByStatus(EmergencyAlert.AlertStatus status);
    long countByStatus(EmergencyAlert.AlertStatus status);
    List<EmergencyAlert> findTop10ByOrderByTriggeredAtDesc();
}
