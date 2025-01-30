package hospital.hospital_system.repository;

import hospital.hospital_system.entity.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {
}