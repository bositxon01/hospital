package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    ApiResult<AttachmentDTO> upload(MultipartFile multipartFile);

    ResponseEntity<Resource> downloadById(Integer id);

    ApiResult<String> delete(Integer id);
}
