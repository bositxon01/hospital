package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Room;
import hospital.hospital_system.enums.RoomEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.RoomDTO;
import hospital.hospital_system.repository.RoomRepository;
import hospital.hospital_system.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public ApiResult<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomRepository.findAll()
                .stream()
                .map(RoomServiceImpl::getRoomDTO)
                .toList();
        return ApiResult.success(rooms);
    }

    @Override
    public ApiResult<RoomDTO> getRoom(Integer id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        return optionalRoom.map(room -> ApiResult.success(getRoomDTO(room)))
                .orElseGet(() -> ApiResult.error("Room not found"));
    }

    @Override
    public ApiResult<RoomDTO> createRoom(RoomDTO roomDTO) {
        RoomEnum name = roomDTO.getName();
        Optional<Room> optionalRoom = roomRepository.findByNameAndDeletedFalse(name);

        if (optionalRoom.isPresent()) {
            return ApiResult.error("Room already exists with name " + name);
        }

        Room room = new Room();
        room.setName(roomDTO.getName());
        room.setStatus(roomDTO.isStatus());
        roomRepository.save(room);

        return ApiResult.success(getRoomDTO(room));
    }

    @Override
    public ApiResult<RoomDTO> updateRoom(Integer id, RoomDTO roomDTO) {
        Optional<Room> optionalRoom = roomRepository.findById(id);

        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room.setName(roomDTO.getName());
            room.setStatus(roomDTO.isStatus());

            roomRepository.save(room);

            return ApiResult.success(getRoomDTO(room));
        }
        return ApiResult.error("Room not found");
    }

    @Override
    public ApiResult<RoomDTO> deleteRoom(Integer id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);

        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room.setDeleted(true);
            roomRepository.save(room);

            return ApiResult.success("Room deleted successfully");
        }
        return ApiResult.error("Room not found");
    }

    private static RoomDTO getRoomDTO(Room room) {
        return new RoomDTO(room.getId(), room.getName(), room.isStatus());
    }
}
