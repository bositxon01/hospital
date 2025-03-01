package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.EmployeeRoom;
import hospital.hospital_system.payload.EmployeeRoomDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeRoomMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "room.id", target = "roomId")
    EmployeeRoomDTO toDTO(EmployeeRoom employeeRoom);

    List<EmployeeRoomDTO> toDTO(List<EmployeeRoom> employeeRooms);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "roomId", target = "room.id")
    EmployeeRoom toEntity(EmployeeRoomDTO employeeRoomDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "roomId", target = "room.id")
    void updateEntity(@MappingTarget EmployeeRoom employeeRoom, EmployeeRoomDTO employeeRoomDTO);

}