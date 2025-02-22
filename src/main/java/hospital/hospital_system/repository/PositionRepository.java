package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Position;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findPositionByName(@NotBlank String name);

    @Modifying
    @Query("DELETE FROM Position p WHERE p.id = :id")
    void deleteById(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"positionPermissionList"})
    Optional<Position> findPositionById(Integer id);
}
