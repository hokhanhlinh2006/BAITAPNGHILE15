package com.example.employeemanagement;
import com.example.employeemanagement.entity.*;
import com.example.employeemanagement.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(DepartmentRepository deptRepo, EmployeeRepository empRepo) {
        return args -> {

            Department it = new Department(null, "IT", "Hanoi", null);
            Department hr = new Department(null, "HR", "HCM", null);

            deptRepo.save(it);
            deptRepo.save(hr);

            Employee e1 = new Employee(null, "An", 22, null, "ACTIVE", it);
            Employee e2 = new Employee(null, "Binh", 25, null, "ACTIVE", hr);

            empRepo.save(e1);
            empRepo.save(e2);
        };
    }
}