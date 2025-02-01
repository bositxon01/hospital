package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Position;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PositionDTO;
import hospital.hospital_system.repository.PositionRepository;
import hospital.hospital_system.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

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
                                position.getSalary()
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
        PositionDTO positionDTO = new PositionDTO(position.getId(), position.getName(), position.getSalary());
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
        return ApiResult.success("Successfully created position");
    }

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
        return ApiResult.success("Successfully updated position");
    }

    @Override
    public ApiResult<PositionDTO> delete(Integer id) {
        positionRepository.deleteById(id);
        return ApiResult.success("Successfully deleted position");
    }


}
