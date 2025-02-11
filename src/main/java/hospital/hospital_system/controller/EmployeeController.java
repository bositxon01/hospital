package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.*;
import hospital.hospital_system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE)
    @GetMapping
    public ResponseEntity<ApiResult<List<EmployeeGetDTO>>> getAllEmployees() {
        ApiResult<List<EmployeeGetDTO>> employeeAll = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeAll);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<EmployeeGetDTO>> getEmployeeById(@PathVariable("id") Integer id) {
        ApiResult<EmployeeGetDTO> employeeById = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeById);

    }




    @CheckAuth(permissions = PermissionEnum.CREATE_EMPLOYEE)
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmployeeAndUserDTO employeeDTO) {
        ApiResult<EmployeeAndUserDTO> employee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok(employee);
    }


    @CheckAuth(permissions = PermissionEnum.CREATE_EMPLOYEE)
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String code) {
        ApiResult<?> verify = employeeService.verify(email, code);
        return ResponseEntity.ok(verify);
    }

    @CheckAuth(permissions = PermissionEnum.EDIT_EMPLOYEE)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody EmployeeUpdateDto employeeDTO) {
        ApiResult<EmployeeAndUserDTO> updateEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updateEmployee);
    }

    @PutMapping("/attachment")
    public ResponseEntity<?> updateAttachment(@RequestBody EmployeeAttachmentDto attachmentDto) {
        ApiResult<String> apiResult = employeeService.updateEmployeeAttachment(attachmentDto);
        return ResponseEntity.ok(apiResult);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_EMPLOYEE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        ApiResult<EmployeeAndUserDTO> deleteEmployee = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(deleteEmployee);
    }

@GetMapping("/filter")
    public ResponseEntity<?>filter(EmployeeFilterDTO employeeFilterDTO) {
    ApiResult<List<EmployeeGetDTO>> filter = employeeService.filter(employeeFilterDTO);
    return ResponseEntity.ok(filter);
}


}
