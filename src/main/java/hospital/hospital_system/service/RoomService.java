package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.RoomDTO;

import java.util.List;

public interface RoomService {
    ApiResult<List<RoomDTO>> getAllRooms();

    ApiResult<RoomDTO> getRoom(Integer id);

    ApiResult<RoomDTO> createRoom(RoomDTO roomDTO);

    ApiResult<RoomDTO> updateRoom(Integer id, RoomDTO roomDTO);

    ApiResult<RoomDTO> deleteRoom(Integer id);
}
