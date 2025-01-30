package hospital.hospital_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Attachment extends AbsIntegerEntity {

    @NotBlank(message = "FileName cannot be blank")
    @Column(nullable = false, unique = true, columnDefinition = "text")
    private String fileName;

    @NotBlank(message = "File orginalname cannot be blank")
    @Column(nullable = false, columnDefinition = "text")
    private String originalFileName;

    @NotBlank(message = "ContentType cannot be blank")
    @Column(nullable = false)
    private String contentType;

    @NotBlank(message = "Path cannot be blank")
    @Column(nullable = false, columnDefinition = "text", unique = true)
    private String path;

    @Positive
    private Long size;

    @OneToOne(mappedBy = "attachment")
    private Employee employee;
}
