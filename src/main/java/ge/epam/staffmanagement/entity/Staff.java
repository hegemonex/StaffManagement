package ge.epam.staffmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString
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
}
