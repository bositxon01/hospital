package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.enums.TurnEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;
import hospital.hospital_system.repository.TurnRepository;
import hospital.hospital_system.service.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurnServiceImpl implements TurnService {
    private final TurnRepository turnRepository;

    @Override
    public ApiResult<TurnDTO> createTurn(TurnDTO turnDTO) {
        TurnEnum name = turnDTO.getName();

        if (turnRepository.existsByNameAndDeletedFalse(name)) {
            return ApiResult.error("Turn name already exists with " + name);
        }

        LocalTime startTime = turnDTO.getStartTime();
        LocalTime endTime = turnDTO.getEndTime();

        Turn turn = new Turn();
        turn.setName(name);
        turn.setStartTime(startTime);
        turn.setEndTime(endTime);

        turnRepository.save(turn);
        turnDTO.setId(turn.getId());

        return ApiResult.success("Turn created successfully", turnDTO);
    }

    @Override
    public ApiResult<List<TurnDTO>> getAllTurns() {
        List<Turn> turnList = turnRepository.findAll();

        if (turnList.isEmpty()) {
            return ApiResult.success("No turns found");
        }

        List<TurnDTO> turnDTOS = turnList.stream()
                .map(turn ->
                        new TurnDTO(
                                turn.getId(),
                                turn.getName(),
                                turn.getStartTime(),
                                turn.getEndTime()))
                .toList();

        return ApiResult.success(turnDTOS);
    }

    @Override
    public ApiResult<TurnDTO> getTurnById(int id) {
        Optional<Turn> optionalTurn = turnRepository.findById(id);

        if (optionalTurn.isEmpty()) {
            return ApiResult.error("Turn not found with id " + id);
        }

        Turn turn = optionalTurn.get();
        TurnDTO turnDTO = new TurnDTO(turn.getId(), turn.getName(), turn.getStartTime(), turn.getEndTime());

        return ApiResult.success(turnDTO);
    }

    @Override
    public ApiResult<TurnDTO> updateTurn(Integer id, TurnDTO turnDTO) {
        Optional<Turn> optionalTurn = turnRepository.findById(id);
        if (optionalTurn.isEmpty()) {
            return ApiResult.error("Turn not found with id " + id);
        }

        Turn turn = optionalTurn.get();
        turn.setName(turnDTO.getName());
        turn.setStartTime(turnDTO.getStartTime());
        turn.setEndTime(turnDTO.getEndTime());
        turnRepository.save(turn);

        turnDTO.setId(turn.getId());

        return ApiResult.success("Turn updated successfully", turnDTO);
    }

    @Override
    public ApiResult<String> deleteTurn(int id) {
        Optional<Turn> optionalTurn = turnRepository.findById(id);

        if (optionalTurn.isEmpty()) {
            return ApiResult.error("Turn not found with id " + id);
        }

        Turn turn = optionalTurn.get();
        turn.setDeleted(true);
        turnRepository.save(turn);

        return ApiResult.success("Turn deleted successfully");
    }
}
