package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByAppointmentTime(Timestamp appointmentTime);
}