package hospital.hospital_system.repository;

import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.enums.DayEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {
    boolean existsByEmployeeIdAndTurnIdAndDay(Integer employeeId, Integer turnId, DayEnum day);

    List<WorkTime> findByEmployeeIdAndDay(Integer employeeId, DayEnum day);
}
