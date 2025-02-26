package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PatientDTO;

import java.util.List;

public interface PatientService {
    ApiResult<List<PatientDTO>> getAllPatients();

    ApiResult<PatientDTO> getPatient(Integer id);

    ApiResult<PatientDTO> createPatient(PatientDTO patientDTO);

    ApiResult<PatientDTO> deletePatient(Integer id);
}
