package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Turn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnRepository extends JpaRepository<Turn, Integer> {
}