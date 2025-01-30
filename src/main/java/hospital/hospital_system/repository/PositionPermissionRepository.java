package hospital.hospital_system.repository;

import hospital.hospital_system.entity.PositionPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionPermissionRepository extends JpaRepository<PositionPermission, Integer> {
}