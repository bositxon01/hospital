package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeAndUserDTO;
import hospital.hospital_system.payload.EmployeeGetDTO;
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

    //    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE)
    @GetMapping
    public ResponseEntity<ApiResult<List<EmployeeGetDTO>>> getAllEmployees() {
        ApiResult<List<EmployeeGetDTO>> employeeAll = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeAll);
    }

    //    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<EmployeeGetDTO>> getEmployeeById(@PathVariable("id") Integer id) {
        ApiResult<EmployeeGetDTO> employeeById = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeById);

    }


    //    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE)
    @GetMapping("/")
    public ResponseEntity<?> searchEmployee(@RequestParam(required = false) String firstName,
                                            @RequestParam(required = false) String lastName) {
        ApiResult<List<EmployeeGetDTO>> byFirstName = employeeService.findByFirstNameOrLastName(firstName, lastName);
        return ResponseEntity.ok(byFirstName);

    }

    //    @CheckAuth(permissions = PermissionEnum.VIEW_EMPLOYEE)
    @GetMapping("/searchSpecializations/")

    public ResponseEntity<?> searchEmployee(@RequestParam(required = false) String specialization) {
        if (specialization != null) {
            ApiResult<List<EmployeeGetDTO>> searchSpecialization = employeeService.searchSpecialization(specialization);
            return ResponseEntity.ok(searchSpecialization);
        }
        return ResponseEntity.badRequest().body("Specialization not found");
    }

    //    @CheckAuth(permissions = PermissionEnum.CREATE_EMPLOYEE)
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmployeeAndUserDTO employeeDTO) {
        ApiResult<EmployeeAndUserDTO> employee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok(employee);
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String code) {
        ApiResult<?> verify = employeeService.verify(email, code);
        return ResponseEntity.ok(verify);
    }

    //    @CheckAuth(permissions = PermissionEnum.EDIT_EMPLOYEE)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody EmployeeAndUserDTO employeeDTO) {
        ApiResult<EmployeeAndUserDTO> updateEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updateEmployee);
    }

    //    @CheckAuth(permissions = PermissionEnum.DELETE_EMPLOYEE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        ApiResult<EmployeeAndUserDTO> deleteEmployee = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(deleteEmployee);
    }

}
