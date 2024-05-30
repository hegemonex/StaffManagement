package ge.epam.staffmanagement.repository;

import ge.epam.staffmanagement.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT s FROM Staff s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.contactNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.department.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Staff> searchStaff(String query, Pageable pageable);
}
