package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttachmentDTO implements Serializable {

    private Integer id;

    private String fileName;

    private String originalFileName;

    private String contentType;

    private Long size;

}