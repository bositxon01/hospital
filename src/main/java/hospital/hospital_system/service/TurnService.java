package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;

import java.util.List;

public interface TurnService {
    ApiResult<TurnDTO> createTurn(TurnDTO turnDTO);

    ApiResult<List<TurnDTO>> getAllTurns();

    ApiResult<TurnDTO> getTurnById(int id);

    ApiResult<TurnDTO> updateTurn(Integer id, TurnDTO turnDTO);

    ApiResult<String> deleteTurn(int id);
}
