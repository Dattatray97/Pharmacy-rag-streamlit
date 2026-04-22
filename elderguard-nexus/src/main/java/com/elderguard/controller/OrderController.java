package com.elderguard.controller;

import com.elderguard.entity.Order;
import com.elderguard.entity.Patient;
import com.elderguard.service.OrderService;
import com.elderguard.service.PatientService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PatientService patientService;

    public OrderController(OrderService orderService, PatientService patientService) {
        this.orderService = orderService;
        this.patientService = patientService;
    }

    @GetMapping
    public String listOrders(Model model, Authentication auth) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("newOrder", new Order());
        model.addAttribute("currentUser", auth.getName());
        return "orders";
    }

    @PostMapping("/add")
    public String placeOrder(@ModelAttribute Order order,
                             @RequestParam Long patientId,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        order.setPatient(patient);
        order.setOrderedBy(auth.getName());
        orderService.save(order);
        redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
        return "redirect:/orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(Order.OrderStatus.valueOf(status));
        orderService.save(order);
        redirectAttributes.addFlashAttribute("success", "Order status updated!");
        return "redirect:/orders";
    }
}
