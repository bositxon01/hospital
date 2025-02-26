package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.RoomDTO;
import hospital.hospital_system.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Tag(name = "Room API", description = "Room CRUD API")
public class RoomController {
    private final RoomService roomService;

    @CheckAuth(permissions = PermissionEnum.VIEW_ROOM)
    @GetMapping
    public ResponseEntity<ApiResult<List<RoomDTO>>> getAllRooms() {
        ApiResult<List<RoomDTO>> allRooms = roomService.getAllRooms();
        return ResponseEntity.ok(allRooms);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_ROOM)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<RoomDTO>> getRoom(@PathVariable Integer id) {
        ApiResult<RoomDTO> roomDto = roomService.getRoom(id);
        return ResponseEntity.ok(roomDto);
    }

    @CheckAuth(permissions = PermissionEnum.CREATE_ROOM)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<RoomDTO>> createRoom(@RequestBody RoomDTO roomDTO) {
        ApiResult<RoomDTO> roomDTOApiResult = roomService.createRoom(roomDTO);
        return ResponseEntity.ok(roomDTOApiResult);
    }

    @CheckAuth(permissions = PermissionEnum.EDIT_ROOM)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<RoomDTO>> updateRoom(@PathVariable Integer id, @RequestBody RoomDTO roomDTO) {
        ApiResult<RoomDTO> update = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(update);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_ROOM)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<RoomDTO>> deleteRoom(@PathVariable Integer id) {
        ApiResult<RoomDTO> delete = roomService.deleteRoom(id);
        return ResponseEntity.ok(delete);
    }
}
