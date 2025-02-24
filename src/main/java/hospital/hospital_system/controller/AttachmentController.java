package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import hospital.hospital_system.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
@Tag(name = "Attachment API", description = "Endpoints for managing attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Operation(summary = "Upload a File", description = "Uploads an attachment file and saves it to the server")

    @ApiResponse(responseCode = "200", description = "File uploaded successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))

    @ApiResponse(responseCode = "400", description = "Invalid file")
    @CheckAuth(permissions = PermissionEnum.CREATE_ATTACHMENT)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<AttachmentDTO>> upload(@RequestParam("file") MultipartFile file) {
        ApiResult<AttachmentDTO> result = attachmentService.upload(file);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Attachment by ID", description = "Fetches an attachment using its ID")

    @ApiResponse(responseCode = "200", description = "Attachment retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttachmentDTO.class)))

    @ApiResponse(responseCode = "404", description = "Attachment not found")
    @CheckAuth(permissions = PermissionEnum.VIEW_ATTACHMENT)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<AttachmentDTO>> getAttachment(@PathVariable Integer id) {
        ApiResult<AttachmentDTO> result = attachmentService.getAttachmentById(id);

        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete an Attachment", description = "Deletes an attachment by ID")

    @ApiResponse(responseCode = "200", description = "Attachment deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))

    @ApiResponse(responseCode = "404", description = "Attachment not found")
    @CheckAuth(permissions = PermissionEnum.DELETE_ATTACHMENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteAttachment(@PathVariable Integer id) {
        ApiResult<String> result = attachmentService.deleteAttachment(id);

        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
