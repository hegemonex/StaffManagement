package ge.epam.staffmanagement.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestDTO {
    @NotBlank(message = "First Name is required.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First Name must contain only alphabet characters.")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last Name must contain only alphabet characters.")
    private String lastName;

    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotNull(message = "Contact Number is required.")
    private String contactNumber;

    @NotNull(message = "Department is required.")
    private Long departmentId;
}
