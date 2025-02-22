package hospital.hospital_system.repository;

import hospital.hospital_system.entity.EmployeeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRoomRepository extends JpaRepository<EmployeeRoom, Integer> {
    Optional<EmployeeRoom> findEmployeeRoomByEmployeeId(Integer employeeId);
}