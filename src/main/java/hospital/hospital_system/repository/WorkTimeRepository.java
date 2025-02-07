package hospital.hospital_system.repository;

import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.enums.DayEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {
    boolean existsByEmployeeIdAndTurnIdAndDay(Integer employeeId, Integer turnId, @NotNull(message = "Day cannot be null") DayEnum day);
}