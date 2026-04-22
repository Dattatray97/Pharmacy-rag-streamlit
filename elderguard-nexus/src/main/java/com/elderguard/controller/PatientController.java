package com.elderguard.controller;

import com.elderguard.entity.Patient;
import com.elderguard.entity.User;
import com.elderguard.service.PatientService;
import com.elderguard.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    public PatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @GetMapping
    public String listPatients(Model model, Authentication auth) {
        List<Patient> patients = patientService.findAll();
        model.addAttribute("patients", patients);
        model.addAttribute("currentUser", auth.getName());
        return "patients";
    }

    @GetMapping("/add")
    public String addPatientForm(Model model, Authentication auth) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("currentUser", auth.getName());
        return "add-patient";
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        User caretaker = userService.findByUsername(auth.getName()).orElse(null);
        patient.setCaretaker(caretaker);
        patientService.save(patient);
        redirectAttributes.addFlashAttribute("success", "Patient added successfully!");
        return "redirect:/patients";
    }

    @GetMapping("/{id}")
    public String viewPatient(@PathVariable Long id, Model model, Authentication auth) {
        Patient patient = patientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        model.addAttribute("currentUser", auth.getName());
        return "patient-detail";
    }

    @PostMapping("/{id}/delete")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        patientService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Patient removed successfully!");
        return "redirect:/patients";
    }
}
