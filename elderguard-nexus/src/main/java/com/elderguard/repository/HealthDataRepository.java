package com.elderguard.repository;

import com.elderguard.entity.HealthData;
import com.elderguard.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData, Long> {
    List<HealthData> findByPatientOrderByRecordedAtDesc(Patient patient);
    List<HealthData> findTop10ByOrderByRecordedAtDesc();
    Optional<HealthData> findTopByPatientOrderByRecordedAtDesc(Patient patient);
}
