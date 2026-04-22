package com.elderguard.repository;

import com.elderguard.entity.Patient;
import com.elderguard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByOrderByRegisteredAtDesc();
    List<Patient> findByStatus(String status);
    List<Patient> findByCaretaker(User caretaker);
    long countByStatus(String status);
}
