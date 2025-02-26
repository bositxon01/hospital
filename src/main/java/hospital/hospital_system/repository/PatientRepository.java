package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    List<Patient> findByDeletedFalse();

    Optional<Patient> findByIdAndDeletedFalse(Integer id);

    @Query("SELECT p FROM Patient p WHERE p.user.username = :username AND p.deleted = FALSE")
    Optional<Patient> findByUsernameAndDeletedFalse(String username);

}