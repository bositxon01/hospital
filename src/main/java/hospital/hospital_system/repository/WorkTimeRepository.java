package hospital.hospital_system.repository;

import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.enums.DayEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {
    boolean existsByEmployeeIdAndTurnIdAndDay(Integer employeeId, Integer turnId, DayEnum day);

    List<WorkTime> findByEmployeeIdAndDay(Integer employeeId, DayEnum day);
}
