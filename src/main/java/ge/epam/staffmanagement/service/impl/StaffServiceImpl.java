package ge.epam.staffmanagement.service.impl;

import ge.epam.staffmanagement.entity.Image;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.exception.ResourceNotFoundException;
import ge.epam.staffmanagement.repository.StaffRepository;
import ge.epam.staffmanagement.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getStaffById(long id) {
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    }

    @Override
    public Staff createStaffWithImage(Staff staff, Image image) {
        staff.setImage(image);
        image.setStaff(staff);
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(long id, Staff staffDetails) {
        //FIXME: for what purpose u are doing all of this things?? it can be done via mapper (mapstruct)
        Staff existingStaff = getStaffById(id);
        Staff.StaffBuilder staffBuilder = existingStaff.toBuilder()
                .firstName(staffDetails.getFirstName())
                .lastName(staffDetails.getLastName())
                .email(staffDetails.getEmail())
                .department(staffDetails.getDepartment())
                .contactNumber(staffDetails.getContactNumber());
        if (staffDetails.getImage() != null) {
            Image existingImage = existingStaff.getImage();
            if (existingImage == null) {
                existingImage = new Image();
            } else {
                Image newImage = staffDetails.getImage();
                existingImage.setName(newImage.getName());
                existingImage.setData(newImage.getData());
            }
            existingStaff.toBuilder().image(existingImage);
        }
        Staff updatedStaff = staffBuilder.build();
        return staffRepository.save(updatedStaff);
    }

    @Override
    public void deleteStaff(long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found with id " + id));
        staffRepository.delete(staff);
    }

    @Override
    public Page<Staff> searchStaff(String query, Pageable pageable) {
        Specification<Staff> specification = new StaffSpecification(query);
        return staffRepository.findAll(specification, pageable);
    }
}
