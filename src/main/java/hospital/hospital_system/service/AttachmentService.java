package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    ApiResult<AttachmentDTO> upload(MultipartFile multipartFile);

    ApiResult<AttachmentDTO> getAttachmentById(Integer id);

    ApiResult<String> deleteAttachment(Integer id);
}
