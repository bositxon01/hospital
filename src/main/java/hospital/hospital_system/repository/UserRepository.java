package hospital.hospital_system.repository;

import hospital.hospital_system.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = {"position.positionPermissionList"})
    Optional<User> findByUsername(@NotBlank String username);

    boolean existsByUsername(String userEmail);
}