package com.elderguard.repository;

import com.elderguard.entity.Order;
import com.elderguard.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByOrderedAtDesc();
    List<Order> findByPatientOrderByOrderedAtDesc(Patient patient);
    List<Order> findByOrderType(Order.OrderType orderType);
    List<Order> findByStatus(Order.OrderStatus status);
    long countByStatus(Order.OrderStatus status);
    List<Order> findTop10ByOrderByOrderedAtDesc();
}
