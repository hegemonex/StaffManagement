package ge.epam.staffmanagement.service;

import ge.epam.staffmanagement.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department getDepartmentById(long id);
    List<Department> getAllDepartments();
    Department addDepartment(Department department);
}
