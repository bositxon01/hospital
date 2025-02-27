package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    Optional<Attachment> findAttachmentByIdAndDeletedFalse(Integer id);

    Optional<Attachment> findByIdAndDeletedFalse(Integer id);

}