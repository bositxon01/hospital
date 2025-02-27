package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.EmployeeAndUserDTO;
import hospital.hospital_system.payload.EmployeeGetDTO;
import hospital.hospital_system.payload.EmployeeUpdateDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.position.name", target = "position")
    @Mapping(source = "user.position.id", target = "positionId")
    @Mapping(source = "user.position.salary", target = "salary")
    @Mapping(source = "attachment.id", target = "attachmentId")
    EmployeeGetDTO toGetDTO(Employee employee);

    EmployeeAndUserDTO toDTO(Employee employee);

    List<EmployeeGetDTO> toGetDTO(List<Employee> employees);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Employee toEntity(EmployeeAndUserDTO dto);

    void updateEntity(@MappingTarget Employee employee, EmployeeUpdateDTO dto);

    default User toUser(EmployeeAndUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        return user;
    }
}
