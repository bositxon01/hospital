package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByAppointmentTime(Timestamp appointmentTime);

    @Query("SELECT a FROM Appointment a WHERE a.employee.id = :doctorId AND DATE(a.appointmentTime) = DATE(:date)")
    List<Appointment> findByEmployeeAndAppointmentTime_Date(@Param("doctorId") Integer doctorId, @Param("date") Timestamp date);
}
