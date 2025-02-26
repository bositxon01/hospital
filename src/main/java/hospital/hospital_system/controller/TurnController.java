package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;
import hospital.hospital_system.service.TurnService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/turn")
@Tag(name = "Turn API", description = "Turn CRUD API")
public class TurnController {
    private final TurnService turnService;

    @CheckAuth(permissions = PermissionEnum.CREATE_TURN)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<TurnDTO>> createTurn(@Valid @RequestBody TurnDTO turnDTO) {
        ApiResult<TurnDTO> apiResult = turnService.createTurn(turnDTO);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_TURN)
    @GetMapping
    public ResponseEntity<?> getTurns() {
        ApiResult<List<TurnDTO>> apiResult = turnService.getAllTurns();
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_TURN)
    @GetMapping("/{id}")
    public ResponseEntity<?> getTurn(@PathVariable int id) {
        ApiResult<TurnDTO> apiResult = turnService.getTurnById(id);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.EDIT_TURN)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<TurnDTO>> updateTurn(@PathVariable Integer id,
                                                        @Valid @RequestBody TurnDTO turnDTO) {
        ApiResult<TurnDTO> apiResult = turnService.updateTurn(id, turnDTO);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_TURN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteTurn(@PathVariable int id) {
        ApiResult<String> apiResult = turnService.deleteTurn(id);
        return ResponseEntity.ok(apiResult);
    }

}
