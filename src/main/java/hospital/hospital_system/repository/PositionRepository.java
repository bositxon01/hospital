package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Position;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

    Optional<Position> findByIdAndDeletedFalse(Integer id);

    Optional<Position> findPositionByNameAndDeletedFalse(@NotBlank String name);

    @EntityGraph(attributePaths = {"positionPermissionList"})
    Optional<Position> findPositionByIdAndDeletedFalse(Integer id);

    List<Position> findByDeletedFalse();

}