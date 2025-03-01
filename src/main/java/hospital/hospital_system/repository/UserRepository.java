package hospital.hospital_system.repository;

import hospital.hospital_system.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsernameAndDeletedFalse(String username);

    @EntityGraph(attributePaths = {"position.positionPermissionList"})
    Optional<User> findByUsernameAndDeletedFalse(@NotBlank String username);

}