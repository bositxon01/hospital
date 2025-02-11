package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PositionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PositionService {
    ApiResult<List<PositionDTO>> getAllPositions();

    ApiResult<PositionDTO> getPosition(Integer id);

    ApiResult<PositionDTO> create(PositionDTO positionDTO);

    ApiResult<PositionDTO> update(Integer id, PositionDTO positionDTO);

    ApiResult<PositionDTO> delete(Integer id);

}


