package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeRoomDTO;

import java.util.List;

public interface EmployeeRoomService {

    ApiResult<EmployeeRoomDTO> getEmployeeRoomById(Integer id);

    ApiResult<List<EmployeeRoomDTO>> getAllEmployeeRooms();

    ApiResult<EmployeeRoomDTO> createEmployeeRoom(EmployeeRoomDTO employeeRoomDTO);

    ApiResult<EmployeeRoomDTO> updateEmployeeRoom(Integer id, EmployeeRoomDTO employeeRoomDTO);

    ApiResult<String> deleteEmployeeRoom(Integer id);

}