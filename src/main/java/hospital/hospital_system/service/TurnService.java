package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;

import java.util.List;

public interface TurnService {
    ApiResult<String> create(TurnDTO turnDTO);

    ApiResult<List<TurnDTO>> getAll();

    ApiResult<TurnDTO> getById(int id);

    ApiResult<String> update(Integer id, TurnDTO turnDTO);

    ApiResult<String> delete(int id);
}
