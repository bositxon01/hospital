package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.RoomDTO;

import java.util.List;

public interface RoomService {

    ApiResult<RoomDTO> getRoomById(Integer id);

    ApiResult<List<RoomDTO>> getAllRooms();

    ApiResult<RoomDTO> createRoom(RoomDTO roomDTO);

    ApiResult<RoomDTO> updateRoom(Integer id, RoomDTO roomDTO);

    ApiResult<String> deleteRoom(Integer id);

}