package com.elderguard.controller;

import com.elderguard.entity.HealthData;
import com.elderguard.entity.Patient;
import com.elderguard.service.HealthDataService;
import com.elderguard.service.PatientService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/health")
public class HealthDataController {

    private final HealthDataService healthDataService;
    private final PatientService patientService;

    public HealthDataController(HealthDataService healthDataService, PatientService patientService) {
        this.healthDataService = healthDataService;
        this.patientService = patientService;
    }

    @GetMapping
    public String listHealthData(Model model, Authentication auth) {
        model.addAttribute("healthDataList", healthDataService.findRecent());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("newHealthData", new HealthData());
        model.addAttribute("currentUser", auth.getName());
        return "health-monitoring";
    }

    @PostMapping("/add")
    public String addHealthData(@ModelAttribute HealthData healthData,
                                @RequestParam Long patientId,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        healthData.setPatient(patient);
        healthData.setRecordedBy(auth.getName());
        healthDataService.save(healthData);
        redirectAttributes.addFlashAttribute("success", "Health data recorded successfully!");
        return "redirect:/health";
    }

    @GetMapping("/patient/{patientId}")
    public String patientHealthHistory(@PathVariable Long patientId, Model model, Authentication auth) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        List<HealthData> history = healthDataService.findByPatient(patient);
        model.addAttribute("patient", patient);
        model.addAttribute("healthHistory", history);
        model.addAttribute("currentUser", auth.getName());
        return "health-history";
    }
}
