package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AttachmentService {

    ApiResult<AttachmentDTO> upload(MultipartFile multipartFile);

    ApiResult<AttachmentDTO> getAttachmentById(UUID id);

    ApiResult<String> deleteAttachment(UUID id);
}
