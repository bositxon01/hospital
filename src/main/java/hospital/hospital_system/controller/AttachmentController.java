package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import hospital.hospital_system.service.AttachmentService;
import lombok.RequiredArgsConstructor;
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
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResult.error("No file uploaded"));
        }

        if (file.getSize() > 10 * 1024 * 1024) {  // 10MB dan katta fayllar uchun xato
            return ResponseEntity.badRequest().body(ApiResult.error("File is too large"));
        }

        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {  // Fayl turi faqat rasm bo'lishi kerak
            return ResponseEntity.badRequest().body(ApiResult.error("Invalid file type"));
        }

        ApiResult<AttachmentDTO> result = attachmentService.upload(file);
        return ResponseEntity.ok(result);
    }

    // Attachmentni ID orqali olish
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<AttachmentDTO>> getAttachment(@PathVariable UUID id) {
        ApiResult<AttachmentDTO> result = attachmentService.getAttachmentById(id);
        return ResponseEntity.ok(result);
    }

    // Attachmentni o'chirish
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteAttachment(@PathVariable UUID id) {
        ApiResult<String> result = attachmentService.deleteAttachment(id);
        return ResponseEntity.ok(result);
    }
}
