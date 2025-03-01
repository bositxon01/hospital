package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.EmployeeRoom;
import hospital.hospital_system.entity.Room;
import hospital.hospital_system.mapper.EmployeeRoomMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeRoomDTO;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.EmployeeRoomRepository;
import hospital.hospital_system.repository.RoomRepository;
import hospital.hospital_system.service.EmployeeRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeRoomServiceImpl implements EmployeeRoomService {

    private final EmployeeRoomRepository employeeRoomRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoomMapper employeeRoomMapper;

    @Override
    public ApiResult<List<EmployeeRoomDTO>> getAllEmployeeRooms() {
        List<EmployeeRoom> employeeRoomList = employeeRoomRepository.findByDeletedFalse();

        if (employeeRoomList.isEmpty())
            return ApiResult.success("EmployeeRooms not found");

        return ApiResult.success(employeeRoomMapper.toDTO(employeeRoomList));
    }

    @Override
    public ApiResult<EmployeeRoomDTO> getEmployeeRoomById(Integer id) {
        return employeeRoomRepository.findByIdAndDeletedFalse(id)
                .map(employeeRoomMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.success("EmployeeRoom not found with id " + id));
    }

    @Override
    public ApiResult<EmployeeRoomDTO> createEmployeeRoom(EmployeeRoomDTO employeeRoomDTO) {
        Optional<Room> optionalRoom = roomRepository.findByIdAndDeletedFalse(employeeRoomDTO.getRoomId());

        if (optionalRoom.isEmpty())
            return ApiResult.success("Room not found with id " + employeeRoomDTO.getRoomId());

        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(employeeRoomDTO.getEmployeeId());

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id " + employeeRoomDTO.getEmployeeId());

        EmployeeRoom employeeRoom = employeeRoomMapper.toEntity(employeeRoomDTO);

        employeeRoom.setEmployee(optionalEmployee.get());
        employeeRoom.setRoom(optionalRoom.get());

        employeeRoomRepository.save(employeeRoom);

        return ApiResult.success("EmployeeRoom created successfully", employeeRoomMapper.toDTO(employeeRoom));
    }

    @Override
    public ApiResult<EmployeeRoomDTO> updateEmployeeRoom(Integer id, EmployeeRoomDTO employeeRoomDTO) {
        Optional<EmployeeRoom> optionalEmployeeRoom = employeeRoomRepository.findByIdAndDeletedFalse(id);

        if (optionalEmployeeRoom.isEmpty())
            return ApiResult.error("EmployeeRoom not found with id " + id);

        Optional<Room> optionalRoom = roomRepository.findByIdAndDeletedFalse(employeeRoomDTO.getRoomId());

        if (optionalRoom.isEmpty())
            return ApiResult.error("Room not found with id " + employeeRoomDTO.getRoomId());

        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(employeeRoomDTO.getEmployeeId());

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id " + employeeRoomDTO.getEmployeeId());

        EmployeeRoom employeeRoom = optionalEmployeeRoom.get();

        employeeRoomMapper.updateEntity(employeeRoom, employeeRoomDTO);

        employeeRoom.setRoom(optionalRoom.get());
        employeeRoom.setEmployee(optionalEmployee.get());

        employeeRoomRepository.save(employeeRoom);

        return ApiResult.success("EmployeeRoom updated successfully", employeeRoomMapper.toDTO(employeeRoom));
    }

    @Override
    public ApiResult<String> deleteEmployeeRoom(Integer id) {
        Optional<EmployeeRoom> optionalEmployeeRoom = employeeRoomRepository.findByIdAndDeletedFalse(id);

        if (optionalEmployeeRoom.isEmpty())
            return ApiResult.error("EmployeeRoom not found with id " + id);

        EmployeeRoom employeeRoom = optionalEmployeeRoom.get();

        employeeRoom.setDeleted(true);
        employeeRoomRepository.save(employeeRoom);

        return ApiResult.success("EmployeeRoom deleted successfully");
    }

}