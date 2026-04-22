package com.elderguard.controller;

import com.elderguard.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final PatientService patientService;
    private final HealthDataService healthDataService;
    private final OrderService orderService;
    private final EmergencyAlertService alertService;
    private final UserService userService;

    public DashboardController(PatientService patientService,
                                HealthDataService healthDataService,
                                OrderService orderService,
                                EmergencyAlertService alertService,
                                UserService userService) {
        this.patientService = patientService;
        this.healthDataService = healthDataService;
        this.orderService = orderService;
        this.alertService = alertService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("totalPatients", patientService.count());
        model.addAttribute("activePatients", patientService.countActive());
        model.addAttribute("activeAlerts", alertService.countActive());
        model.addAttribute("pendingOrders", orderService.countPending());
        model.addAttribute("totalStaff", userService.count());
        model.addAttribute("recentAlerts", alertService.findRecent());
        model.addAttribute("recentOrders", orderService.findRecent());
        model.addAttribute("recentHealthData", healthDataService.findRecent());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("currentUser", auth.getName());
        return "dashboard";
    }
}
