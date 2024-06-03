package ge.epam.staffmanagement.controller;

import ge.epam.staffmanagement.entity.Department;
import ge.epam.staffmanagement.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(value = "Department Management System", description = "Operations pertaining to department in Department Management System")
@RestController
@RequestMapping("api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @ApiOperation(value = "View a list of available departments", response = List.class)
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "Get a department by Id")
    @GetMapping("/id")
    public Department getDepartmentById(long id) {
        return departmentService.getDepartmentById(id);
    }

}
