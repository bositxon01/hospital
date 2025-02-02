package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Patient;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PatientDTO;
import hospital.hospital_system.repository.PatientRepository;
import hospital.hospital_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public ApiResult<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        if (patients.isEmpty()) {
            return ApiResult.success("No patients found");
        }

        List<PatientDTO> patientDTOList = patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ApiResult.success(patientDTOList);
    }

    @Override
    public ApiResult<PatientDTO> getPatient(Integer id) {
        return patientRepository.findById(id)
                .map(patient -> ApiResult.success(convertToDTO(patient)))
                .orElse(ApiResult.error("Patient not found"));
    }

    @Override
    public ApiResult<PatientDTO> create(PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return ApiResult.success(convertToDTO(savedPatient));
    }

    @Override
    public ApiResult<PatientDTO> update(Integer id, PatientDTO patientDTO) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setFirstName(patientDTO.getFirstName());
                    patient.setLastName(patientDTO.getLastName());
                    patient.setDateOfBirth(patientDTO.getDateOfBirth());
                    Patient updatedPatient = patientRepository.save(patient);
                    return ApiResult.success(convertToDTO(updatedPatient));
                })
                .orElse(ApiResult.error("Patient not found"));
    }

    @Override
    public ApiResult<PatientDTO> delete(Integer id) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patientRepository.deleteById(id);
                    return ApiResult.success(convertToDTO(patient));

                })
                .orElse(ApiResult.error("Patient not found"));
    }


    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getComplaint() != null ? patient.getComplaint().getId() : null,
                patient.getUser() != null ? patient.getUser().getId() : null,
                patient.getAppointments().stream().map(a -> a.getId()).collect(Collectors.toList())
        );
    }

    private Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        return patient;
    }
}