package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TurnService {
    ApiResult<TurnDTO> create(TurnDTO turnDTO);

    ApiResult<List<TurnDTO>> getAll();

    ApiResult<TurnDTO> getById(int id);

    ApiResult<TurnDTO> update(Integer id, TurnDTO turnDTO);

    ApiResult<TurnDTO> delete(int id);
}
