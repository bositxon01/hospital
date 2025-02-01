package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    // Qo'shimcha metodlar
    Optional<Attachment> findByFileName(String fileName);
    Optional<Attachment> findByPath(String path);
}
