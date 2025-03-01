package hospital.hospital_system.repository;

import hospital.hospital_system.entity.AppointmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentResultRepository extends JpaRepository<AppointmentResult, Integer> {
}