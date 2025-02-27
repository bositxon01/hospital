package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Room;
import hospital.hospital_system.enums.RoomEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByNameAndDeletedFalse(RoomEnum name);

    Optional<Room> findByIdAndDeletedFalse(Integer id);

    List<Room> findByDeletedFalse();

}