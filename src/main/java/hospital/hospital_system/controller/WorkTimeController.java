package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.WorkTimeDTO;
import hospital.hospital_system.payload.WorkTimeWithIdDTO;
import hospital.hospital_system.service.WorkTimeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-time")
@RequiredArgsConstructor
@Tag(name = "Work-time API", description = "Work-time CRUD API")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    @CheckAuth(permissions = PermissionEnum.CREATE_WORK_TIME)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<WorkTimeDTO>> create(@RequestBody WorkTimeWithIdDTO workTimeWithIdDTO) {
        ApiResult<WorkTimeDTO> apiResult = workTimeService.createWorkTime(workTimeWithIdDTO);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_WORK_TIME)
    @GetMapping
    public ResponseEntity<ApiResult<List<WorkTimeDTO>>> getWorkTimes() {
        ApiResult<List<WorkTimeDTO>> apiResult = workTimeService.getWorkTimes();
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_WORK_TIME)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<WorkTimeDTO>> getWorkTime(@PathVariable int id) {
        ApiResult<WorkTimeDTO> apiResult = workTimeService.getWorkTimeById(id);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.EDIT_WORK_TIME)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<WorkTimeDTO>> updateWorkTime(@PathVariable int id,
                                            @RequestBody WorkTimeWithIdDTO workTimeWithIdDTO) {
        ApiResult<WorkTimeDTO> apiResult = workTimeService.updateWorkTime(id, workTimeWithIdDTO);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_WORK_TIME)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> deleteWorkTime(@PathVariable int id) {
        ApiResult<String> apiResult = workTimeService.deleteWorkTime(id);
        return ResponseEntity.ok(apiResult);
    }

}