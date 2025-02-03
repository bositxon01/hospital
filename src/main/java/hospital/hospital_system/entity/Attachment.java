package hospital.hospital_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotBlank(message = "File original name cannot be blank")
    @Column(nullable = false, columnDefinition = "text")
    private String originalFileName;

    @NotBlank(message = "ContentType cannot be blank")
    @Column(nullable = false)
    private String contentType;

    @NotBlank(message = "Path cannot be blank")
    @Column(nullable = false, columnDefinition = "text", unique = true)
    private String path;

    @Min(1)
    private Long size;

    @OneToOne(mappedBy = "attachment")
    private Employee employee;

}
