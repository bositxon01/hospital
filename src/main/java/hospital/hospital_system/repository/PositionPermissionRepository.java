package hospital.hospital_system.repository;

import hospital.hospital_system.entity.PositionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionPermissionRepository extends JpaRepository<PositionPermission, Integer> {

    @Modifying
    @Query("DELETE FROM PositionPermission p WHERE p.position.id = :id")
    void deleteAllByPosition_Id(@Param("id") Integer id);

}