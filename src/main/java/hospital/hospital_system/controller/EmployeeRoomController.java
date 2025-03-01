package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeRoomDTO;
import hospital.hospital_system.service.EmployeeRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee-room")
@RequiredArgsConstructor
@Tag(name = "EmployeeRoom API", description = "EmployeeRoom CRUD API")
public class EmployeeRoomController {

    private final EmployeeRoomService employeeRoomService;

    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE_ROOM)
    @GetMapping
    public ResponseEntity<ApiResult<List<EmployeeRoomDTO>>> getAllEmployeeRooms() {
        ApiResult<List<EmployeeRoomDTO>> allEmployeeRooms = employeeRoomService.getAllEmployeeRooms();
        return ResponseEntity.ok(allEmployeeRooms);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE_ROOM)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<EmployeeRoomDTO>> getEmployeeRoomById(@PathVariable Integer id) {
        ApiResult<EmployeeRoomDTO> employeeRoomDTO = employeeRoomService.getEmployeeRoomById(id);
        return ResponseEntity.ok(employeeRoomDTO);
    }

    @CheckAuth(permissions = PermissionEnum.CREATE_EMPLOYEE_ROOM)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<EmployeeRoomDTO>> createEmployeeRoom(@RequestBody EmployeeRoomDTO employeeRoomDTO) {
        ApiResult<EmployeeRoomDTO> result = employeeRoomService.createEmployeeRoom(employeeRoomDTO);
        return ResponseEntity.ok(result);
    }

    @CheckAuth(permissions = PermissionEnum.EDIT_EMPLOYEE_ROOM)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<EmployeeRoomDTO>> updateEmployeeRoom(@PathVariable Integer id, @RequestBody EmployeeRoomDTO employeeRoomDTO) {
        ApiResult<EmployeeRoomDTO> result = employeeRoomService.updateEmployeeRoom(id, employeeRoomDTO);
        return ResponseEntity.ok(result);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_EMPLOYEE_ROOM)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteEmployeeRoom(@PathVariable Integer id) {
        ApiResult<String> result = employeeRoomService.deleteEmployeeRoom(id);
        return ResponseEntity.ok(result);
    }

}