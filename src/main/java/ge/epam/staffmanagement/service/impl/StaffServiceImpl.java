package ge.epam.staffmanagement.service.impl;

import ge.epam.staffmanagement.entity.Image;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.exception.ResourceNotFoundException;
import ge.epam.staffmanagement.repository.StaffRepository;
import ge.epam.staffmanagement.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Staff staff = getStaffById(id);
        staff.setFirstName(staffDetails.getFirstName());
        staff.setLastName(staffDetails.getLastName());
        staff.setEmail(staffDetails.getEmail());
        staff.setDepartment(staffDetails.getDepartment());
        staff.setContactNumber(staffDetails.getContactNumber());
        if (staffDetails.getImage() != null) {
            Image existingImage = staff.getImage();
            if (existingImage == null) {
                existingImage = new Image();
                staff.setImage(existingImage);
            }
            Image newImage = staffDetails.getImage();
            existingImage.setName(newImage.getName());
            existingImage.setData(newImage.getData());
        }
        return staffRepository.save(staff);
    }

    @Override
    public void deleteStaff(long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found with id " + id));
        staffRepository.delete(staff);
    }

    @Override
    public Page<Staff> searchStaff(String query, Pageable pageable) {
        return staffRepository.searchStaff(query, pageable);
    }
}
