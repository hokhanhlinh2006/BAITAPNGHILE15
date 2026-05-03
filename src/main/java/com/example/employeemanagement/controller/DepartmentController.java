package com.example.employeemanagement.controller;

import com.example.employeemanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {

        int count = departmentService.deleteDepartment(id);

        ra.addFlashAttribute("message",
                "Đã xóa phòng ban và cập nhật " + count + " nhân viên");

        return "redirect:/";
    }
}