package ge.epam.staffmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String contactNumber;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties("staff")
    private Department department;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("staff")
    private Image image;

    public StaffBuilder toBuilder() {
        return new StaffBuilder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .department(this.department)
                .contactNumber(this.contactNumber)
                .image(this.image);
    }
}
