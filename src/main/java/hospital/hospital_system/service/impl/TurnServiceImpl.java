package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.mapper.TurnMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;
import hospital.hospital_system.repository.TurnRepository;
import hospital.hospital_system.service.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurnServiceImpl implements TurnService {

    private final TurnRepository turnRepository;
    private final TurnMapper turnMapper;

    @Override
    public ApiResult<TurnDTO> createTurn(TurnDTO turnDTO) {

        if (turnRepository.existsByNameAndDeletedFalse(turnDTO.getName()))
            return ApiResult.error("Turn name already exists with name " + turnDTO.getName());

        Turn turn = turnMapper.toEntity(turnDTO);
        turnRepository.save(turn);

        return ApiResult.success("Turn created successfully", turnMapper.toDTO(turn));
    }

    @Override
    public ApiResult<List<TurnDTO>> getAllTurns() {
        List<Turn> turnList = turnRepository.findByDeletedFalse();

        if (turnList.isEmpty())
            return ApiResult.success("Turns not found");

        return ApiResult.success(turnMapper.toDTO(turnList));
    }

    @Override
    public ApiResult<TurnDTO> getTurnById(Integer id) {
        return turnRepository.findByIdAndDeletedFalse(id)
                .map(turnMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Turn not found with id " + id));
    }

    @Override
    public ApiResult<TurnDTO> updateTurn(Integer id, TurnDTO turnDTO) {
        Optional<Turn> optionalTurn = turnRepository.findByIdAndDeletedFalse(id);

        if (optionalTurn.isEmpty())
            return ApiResult.error("Turn not found with id " + id);

        Turn turn = optionalTurn.get();

        turn.setName(turnDTO.getName());
        turn.setStartTime(turnDTO.getStartTime());
        turn.setEndTime(turnDTO.getEndTime());

        turnRepository.save(turn);

        return ApiResult.success("Turn updated successfully", turnMapper.toDTO(turn));
    }

    @Override
    public ApiResult<String> deleteTurn(Integer id) {
        Optional<Turn> optionalTurn = turnRepository.findByIdAndDeletedFalse(id);

        if (optionalTurn.isEmpty())
            return ApiResult.error("Turn not found with id " + id);

        Turn turn = optionalTurn.get();
        turn.setDeleted(true);
        turnRepository.save(turn);

        return ApiResult.success("Turn deleted successfully");
    }

}