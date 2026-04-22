package com.elderguard.controller;

import com.elderguard.entity.EmergencyAlert;
import com.elderguard.entity.Patient;
import com.elderguard.service.EmergencyAlertService;
import com.elderguard.service.PatientService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/emergency")
public class EmergencyController {

    private final EmergencyAlertService alertService;
    private final PatientService patientService;

    public EmergencyController(EmergencyAlertService alertService, PatientService patientService) {
        this.alertService = alertService;
        this.patientService = patientService;
    }

    @GetMapping
    public String listAlerts(Model model, Authentication auth) {
        model.addAttribute("alerts", alertService.findAll());
        model.addAttribute("activeAlerts", alertService.findActive());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("newAlert", new EmergencyAlert());
        model.addAttribute("currentUser", auth.getName());
        return "emergency";
    }

    @PostMapping("/sos")
    public String triggerSOS(@RequestParam Long patientId,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) String locationDetails,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        EmergencyAlert alert = new EmergencyAlert();
        alert.setPatient(patient);
        alert.setAlertType("SOS");
        alert.setDescription(description != null ? description : "Emergency SOS triggered");
        alert.setSeverity(EmergencyAlert.AlertSeverity.CRITICAL);
        alert.setLocationDetails(locationDetails);
        alert.setTriggeredBy(auth.getName());
        alertService.save(alert);
        redirectAttributes.addFlashAttribute("sosTriggered", "SOS Alert triggered for patient: " + patient.getFullName());
        return "redirect:/emergency";
    }

    @PostMapping("/{id}/resolve")
    public String resolveAlert(@PathVariable Long id,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        alertService.resolve(id, auth.getName());
        redirectAttributes.addFlashAttribute("success", "Alert resolved successfully!");
        return "redirect:/emergency";
    }
}
