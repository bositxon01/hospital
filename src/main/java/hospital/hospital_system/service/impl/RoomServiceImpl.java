package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Room;
import hospital.hospital_system.mapper.RoomMapper;
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
    private final RoomMapper roomMapper;

    @Override
    public ApiResult<List<RoomDTO>> getAllRooms() {

        List<Room> roomList = roomRepository.findByDeletedFalse();

        if (roomList.isEmpty())
            return ApiResult.success("No rooms found");

        return ApiResult.success(roomMapper.toDTO(roomList));
    }

    @Override
    public ApiResult<RoomDTO> getRoomById(Integer id) {
        return roomRepository.findByIdAndDeletedFalse(id)
                .map(roomMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Room not found with id" + id));
    }

    @Override
    public ApiResult<RoomDTO> createRoom(RoomDTO roomDTO) {

        if (roomRepository.existsByNameAndDeletedFalse(roomDTO.getName()))
            return ApiResult.error("Room already exists with name " + roomDTO.getName());

        Room room = roomMapper.toEntity(roomDTO);
        roomRepository.save(room);

        return ApiResult.success("Room created successfully", roomMapper.toDTO(room));
    }

    @Override
    public ApiResult<RoomDTO> updateRoom(Integer id, RoomDTO roomDTO) {
        Optional<Room> optionalRoom = roomRepository.findByIdAndDeletedFalse(id);

        if (optionalRoom.isEmpty())
            return ApiResult.error("Room not found with id " + id);

        Room room = optionalRoom.get();
        room.setName(roomDTO.getName());
        room.setStatus(roomDTO.isStatus());

        roomRepository.save(room);

        return ApiResult.success("Room updated successfully", roomMapper.toDTO(room));
    }

    @Override
    public ApiResult<String> deleteRoom(Integer id) {
        Optional<Room> optionalRoom = roomRepository.findByIdAndDeletedFalse(id);

        if (optionalRoom.isEmpty())
            return ApiResult.error("Room not found with id " + id);

        Room room = optionalRoom.get();
        room.setDeleted(true);
        roomRepository.save(room);

        return ApiResult.success("Room deleted successfully");
    }

}