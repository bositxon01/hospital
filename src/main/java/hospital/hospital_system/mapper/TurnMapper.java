package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.payload.TurnDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TurnMapper {

    TurnDTO toDTO(Turn turn);

    List<TurnDTO> toDTO(List<Turn> turns);

    Turn toEntity(TurnDTO turnDTO);

    List<Turn> toEntity(List<TurnDTO> turnDTOs);

}