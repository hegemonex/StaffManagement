package ge.epam.staffmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.epam.staffmanagement.entity.Image;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.model.RequestDTO;
import ge.epam.staffmanagement.model.ValidateDTO;
import ge.epam.staffmanagement.service.DepartmentService;
import ge.epam.staffmanagement.service.StaffService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

//TODO: provide documentation for swagger pls
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;
    private final DepartmentService departmentService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public StaffController(StaffService staffService, DepartmentService departmentService, ObjectMapper objectMapper, Validator validator) {
        this.staffService = staffService;
        this.departmentService = departmentService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @GetMapping
    public Page<Staff> getStaff(
            @RequestParam(defaultValue = "") String query, //FIXME: query injection vulnerability!!! use instead JPA Specification API || Criteria API
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return staffService.searchStaff(query, pageable);
    }

    @GetMapping("/{id}")
    public Staff getStaffById(@PathVariable Long id) {
        return staffService.getStaffById(id);
    }

    @PostMapping
    public ResponseEntity<String> createStaff(@RequestPart String requestDto, //FIXME: use DTO with mapstruct||modelmapper and validation on DTO
                                              @RequestPart MultipartFile image) {
        //fixme: instead of try{} use @ControllerAdvice with @ExceptionHandler
        try {
            ValidateDTO validateDTO = validateStaff(requestDto);
            if (validateDTO.getMessage() == null) {
                Staff staffDetails = dtoToObject(validateDTO.getRequestDTO(), image);
                staffService.createStaffWithImage(staffDetails, staffDetails.getImage());
                return ResponseEntity.status(HttpStatus.CREATED).body("Staff registered successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateDTO.getMessage());
            }
        } catch (Exception ex) {
            //fixme: it is bad practice to return exception message for 500, because it opens for users what happen with
            //fixme: the application and this info can consists sencetive info that allows to huck smth in the app
            //fixme: instead, return some prepared message 'Sorry, smth went wrong, we are working on this'
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStaff(@PathVariable Long id,
                                              @RequestPart String requestDto, //FIXME: use DTO with mapstruct||modelmapper and validation on DTO
                                              @RequestPart(required = false) MultipartFile image) {
        try {
            ValidateDTO validateDTO = validateStaff(requestDto);
            if (validateDTO.getMessage() == null) {
                Staff staffDetails = dtoToObject(validateDTO.getRequestDTO(), image);
                staffService.updateStaff(id, staffDetails);
                return ResponseEntity.status(HttpStatus.OK).body("Staff Updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateDTO.getMessage());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return ResponseEntity.status(HttpStatus.OK).body("Staff Deleted successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    private Staff dtoToObject(RequestDTO requestDto, MultipartFile image) throws IOException {
        Staff staffDetails = new Staff();
        staffDetails.setFirstName(requestDto.getFirstName());
        staffDetails.setLastName(requestDto.getLastName());
        staffDetails.setEmail(requestDto.getEmail());
        staffDetails.setContactNumber(requestDto.getContactNumber());
        staffDetails.setDepartment(departmentService.getDepartmentById(requestDto.getDepartmentId()));
        if (image != null && image.getOriginalFilename() != null && !Objects.equals(image.getOriginalFilename(), "")) {
            Image img = new Image();
            img.setName(image.getOriginalFilename());
            img.setData(image.getBytes());
            staffDetails.setImage(img);
        }
        return staffDetails;
    }

    private ValidateDTO validateStaff(String requestDto) {
        ValidateDTO validateDTO = new ValidateDTO();
        try {
            validateDTO.setRequestDTO(objectMapper.readValue(requestDto, RequestDTO.class));
            Set<ConstraintViolation<RequestDTO>> violations = validator.validate(validateDTO.getRequestDTO());
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<RequestDTO> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                validateDTO.setMessage(sb.toString());
            }
            return validateDTO;
        } catch (Exception ex) {
            validateDTO.setMessage(ex.getMessage());
            return validateDTO;
        }
    }
}
