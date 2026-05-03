package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public int deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban"));

        List<Employee> employees = employeeRepository.findAll();

        int count = 0;

        for (Employee e : employees) {
            if (e.getDepartment() != null &&
                    e.getDepartment().getId().equals(id)) {

                e.setDepartment(null); // gỡ liên kết
                employeeRepository.save(e);
                count++;
            }
        }

        departmentRepository.delete(department);

        return count;
    }
}