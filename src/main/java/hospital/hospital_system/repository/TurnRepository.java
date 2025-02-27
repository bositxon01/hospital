package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.enums.TurnEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Integer> {

    boolean existsByNameAndDeletedFalse(TurnEnum name);

    Optional<Turn> findByIdAndDeletedFalse(Integer id);

    List<Turn> findByDeletedFalse();

}