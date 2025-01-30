package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Position;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findPositionByName(@NotBlank String name);
}