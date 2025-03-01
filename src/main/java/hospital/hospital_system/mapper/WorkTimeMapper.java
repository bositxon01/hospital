package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.payload.WorkTimeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkTimeMapper {

    @Mapping(target = "employeeDTO.id", source = "employee.id")
    @Mapping(target = "employeeDTO.firstName", source = "employee.firstName")
    @Mapping(target = "employeeDTO.lastName", source = "employee.lastName")
    @Mapping(target = "employeeDTO.birthDate", source = "employee.dateOfBirth")
    @Mapping(target = "employeeDTO.specialization", source = "employee.specialization")

    @Mapping(target = "turnDTO.id", source = "turn.id")
    @Mapping(target = "turnDTO.name", source = "turn.name")
    @Mapping(target = "turnDTO.startTime", source = "turn.startTime")
    @Mapping(target = "turnDTO.endTime", source = "turn.endTime")
    WorkTimeDTO toDTO(WorkTime workTime);

    List<WorkTimeDTO> toDTO(List<WorkTime> workTimes);
}
