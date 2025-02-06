package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.TurnDTO;
import hospital.hospital_system.service.TurnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/turn")
public class TurnController {
    private final TurnService turnService;

    @CheckAuth(permissions = PermissionEnum.CREATE_TURN)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<String>> createTurn(@Valid @RequestBody TurnDTO turnDTO) {
        ApiResult<String> apiResult = turnService.create(turnDTO);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_TURN)
    @GetMapping
    public ResponseEntity<?> getTurns() {
        ApiResult<List<TurnDTO>> apiResult = turnService.getAll();
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_TURN)
    @GetMapping("/{id}")
    public ResponseEntity<?> getTurn(@PathVariable int id) {
        ApiResult<TurnDTO> apiResult = turnService.getById(id);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.EDIT_TURN)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<String>> updateTurn(@PathVariable Integer id,
                                        @Valid @RequestBody TurnDTO turnDTO) {
        ApiResult<String> apiResult = turnService.update(id, turnDTO);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_TURN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteTurn(@PathVariable int id) {
        ApiResult<String> apiResult = turnService.delete(id);
        return ResponseEntity.ok(apiResult);
    }

}
