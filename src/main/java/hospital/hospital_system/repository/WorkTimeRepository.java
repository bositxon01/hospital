package hospital.hospital_system.repository;

import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.enums.DayEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {
    boolean existsByEmployeeIdAndTurnIdAndDayAndDeletedFalse(Integer employeeId, Integer turnId, DayEnum day);

    List<WorkTime> findByEmployeeIdAndDayAndDeletedFalse(Integer employeeId, DayEnum day);

    List<WorkTime> findByDeletedFalse();

    Optional<WorkTime> findByIdAndDeletedFalse(Integer id);

}
