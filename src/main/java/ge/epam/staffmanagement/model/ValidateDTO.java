package ge.epam.staffmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidateDTO {
    private RequestDTO requestDTO;
    private String message;
}
