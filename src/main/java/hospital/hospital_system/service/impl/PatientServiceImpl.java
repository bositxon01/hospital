package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Complaint;
import hospital.hospital_system.entity.Patient;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.ComplaintDTO;
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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final UserRepository userRepository;

    private final ComplaintRepository complaintRepository;

    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResult<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientRepository.findByDeletedFalse();

        if (patients.isEmpty()) {
            return ApiResult.success("No patients found");
        }

        List<PatientDTO> patientDTOS = patients.stream()
                .map(PatientServiceImpl::getPatientDTO)
                .toList();

        return ApiResult.success(patientDTOS);
    }

    @Override
    public ApiResult<PatientDTO> getPatient(Integer id) {
        Optional<Patient> optionalPatient = patientRepository.findByIdAndDeletedFalse(id);

        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id: " + id);
        }

        Patient patient = optionalPatient.get();

        PatientDTO patientDTO = getPatientDTO(patient);

        return ApiResult.success(patientDTO);
    }

    @Override
    public ApiResult<PatientDTO> createPatient(PatientDTO patientDTO) {
        ComplaintDTO complaintDTO = patientDTO.getComplaintDTO();
        Complaint complaint = new Complaint();
        complaint.setName(complaintDTO.getName());

        if (Objects.nonNull(complaintDTO.getDescription())) {
            complaint.setDescription(complaintDTO.getDescription());
        }
        complaintRepository.save(complaint);

        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedFalse(patientDTO.getUsername());

        Optional<Patient> existingPatient = patientRepository.findByUsernameAndDeletedFalse(patientDTO.getUsername());

        if (existingPatient.isPresent()) {
            return ApiResult.error("Patient already exists with username: " + patientDTO.getUsername());
        }

        User user;

        if (optionalUser.isEmpty()) {
            user = new User();
            user.setUsername(patientDTO.getUsername());
            user.setPassword(passwordEncoder.encode(patientDTO.getPassword()));

            //send email
            userRepository.save(user);

        } else {
            user = optionalUser.get();
            //send email
        }

        Patient patient = new Patient();

        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setComplaint(complaint);
        patient.setUser(user);
        patientRepository.save(patient);

        patientDTO.setId(patient.getId());

        return ApiResult.success("Patient created successfully", patientDTO);
    }

    @Transactional
    @Override
    public ApiResult<PatientDTO> deletePatient(Integer id) {
        Optional<Patient> optionalPatient = patientRepository.findByIdAndDeletedFalse(id);
        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id: " + id);
        }

        Patient patient = optionalPatient.get();
        patient.setDeleted(true);
        patientRepository.save(patient);

        return ApiResult.success("Patient deleted successfully");
    }

    private static PatientDTO getPatientDTO(Patient patient) {
        ComplaintDTO complaintDTO = new ComplaintDTO(
                patient.getComplaint().getName(),
                patient.getComplaint().getDescription());

        return new PatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getUser().getUsername(),
                complaintDTO
        );
    }
}