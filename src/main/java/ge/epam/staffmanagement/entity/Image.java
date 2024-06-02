package ge.epam.staffmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private byte[] data;

    @OneToOne
    @JoinColumn(name = "staff_id")
    @JsonIgnoreProperties("image")
    private Staff staff;

    public Image.ImageBuilder toBuilder() {
        return new ImageBuilder()
                .id(this.id)
                .name(this.name)
                .data(this.data)
                .staff(this.staff);
    }

}
