package ge.epam.staffmanagement;

import ge.epam.staffmanagement.controller.DepartmentController;
import ge.epam.staffmanagement.entity.Department;
import ge.epam.staffmanagement.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDepartments() {
        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("IT Department");

        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("Finance");

        List<Department> departments = Arrays.asList(department1, department2);
        when(departmentService.getAllDepartments()).thenReturn(departments);

        List<Department> result = departmentController.getAllDepartments();

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    public void testGetDepartmentById() {
        long id = 1;
        Department department = new Department();
        department.setId(id);
        department.setName("Human Resources");

        when(departmentService.getDepartmentById(id)).thenReturn(department);

        Department result = departmentController.getDepartmentById(id);

        verify(departmentService, times(1)).getDepartmentById(id);
    }
}