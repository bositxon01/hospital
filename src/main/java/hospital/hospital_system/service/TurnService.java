package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;

import java.util.List;

public interface TurnService {

    ApiResult<TurnDTO> getTurnById(Integer id);

    ApiResult<List<TurnDTO>> getAllTurns();

    ApiResult<TurnDTO> createTurn(TurnDTO turnDTO);

    ApiResult<TurnDTO> updateTurn(Integer id, TurnDTO turnDTO);

    ApiResult<String> deleteTurn(Integer id);

}