package com.elderguard.service;

import com.elderguard.entity.Order;
import com.elderguard.entity.Patient;
import com.elderguard.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAllByOrderByOrderedAtDesc();
    }

    public List<Order> findByPatient(Patient patient) {
        return orderRepository.findByPatientOrderByOrderedAtDesc(patient);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findRecent() {
        return orderRepository.findTop10ByOrderByOrderedAtDesc();
    }

    public long countPending() {
        return orderRepository.countByStatus(Order.OrderStatus.PENDING);
    }

    public long count() {
        return orderRepository.count();
    }
}
