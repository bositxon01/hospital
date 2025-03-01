package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.PositionDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PositionMapper {

    @Mapping(source = "positionPermissionList", target = "permissions", qualifiedByName = "mapPermissions")
    PositionDTO toDTO(Position position);

    List<PositionDTO> toDTO(List<Position> positions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "positionPermissionList", source = "permissions", qualifiedByName = "mapPositionPermissions")
    @Mapping(target = "users", ignore = true)
    Position toEntity(PositionDTO positionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PositionDTO positionDTO, @MappingTarget Position position);

    @Named("mapPermissions")
    default List<PermissionEnum> mapPermissions(List<PositionPermission> positionPermissions) {
        return positionPermissions.stream()
                .map(PositionPermission::getPermission)
                .toList();
    }

    @Named("mapPositionPermissions")
    default List<PositionPermission> mapPositionPermissions(List<PermissionEnum> permissions) {
        return permissions.stream()
                .map(permission -> {
                    PositionPermission positionPermission = new PositionPermission();
                    positionPermission.setPermission(permission);
                    return positionPermission;
                })
                .toList();
    }

}