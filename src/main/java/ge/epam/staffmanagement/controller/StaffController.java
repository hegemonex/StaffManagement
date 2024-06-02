package ge.epam.staffmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.epam.staffmanagement.entity.Image;
import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.mapper.StaffMapper;
import ge.epam.staffmanagement.model.RequestDTO;
import ge.epam.staffmanagement.model.ValidateDTO;
import ge.epam.staffmanagement.service.DepartmentService;
import ge.epam.staffmanagement.service.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/staff")
@Api(value = "Staff Management System", description = "Operations pertaining to staff in the Staff Management System")
public class StaffController {
    private final StaffService staffService;
    private final DepartmentService departmentService;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final StaffMapper staffMapper;

    @Autowired
    public StaffController(StaffService staffService, DepartmentService departmentService, ObjectMapper objectMapper, Validator validator, StaffMapper staffMapper) {
        this.staffService = staffService;
        this.departmentService = departmentService;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.staffMapper = staffMapper;
    }

    @ApiOperation(value = "View a list of available staff", response = Page.class)
    @GetMapping
    public Page<Staff> getStaff(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return staffService.searchStaff(query, pageable);
    }

    @ApiOperation(value = "Get a staff member by Id")
    @GetMapping("/{id}")
    public Staff getStaffById(@PathVariable Long id) {
        return staffService.getStaffById(id);
    }

    @PostMapping
    @ExceptionHandler()
    public ResponseEntity<String> createStaff(@RequestPart String requestDto,
                                              @RequestPart MultipartFile image) throws IOException {
        ValidateDTO validateDTO = validateStaff(requestDto);
        if (validateDTO.getMessage() == null) {
            Staff staffDetails = dtoToObject(validateDTO.getRequestDTO(), image);
            staffService.createStaffWithImage(staffDetails, staffDetails.getImage());
            return ResponseEntity.status(HttpStatus.CREATED).body("Staff registered successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateDTO.getMessage());
        }
    }

    @ApiOperation(value = "Update an existing staff member")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStaff(@PathVariable Long id,
                                              @RequestPart String requestDto,
                                              @RequestPart(required = false) MultipartFile image) throws IOException {
        ValidateDTO validateDTO = validateStaff(requestDto);
        if (validateDTO.getMessage() == null) {
            Staff staffDetails = dtoToObject(validateDTO.getRequestDTO(), image);
            staffService.updateStaff(id, staffDetails);
            return ResponseEntity.status(HttpStatus.OK).body("Staff Updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateDTO.getMessage());
        }
    }

    @ApiOperation(value = "Delete a staff member")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.status(HttpStatus.OK).body("Staff deleted successfully.");
    }

    private Staff dtoToObject(RequestDTO requestDto, MultipartFile image) throws IOException {
        Staff staffDetails = staffMapper.toEntity(requestDto);
        staffDetails.setDepartment(departmentService.getDepartmentById(requestDto.getDepartmentId()));
        if (image != null && !image.isEmpty()) {
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