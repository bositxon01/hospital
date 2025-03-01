package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Room;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.RoomDTO;
import hospital.hospital_system.repository.RoomRepository;
import hospital.hospital_system.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public ApiResult<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomRepository.findAll().stream()
                .map(room -> new RoomDTO(room.getId(), room.getName(), room.isStatus()))
                .collect(Collectors.toList());
        return ApiResult.success(rooms);
    }

    @Override
    public ApiResult<RoomDTO> getRoom(Integer id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        return roomOptional.map(room -> ApiResult.success(new RoomDTO(room.getId(), room.getName(), room.isStatus())))
                .orElseGet(() -> ApiResult.error("Room not found"));
    }

    @Override
    public ApiResult<RoomDTO> create(RoomDTO roomDTO) {
        Room room = new Room();
        room.setName(roomDTO.getName());
        room.setStatus(roomDTO.isStatus());
        roomRepository.save(room);
        return ApiResult.success(new RoomDTO(room.getId(), room.getName(), room.isStatus()));
    }

    @Override
    public ApiResult<RoomDTO> update(Integer id, RoomDTO roomDTO) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setName(roomDTO.getName());
            room.setStatus(roomDTO.isStatus());
            roomRepository.save(room);
            return ApiResult.success(new RoomDTO(room.getId(), room.getName(), room.isStatus()));
        }
        return ApiResult.error("Room not found");
    }

    @Override
    public ApiResult<RoomDTO> delete(Integer id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            roomRepository.deleteById(id);
            return ApiResult.success("Room deleted successfully");
        }
        return ApiResult.error("Room not found");
    }
}
