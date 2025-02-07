package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import hospital.hospital_system.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    // Fayl yuklash
    @PostMapping("/upload")
    public ResponseEntity<ApiResult<AttachmentDTO>> upload(@RequestParam("file") MultipartFile file) {
        ApiResult<AttachmentDTO> result = attachmentService.upload(file);
        return ResponseEntity.ok(result);
    }

    // Attachmentni ID orqali olish
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> getAttachment(@PathVariable Integer id) {
        return attachmentService.getAttachmentById(id);
    }


    // Attachmentni o'chirish
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteAttachment(@PathVariable Integer id) {
        ApiResult<String> result = attachmentService.deleteAttachment(id);
        return ResponseEntity.ok(result);
    }
}
