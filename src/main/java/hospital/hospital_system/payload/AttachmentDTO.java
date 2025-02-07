package hospital.hospital_system.payload;

import hospital.hospital_system.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO implements Serializable {

    private Integer id;
    private String fileName;
    private String originalFileName;
    private String contentType;
    private Long size;
    private String path;

    private String url;

    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.originalFileName = attachment.getOriginalFileName();
        this.contentType = attachment.getContentType();
        this.size = attachment.getSize();
        this.path = attachment.getPath();
        this.url = "/api/attachment/download/" + attachment.getId();
    }
}
