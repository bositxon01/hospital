package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.enums.TurnEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Integer> {

    boolean existsByNameAndDeletedFalse(TurnEnum name);
}