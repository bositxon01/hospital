package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.mapper.PositionMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PositionDTO;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PositionPermissionRepository;
import hospital.hospital_system.repository.PositionRepository;
import hospital.hospital_system.service.PositionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionPermissionRepository positionPermissionRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionMapper positionMapper;

    @Override
    public ApiResult<List<PositionDTO>> getAllPositions() {

        List<Position> positionList = positionRepository.findByDeletedFalse();

        if (positionList.isEmpty())
            return ApiResult.success("No positionList found");

        List<PositionDTO> positionDTOS = positionMapper.toDTO(positionList);

        return ApiResult.success(positionDTOS);
    }

    @Override
    public ApiResult<PositionDTO> getPositionById(Integer id) {
        return positionRepository.findByIdAndDeletedFalse(id)
                .map(positionMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("No position found with id: " + id));
    }

    @Override
    public ApiResult<PositionDTO> createPosition(PositionDTO positionDTO) {
        Optional<Position> positionByName = positionRepository.findPositionByNameAndDeletedFalse(positionDTO.getName());

        if (positionByName.isPresent())
            return ApiResult.error("This positionName already exists");

        Position position = positionMapper.toEntity(positionDTO);
        positionRepository.save(position);

        for (PositionPermission positionPermission : position.getPositionPermissionList()) {
            positionPermission.setPosition(position);
        }

        positionPermissionRepository.saveAll(position.getPositionPermissionList());

        return ApiResult.success("Position created successfully", positionMapper.toDTO(position));
    }

    @Transactional
    @Override
    public ApiResult<PositionDTO> updatePosition(Integer id, PositionDTO positionDTO) {

        Optional<Position> optionalPosition = positionRepository.findByIdAndDeletedFalse(id);

        if (optionalPosition.isEmpty())
            return ApiResult.error("Position not found with id: " + id);

        Position position = optionalPosition.get();

        positionMapper.updateEntity(positionDTO, position);
        positionRepository.save(position);

        if (positionDTO.getPermissions().isEmpty())
            return ApiResult.error("Permissions cannot be empty");

        updatePositionPermissions(position, positionDTO.getPermissions());

        return ApiResult.success("Position updated successfully", positionMapper.toDTO(position));
    }

    @Transactional
    @Override
    public ApiResult<String> deletePosition(Integer id) {
        Optional<Position> optionalPosition = positionRepository.findByIdAndDeletedFalse(id);

        if (optionalPosition.isEmpty())
            return ApiResult.error("Position not found");

        Position position = optionalPosition.get();

        boolean isAssigned = employeeRepository.existsByUserPositionIdAndDeletedFalse(id);

        if (isAssigned)
            return ApiResult.error("Cannot delete position because it is assigned to employees.");

        positionPermissionRepository.deleteAllByPosition_Id(id);

        position.setDeleted(true);
        positionRepository.save(position);

        return ApiResult.success("Position deleted successfully");
    }

    private void updatePositionPermissions(Position position, List<PermissionEnum> permissions) {
        positionPermissionRepository.deleteAllByPosition_Id(position.getId());

        List<PositionPermission> positionPermissions = permissions.stream()
                .map(permission -> new PositionPermission(position, permission))
                .toList();

        positionPermissionRepository.saveAll(positionPermissions);
    }

}