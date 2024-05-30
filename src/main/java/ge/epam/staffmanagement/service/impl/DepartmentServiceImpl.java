package ge.epam.staffmanagement.service.impl;

import ge.epam.staffmanagement.entity.Department;
import ge.epam.staffmanagement.exception.ResourceNotFoundException;
import ge.epam.staffmanagement.repository.DepartmentRepository;
import ge.epam.staffmanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department getDepartmentById(long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }
}
