package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Room;
import hospital.hospital_system.payload.RoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    RoomDTO toDTO(Room room);

    List<RoomDTO> toDTO(List<Room> rooms);

    Room toEntity(RoomDTO roomDTO);

    List<Room> toEntity(List<RoomDTO> roomDTOs);

}