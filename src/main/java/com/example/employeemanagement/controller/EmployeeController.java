package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.*;
import com.example.employeemanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Employee employee,
                       @RequestParam("file") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            file.transferTo(new File(UPLOAD_DIR + fileName));
            employee.setAvatar(fileName);
        }

        employeeRepository.save(employee);
        return "redirect:/";
    }
}