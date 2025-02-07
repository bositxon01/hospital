package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PositionDTO;
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
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final PositionPermissionRepository positionPermissionRepository;

    @Override
    public ApiResult<List<PositionDTO>> getAllPositions() {

        List<Position> allPosition = positionRepository.findAll();
        if (allPosition.isEmpty()) {
            return ApiResult.success("No positions found");
        }


        List<PositionDTO> positionDTOList = allPosition.stream()
                .map(position -> new PositionDTO(
                                position.getId(),
                                position.getName(),
                                position.getSalary(),
                                position.getPositionPermissionList().stream().map(PositionPermission::getPermission).toList()
                        )
                ).toList();

        return ApiResult.success(positionDTOList);
    }


    @Override
    public ApiResult<PositionDTO> getPosition(Integer id) {
        Optional<Position> optionalPosition = positionRepository.findById(id);
        if (optionalPosition.isEmpty()) {
            return ApiResult.error("No position found");
        }
        Position position = optionalPosition.get();
        PositionDTO positionDTO = new PositionDTO(position.getId(), position.getName(), position.getSalary(), position.getPositionPermissionList().stream().map(PositionPermission::getPermission).toList());
        return ApiResult.success(positionDTO);
    }

    @Override
    public ApiResult<PositionDTO> create(PositionDTO positionDTO) {
        Optional<Position> positionByName = positionRepository.findPositionByName(positionDTO.getName());
        if (positionByName.isPresent()) {
            return ApiResult.error("This positionName already exists");
        }
        Position position = new Position();
        position.setName(positionDTO.getName());
        position.setSalary(positionDTO.getSalary());
        positionRepository.save(position);
        List<PermissionEnum> permissions = positionDTO.getPermissions();
        if (permissions.isEmpty()) {
            return ApiResult.error("Permissions cannot be empty");
        }
        for (PermissionEnum permission : permissions) {
            PositionPermission positionPermission = new PositionPermission();
            positionPermission.setPosition(position);
            positionPermission.setPermission(permission);
            positionPermissionRepository.save(positionPermission);
        }

        return ApiResult.success("Successfully created position");
    }


    @Transactional
    @Override
    public ApiResult<PositionDTO> update(Integer id, PositionDTO positionDTO) {
        Optional<Position> optionalPosition = positionRepository.findById(id);
        if (optionalPosition.isEmpty()) {
            return ApiResult.error("No position found for given id");
        }

        Position position = optionalPosition.get();
        position.setName(positionDTO.getName());
        position.setSalary(positionDTO.getSalary());
        positionRepository.save(position);
        List<PermissionEnum> permissions = positionDTO.getPermissions();

        if (permissions.isEmpty()) {
            return ApiResult.error("Permissions cannot be empty");
        }

        positionPermissionRepository.deleteAllByPosition_Id((position.getId()));

        for (PermissionEnum permission : permissions) {
            PositionPermission positionPermission = new PositionPermission();
            positionPermission.setPosition(position);
            positionPermission.setPermission(permission);
            positionPermissionRepository.save(positionPermission);
        }
        return ApiResult.success("Successfully updated position");
    }


    @Transactional
    @Override
    public ApiResult<PositionDTO> delete(Integer id) {
        try {
            positionPermissionRepository.deleteAllByPosition_Id(id);
            positionRepository.deleteById(id);
            return ApiResult.success("Successfully deleted position");
        } catch (Exception e) {
            return ApiResult.error(e.getMessage());
        }
    }
}
