package ge.epam.staffmanagement;

import static org.assertj.core.api.Assertions.assertThat;

import ge.epam.staffmanagement.entity.Department;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.repository.DepartmentRepository;
import ge.epam.staffmanagement.repository.StaffRepository;
import ge.epam.staffmanagement.service.impl.StaffSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class StaffSearchTest {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setup() {
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
    void testSearchStaff() {
        Specification<Staff> specificationA = new StaffSpecification("Tekla");
        Page<Staff> result = staffRepository.findAll(specificationA, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("Tekla");

        Specification<Staff> specificationB = new StaffSpecification("epam");
        result = staffRepository.findAll(specificationB, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getLastName()).isEqualTo("Epam");
    }

    @Test
    public void testSearchStaffNoMatch() {
        Specification<Staff> specificationC = new StaffSpecification("NonExistingName");
        Page<Staff> result = staffRepository.findAll(specificationC, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();

        Specification<Staff> specificationD = new StaffSpecification("NonExistingEmail@gmail.com");
        result = staffRepository.findAll(specificationD, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();

        Specification<Staff> specificationE = new StaffSpecification("0000000000");
        result = staffRepository.findAll(specificationE, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    public void testSearchStaffPartialMatch() {
        Specification<Staff> specificationF = new StaffSpecification("Tek");
        Page<Staff> result1 = staffRepository.findAll(specificationF, PageRequest.of(0, 10));
        assertThat(result1.getTotalElements()).isEqualTo(1);
        assertThat(result1.getContent().get(0).getFirstName()).isEqualTo("Tekla");

        Specification<Staff> specificationG = new StaffSpecification("Epa");
        Page<Staff> result2 = staffRepository.findAll(specificationG, PageRequest.of(0, 10));
        assertThat(result2.getTotalElements()).isEqualTo(2);
        assertThat(result2.getContent().get(0).getFirstName()).isEqualTo("Tekla");
    }
}
