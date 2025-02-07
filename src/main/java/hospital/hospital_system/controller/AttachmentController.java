package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import hospital.hospital_system.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @CheckAuth(permissions = PermissionEnum.CREATE_ATTACHMENT)
    @PostMapping("/upload")
    public ResponseEntity<ApiResult<AttachmentDTO>> upload(@RequestParam("file") MultipartFile file) {
        ApiResult<AttachmentDTO> result = attachmentService.upload(file);
        return ResponseEntity.ok(result);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_ATTACHMENT)
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Integer id) {
        return attachmentService.downloadById(id);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_ATTACHMENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> delete(@PathVariable Integer id) {
        ApiResult<String> result = attachmentService.delete(id);
        return ResponseEntity.ok(result);
    }
}
