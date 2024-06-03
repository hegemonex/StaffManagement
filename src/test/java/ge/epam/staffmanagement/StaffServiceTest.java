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
        Staff staff = createSampleStaff("John", "Doe", "john.doe@example.com", "1234567890");
        staffRepository.save(staff);

        List<Staff> result = staffService.findAll();

        assertFalse(result.isEmpty(), "The result list should not be empty");
        assertTrue(result.stream().anyMatch(s -> "John".equals(s.getFirstName()) && "Doe".equals(s.getLastName())),
                "The list should contain the staff with the correct first and last name");
    }

    @Test
    public void testGetStaffById() {
        Staff staff = createSampleStaff("John", "Doe", "john.doe@example.com", "1234567890");
        staff = staffRepository.save(staff);

        Staff result = staffService.getStaffById(staff.getId());

        assertStaffFields(staff, result);
    }

    @Test
    public void testCreateStaffWithImage() {
        Image image = new Image();
        image.setName("image.jpg");
        image.setData(new byte[0]);

        Staff staff = createSampleStaff("John", "Doe", "john.doe@example.com", "1234567890");

        Staff result = staffService.createStaffWithImage(staff, image);

        assertNotNull(result.getId(), "Staff ID should not be null");
        assertStaffFields(staff, result);
        assertEquals("image.jpg", result.getImage().getName(), "Image name should match");
    }

    @Test
    public void testUpdateStaff() {
        Department department = new Department();
        department.setName("HR");
        departmentRepository.save(department);

        Staff staff = createSampleStaff("Jane", "Doe", "jane.doe@example.com", "9876543210", department);
        staffRepository.save(staff);

        Staff updatedStaff = createSampleStaff("John", "Doe", "john.doe@example.com", "1234567890", department);

        Staff result = staffService.updateStaff(staff.getId(), updatedStaff);

        assertStaffFields(updatedStaff, result);
    }

    @Test
    public void testDeleteStaff() {
        Staff staff = createSampleStaff("Jane", "Doe", "jane.doe@example.com", "9876543210");
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
        Staff staff = createSampleStaff("qweqwe", "qweqwe", "qwe.qwe@qweqwe.com", "1234567890");
        staffRepository.save(staff);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Staff> result = staffService.searchStaff("qweqwe", pageable);

        assertEquals(1, result.getTotalElements(), "Total elements should be 1");
        assertStaffFields(staff, result.getContent().get(0));
    }

    private Staff createSampleStaff(String firstName, String lastName, String email, String contactNumber) {
        return createSampleStaff(firstName, lastName, email, contactNumber, departmentService.getDepartmentById(1L));
    }

    private Staff createSampleStaff(String firstName, String lastName, String email, String contactNumber, Department department) {
        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setEmail(email);
        staff.setContactNumber(contactNumber);
        staff.setDepartment(department);
        return staff;
    }

    private void assertStaffFields(Staff expected, Staff actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName(), "First names should match");
        assertEquals(expected.getLastName(), actual.getLastName(), "Last names should match");
        assertEquals(expected.getEmail(), actual.getEmail(), "Emails should match");
        assertEquals(expected.getContactNumber(), actual.getContactNumber(), "Contact numbers should match");
        assertEquals(expected.getDepartment().getId(), actual.getDepartment().getId(), "Department IDs should match");
    }
}
