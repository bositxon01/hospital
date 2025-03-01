package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Modifying
    @Query("DELETE FROM Patient p WHERE p.id = :id")
    void deletePatientById(@Param("id") Integer id);

}