package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Employee;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByFirstNameOrLastName(@NotBlank(message = "Firstname cannot be blank") String firstName, @NotBlank(message = "Lastname cannot be blank") String lastName);

    List<Employee> findBySpecializationContaining(String specialization);

}