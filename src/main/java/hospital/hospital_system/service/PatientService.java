package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PatientDTO;

import java.util.List;

public interface PatientService {

    ApiResult<PatientDTO> getPatient(Integer id);

    ApiResult<List<PatientDTO>> getAllPatients();

    ApiResult<PatientDTO> createPatient(PatientDTO patientDTO);

    ApiResult<PatientDTO> updatePatient(Integer id, PatientDTO patientDTO);

    ApiResult<String> deletePatient(Integer id);

}