package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    Optional<Attachment> findByFileName(String fileName);

    Optional<Attachment> findByPath(String path);

    Optional<Attachment> findAttachmentById(Integer id);

    Optional<Attachment> findById(Integer id);
}
