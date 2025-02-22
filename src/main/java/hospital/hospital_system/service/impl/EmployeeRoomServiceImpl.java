package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.EmployeeRoom;
import hospital.hospital_system.entity.Room;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeRoomDTO;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.EmployeeRoomRepository;
import hospital.hospital_system.repository.RoomRepository;
import hospital.hospital_system.service.EmployeeRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeRoomServiceImpl implements EmployeeRoomService {
    private final EmployeeRoomRepository employeeRoomRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ApiResult<List<EmployeeRoomDTO>> getAllEmployeeRooms() {
        List<EmployeeRoomDTO> employeeRoomDTOs = employeeRoomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ApiResult.success(employeeRoomDTOs);
    }

    @Override
    public ApiResult<EmployeeRoomDTO> getEmployeeRoomById(Long id) {
        EmployeeRoom employeeRoom = employeeRoomRepository.findById(Math.toIntExact(id)).orElse(null);
        if (employeeRoom == null) {
            return ApiResult.error("EmployeeRoom not found");
        }
        return ApiResult.success(convertToDTO(employeeRoom));
    }

    @Override
    public ApiResult<EmployeeRoomDTO> createEmployeeRoom(EmployeeRoomDTO employeeRoomDTO) {
        Room room = roomRepository.findById(employeeRoomDTO.getRoomId()).orElse(null);
        Employee employee = employeeRepository.findById(employeeRoomDTO.getEmployeeId()).orElse(null);

        if (room == null || employee == null) {
            return ApiResult.error("Room or Employee not found");
        }

        EmployeeRoom employeeRoom = new EmployeeRoom();
        employeeRoom.setRoom(room);
        employeeRoom.setEmployee(employee);
        employeeRoomRepository.save(employeeRoom);

        return ApiResult.success(convertToDTO(employeeRoom));
    }

    @Override
    public ApiResult<EmployeeRoomDTO> updateEmployeeRoom(Long id, EmployeeRoomDTO employeeRoomDTO) {
        EmployeeRoom employeeRoom = employeeRoomRepository.findById(Math.toIntExact(id)).orElse(null);
        if (employeeRoom == null) {
            return ApiResult.error("EmployeeRoom not found");
        }

        Room room = roomRepository.findById(employeeRoomDTO.getRoomId()).orElse(null);
        Employee employee = employeeRepository.findById(employeeRoomDTO.getEmployeeId()).orElse(null);

        if (room == null || employee == null) {
            return ApiResult.error("Room or Employee not found");
        }

        employeeRoom.setRoom(room);
        employeeRoom.setEmployee(employee);
        employeeRoomRepository.save(employeeRoom);

        return ApiResult.success(convertToDTO(employeeRoom));
    }

    @Override
    public ApiResult<EmployeeRoomDTO> deleteEmployeeRoom(Long id) {
        EmployeeRoom employeeRoom = employeeRoomRepository.findById(Math.toIntExact(id)).orElse(null);
        if (employeeRoom == null) {
            return ApiResult.error("EmployeeRoom not found");
        }
        employeeRoomRepository.delete(employeeRoom);
        return ApiResult.success(convertToDTO(employeeRoom));
    }

    private EmployeeRoomDTO convertToDTO(EmployeeRoom employeeRoom) {
        return new EmployeeRoomDTO(
                employeeRoom.getId(),
                employeeRoom.getEmployee().getId(),
                employeeRoom.getRoom().getId()
        );
    }
}