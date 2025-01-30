package hospital.hospital_system.repository;

import hospital.hospital_system.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}