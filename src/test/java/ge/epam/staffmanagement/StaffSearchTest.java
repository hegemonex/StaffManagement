package ge.epam.staffmanagement;

import static org.assertj.core.api.Assertions.assertThat;

import ge.epam.staffmanagement.entity.Department;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.repository.DepartmentRepository;
import ge.epam.staffmanagement.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class StaffSearchTest {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setup() {
        Department department1 = new Department();
        department1.setName("IT Department");
        departmentRepository.save(department1);

        Department department2 = new Department();
        department2.setName("Human Resources");
        departmentRepository.save(department2);


        Staff staff1 = new Staff();
        staff1.setFirstName("Tekla");
        staff1.setLastName("Topuria");
        staff1.setEmail("TeklaTopuria@gmail.com");
        staff1.setContactNumber("123123");
        staff1.setDepartment(department1);

        Staff staff2 = new Staff();
        staff2.setFirstName("Epam");
        staff2.setLastName("Epam");
        staff2.setEmail("epam@epam.com");
        staff2.setContactNumber("0987654321");
        staff2.setDepartment(department2);

        staffRepository.save(staff1);
        staffRepository.save(staff2);
    }

    @Test
    public void testSearchStaff() {
        Page<Staff> result = staffRepository.searchStaff("Tekla", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("Tekla");

        result = staffRepository.searchStaff("epam", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getLastName()).isEqualTo("Epam");
    }

    //FIXME: add pls scenario with fail cases
}
