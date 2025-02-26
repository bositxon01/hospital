package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PatientDTO;
import hospital.hospital_system.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@Tag(name = "Patient API", description = "Patient CRUD API")
public class PatientController {
    public final PatientService patientService;

    @CheckAuth(permissions = PermissionEnum.VIEW_PATIENT)
    @GetMapping
    public ResponseEntity<ApiResult<List<PatientDTO>>> getAllPatients() {
        ApiResult<List<PatientDTO>> allPatients = patientService.getAllPatients();
        return ResponseEntity.ok(allPatients);
    }

    @CheckAuth(permissions = PermissionEnum.VIEW_PATIENT)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<PatientDTO>> getPatient(@PathVariable Integer id) {
        ApiResult<PatientDTO> patientDTO = patientService.getPatient(id);
        return ResponseEntity.ok(patientDTO);
    }

    @CheckAuth(permissions = PermissionEnum.CREATE_PATIENT)
    @PostMapping("/create")
    public ResponseEntity<ApiResult<PatientDTO>> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        ApiResult<PatientDTO> patientDTOApiResult = patientService.createPatient(patientDTO);
        return ResponseEntity.ok(patientDTOApiResult);
    }

    @CheckAuth(permissions = PermissionEnum.DELETE_PATIENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<PatientDTO>> deletePatient(@PathVariable Integer id) {
        ApiResult<PatientDTO> patientDTOApiResult = patientService.deletePatient(id);
        return ResponseEntity.ok(patientDTOApiResult);
    }
}
