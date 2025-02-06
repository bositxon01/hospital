package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PositionDTO;
import hospital.hospital_system.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/position")
@RequiredArgsConstructor
public class PositionController {
    public final PositionService positionService;


//    @CheckAuth(permissions = PermissionEnum.VIEW_POSITION)
    @GetMapping
    public ResponseEntity<ApiResult<List<PositionDTO>>> getAllPositions() {
        ApiResult<List<PositionDTO>> allPositions = positionService.getAllPositions();
        return ResponseEntity.ok(allPositions);
    }

//    @CheckAuth(permissions = PermissionEnum.VIEW_POSITION)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<PositionDTO>> getPosition(@PathVariable Integer id) {
        ApiResult<PositionDTO> positionDto = positionService.getPosition(id);
        return ResponseEntity.ok(positionDto);
    }

//    @CheckAuth(permissions = PermissionEnum.CREATE_POSITION)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<PositionDTO>> createPosition(@RequestBody PositionDTO positionDTO) {
        ApiResult<PositionDTO> positionDTOApiResult = positionService.create(positionDTO);
        return ResponseEntity.ok(positionDTOApiResult);
    }

//    @CheckAuth(permissions = PermissionEnum.EDIT_POSITION)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<PositionDTO>> updatePosition(@PathVariable Integer id, @RequestBody PositionDTO positionDTO) {
        ApiResult<PositionDTO> update = positionService.update(id, positionDTO);
        return ResponseEntity.ok(update);
    }

//    @CheckAuth(permissions = PermissionEnum.DELETE_POSITION)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<PositionDTO>> deletePosition(@PathVariable Integer id) {
        ApiResult<PositionDTO> delete = positionService.delete(id);
        return ResponseEntity.ok(delete);
    }
}
