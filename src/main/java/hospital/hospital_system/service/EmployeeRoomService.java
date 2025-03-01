package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeRoomDTO;

import java.util.List;

public interface EmployeeRoomService {

    ApiResult<List<EmployeeRoomDTO>> getAllEmployeeRooms();

    ApiResult<EmployeeRoomDTO> getEmployeeRoomById(Long id);

    ApiResult<EmployeeRoomDTO> createEmployeeRoom(EmployeeRoomDTO employeeRoomDTO);

    ApiResult<EmployeeRoomDTO> updateEmployeeRoom(Long id, EmployeeRoomDTO employeeRoomDTO);

    ApiResult<EmployeeRoomDTO> deleteEmployeeRoom(Long id);

}