package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByUserPositionIdAndDeletedFalse(Integer id);

    Optional<Employee> findByIdAndDeletedFalse(Integer id);

    List<Employee> findByDeletedFalse();

}