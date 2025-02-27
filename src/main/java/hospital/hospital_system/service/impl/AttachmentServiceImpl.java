package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Attachment;
import hospital.hospital_system.mapper.AttachmentMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AttachmentDTO;
import hospital.hospital_system.repository.AttachmentRepository;
import hospital.hospital_system.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Value("${app.files.baseFolder}")
    private String baseFolder;

    private static final long MAX_FILE_SIZE = 100L * 1024 * 1024;

    @Override
    public ApiResult<AttachmentDTO> upload(MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty())
                return ApiResult.error("File is empty");

            String contentType = multipartFile.getContentType();
            long size = multipartFile.getSize();

            if (size > MAX_FILE_SIZE)
                return ApiResult.error("File is too large (max 100 MB is allowed)");

            if (!Objects.requireNonNull(contentType).startsWith("image/"))
                return ApiResult.error("Invalid file type");

            LocalDate localDate = LocalDate.now();

            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();

            String originalFilename = multipartFile.getOriginalFilename();

            String name = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFilename);

            Path path = Paths.get(baseFolder)
                    .resolve(String.valueOf(year))
                    .resolve(String.valueOf(month))
                    .resolve(String.valueOf(day));

            Files.createDirectories(path);

            path = path.resolve(name);

            Files.copy(multipartFile.getInputStream(), path);

            Attachment attachment = new Attachment(
                    name,
                    originalFilename,
                    contentType,
                    path.toString(),
                    size,
                    null
            );

            attachmentRepository.save(attachment);

            return ApiResult.success(attachmentMapper.toDTO(attachment));
        } catch (IOException e) {
            return ApiResult.error("Error occurred while uploading attachment: " + e.getMessage());
        }
    }

    @Override
    public ApiResult<AttachmentDTO> getAttachmentById(Integer id) {
        return attachmentRepository.findAttachmentByIdAndDeletedFalse(id)
                .map(attachmentMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Attachment not found with id: " + id));
    }

    @Override
    public ApiResult<String> deleteAttachment(Integer id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findAttachmentByIdAndDeletedFalse(id);

        if (optionalAttachment.isEmpty())
            return ApiResult.error("Attachment not found with id: " + id);

        Attachment attachment = optionalAttachment.get();

        Path path = Paths.get(attachment.getPath());

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            return ApiResult.error("Error occurred while deleting attachment: " + e.getMessage());
        }

        attachment.setDeleted(true);
        attachmentRepository.save(attachment);

        return ApiResult.success("Attachment deleted successfully");
    }

}