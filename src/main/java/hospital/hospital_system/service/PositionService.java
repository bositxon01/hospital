package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PositionDTO;

import java.util.List;

public interface PositionService {
    ApiResult<List<PositionDTO>> getAllPositions();

    ApiResult<PositionDTO> getPositionById(Integer id);

    ApiResult<PositionDTO> createPosition(PositionDTO positionDTO);

    ApiResult<PositionDTO> updatePosition(Integer id, PositionDTO positionDTO);

    ApiResult<String> deletePosition(Integer id);
}


