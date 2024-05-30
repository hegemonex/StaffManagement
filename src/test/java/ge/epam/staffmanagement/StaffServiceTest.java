package ge.epam.staffmanagement;

import ge.epam.staffmanagement.entity.Department;
import ge.epam.staffmanagement.entity.Image;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.exception.ResourceNotFoundException;
import ge.epam.staffmanagement.repository.DepartmentRepository;
import ge.epam.staffmanagement.repository.StaffRepository;
import ge.epam.staffmanagement.service.DepartmentService;
import ge.epam.staffmanagement.service.StaffService;
import ge.epam.staffmanagement.service.impl.DepartmentServiceImpl;
import ge.epam.staffmanagement.service.impl.StaffServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StaffServiceTest {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private DepartmentService departmentService;
    private StaffService staffService;

    @BeforeEach
    public void setUp() {
        staffService = new StaffServiceImpl(staffRepository);
        departmentService = new DepartmentServiceImpl(departmentRepository);
        Department department = new Department();
        department.setName("IT Department");
        departmentService.addDepartment(department);
    }

    @Test
    public void testFindAll() {
        Staff staff = new Staff();
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setEmail("john.doe@example.com");
        staff.setContactNumber("1234567890");
        staff.setDepartment(departmentService.getDepartmentById(1L));
        staffRepository.save(staff);

        List<Staff> result = staffService.findAll();

        assertEquals(21, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void testGetStaffById() {
        Staff staff = new Staff();
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setEmail("john.doe@example.com");
        staff.setContactNumber("1234567890");
        staff.setDepartment(departmentService.getDepartmentById(1L));
        staff = staffRepository.save(staff);

        Staff result = staffService.getStaffById(staff.getId());

        assertEquals(staff.getId(), result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    public void testCreateStaffWithImage() {
        Image image = new Image();
        image.setName("image.jpg");
        image.setData(new byte[0]);

        Staff staff = new Staff();
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setEmail("john.doe@example.com");
        staff.setContactNumber("1234567890");
        staff.setDepartment(departmentService.getDepartmentById(1L));

        Staff result = staffService.createStaffWithImage(staff, image);

        assertNotNull(result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("image.jpg", result.getImage().getName());
    }

    @Test
    public void testUpdateStaff() {
        Department department = new Department();
        department.setId(2L);
        department.setName("HR");

        Staff staff = new Staff();
        staff.setFirstName("Jane");
        staff.setLastName("Doe");
        staff.setEmail("jane.doe@example.com");
        staff.setContactNumber("9876543210");
        staff.setDepartment(department);
        staffRepository.save(staff);

        Staff updatedStaff = new Staff();
        updatedStaff.setFirstName("John");
        updatedStaff.setLastName("Doe");
        updatedStaff.setEmail("john.doe@example.com");
        updatedStaff.setContactNumber("1234567890");
        updatedStaff.setDepartment(department);

        Staff result = staffService.updateStaff(staff.getId(), updatedStaff);

        assertEquals("John", result.getFirstName());
        assertEquals("1234567890", result.getContactNumber());
    }

    @Test
    public void testDeleteStaff() {
        Staff staff = new Staff();
        staff.setFirstName("Jane");
        staff.setLastName("Doe");
        staff.setEmail("jane.doe@example.com");
        staff.setContactNumber("9876543210");
        staff.setDepartment(departmentService.getDepartmentById(1L));
        staff = staffRepository.save(staff);

        staffService.deleteStaff(staff.getId());

        Staff finalStaff = staff;
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            staffService.getStaffById(finalStaff.getId());
        });

        assertEquals("Staff not found", exception.getMessage());
    }

    @Test
    public void testSearchStaff() {
        Staff staff = new Staff();
        staff.setFirstName("qweqwe");
        staff.setLastName("qweqwe");
        staff.setEmail("qwe.qwe@qweqwe.com");
        staff.setContactNumber("1234567890");
        staff.setDepartment(departmentService.getDepartmentById(1L));
        staffRepository.save(staff);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Staff> result = staffService.searchStaff("qweqwe", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("qweqwe", result.getContent().get(0).getFirstName());
    }
}
