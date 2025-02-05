package hospital.hospital_system.controller;

import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.payload.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/postionPermission")
public class PositionPermissionController {
    private final PositionPermissionService positionPermissionService;

    @GetMapping
    public ResponseEntity<?> getPositionPermission() {
        ApiResult<List<PositionPermission>> allPositionPermission = positionPermissionService.getAllPositionPermission();
        return ResponseEntity.ok(allPositionPermission);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getPositionPermissionById(Long id) {
//    }
}
