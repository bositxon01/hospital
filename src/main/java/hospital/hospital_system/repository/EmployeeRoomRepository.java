package hospital.hospital_system.repository;

import hospital.hospital_system.entity.EmployeeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRoomRepository extends JpaRepository<EmployeeRoom, Integer> {
    Optional<EmployeeRoom> findEmployeeRoomByEmployeeId(Integer employeeId);
}