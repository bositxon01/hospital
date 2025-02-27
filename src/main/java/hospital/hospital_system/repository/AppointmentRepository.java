package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Optional<Appointment> findByIdAndDeletedFalse(Integer id);

    List<Appointment> findByDeletedFalse();

    @Query("SELECT a FROM Appointment a WHERE a.employee.id = :doctorId AND DATE(a.appointmentTime) = DATE(:date) AND a.deleted = FALSE")
    List<Appointment> findByEmployeeAndAppointmentTime_DateAndDeletedFalse(@Param("doctorId") Integer doctorId, @Param("date") Timestamp date);

    List<Appointment> findAllByAppointmentTime(Timestamp appointmentTime);

}