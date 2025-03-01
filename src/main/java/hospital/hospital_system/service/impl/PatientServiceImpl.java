package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Complaint;
import hospital.hospital_system.entity.Patient;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.mapper.PatientMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.PatientDTO;
import hospital.hospital_system.repository.ComplaintRepository;
import hospital.hospital_system.repository.PatientRepository;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientMapper patientMapper;

    @Override
    public ApiResult<List<PatientDTO>> getAllPatients() {
        List<Patient> patientList = patientRepository.findByDeletedFalse();

        if (patientList.isEmpty())
            return ApiResult.success("Patients not found");

        return ApiResult.success(patientMapper.toDTO(patientList));
    }

    @Override
    public ApiResult<PatientDTO> getPatient(Integer id) {
        return patientRepository.findByIdAndDeletedFalse(id)
                .map(patientMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Patient not found with id: " + id));
    }

    @Override
    public ApiResult<PatientDTO> createPatient(PatientDTO patientDTO) {
        if (patientRepository.findByUsernameAndDeletedFalse(patientDTO.getUsername()).isPresent())
            return ApiResult.error("Patient already exists with username: " + patientDTO.getUsername());

        Complaint complaint = patientMapper.toEntity(patientDTO.getComplaintDTO());
        complaintRepository.save(complaint);

        User user = userRepository.findByUsernameAndDeletedFalse(patientDTO.getUsername())
                .orElseGet(() -> {
                    User newUser = patientMapper.toEntityFromDTO(patientDTO);
                    newUser.setPassword(passwordEncoder.encode(patientDTO.getPassword()));
                    return userRepository.save(newUser);
                });

        Patient patient = patientMapper.toEntity(patientDTO);

        patient.setUser(user);
        patient.setComplaint(complaint);

        patientRepository.save(patient);

        return ApiResult.success("Patient created successfully", patientMapper.toDTO(patient));
    }

    @Transactional
    @Override
    public ApiResult<PatientDTO> updatePatient(Integer id, PatientDTO patientDTO) {
        Optional<Patient> optionalPatient = patientRepository.findByIdAndDeletedFalse(id);

        if (optionalPatient.isEmpty())
            return ApiResult.error("Patient not found with id: " + id);

        Patient patient = optionalPatient.get();
        String username = patientDTO.getUsername();

        if (!patient.getUser().getUsername().equals(username)) {
            if (userRepository.existsByUsernameAndDeletedFalse(username))
                return ApiResult.error("Username is already taken.");

            patient.getUser().setUsername(username);
        }

        patientMapper.updateEntity(patient, patientDTO);

        if (patientDTO.getComplaintDTO() != null) {
            if (patient.getComplaint() == null)
                patient.setComplaint(new Complaint());

            patientMapper.updateComplaintEntity(patient.getComplaint(), patientDTO.getComplaintDTO());
            complaintRepository.save(patient.getComplaint());
        }

        patientRepository.save(patient);

        return ApiResult.success("Patient updated successfully", patientMapper.toDTO(patient));
    }

    @Transactional
    @Override
    public ApiResult<String> deletePatient(Integer id) {
        Optional<Patient> optionalPatient = patientRepository.findByIdAndDeletedFalse(id);

        if (optionalPatient.isEmpty())
            return ApiResult.error("Patient not found with id: " + id);

        Patient patient = optionalPatient.get();
        patient.setDeleted(true);
        patientRepository.save(patient);

        patient.getUser().setDeleted(true);
        userRepository.save(patient.getUser());

        return ApiResult.success("Patient deleted successfully");
    }

}