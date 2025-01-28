package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Role;
import hospital.hospital_system.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleEnum(RoleEnum roleEnum);
}