package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.specification.EmployeeSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,

            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge
    ) {

        Sort sort = sortDir.equals("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = EmployeeSpecification.filter(
                keyword, departmentId, minAge, maxAge
        );

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("keyword", keyword);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);

        model.addAttribute("departments", departmentRepository.findAll());

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