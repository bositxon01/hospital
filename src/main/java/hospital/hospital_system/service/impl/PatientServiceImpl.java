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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final UserRepository userRepository;

    private final ComplaintRepository complaintRepository;

    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResult<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        if (patients.isEmpty()) {
            return ApiResult.success("No patients found");
        }

        List<PatientDTO> patientDTOList = patients.stream()
                .map(PatientServiceImpl::getPatientDTO).toList();

        return ApiResult.success(patientDTOList);
    }

    @Override
    public ApiResult<PatientDTO> getPatient(Integer id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id: " + id);
        }

        Patient patient = optionalPatient.get();

        PatientDTO patientDTO = getPatientDTO(patient);

        return ApiResult.success(patientDTO);
    }

    @Override
    public ApiResult<PatientDTO> create(PatientDTO patientDTO) {
        ComplaintDTO complaintDTO = patientDTO.getComplaintDTO();
        Complaint complaint = new Complaint();

        complaint.setName(complaintDTO.getName());

        if (Objects.nonNull(complaintDTO.getDescription())) {
            complaint.setDescription(complaintDTO.getDescription());
        }
        complaintRepository.save(complaint);

        Optional<User> optionalUser = userRepository.findByUsername(patientDTO.getUsername());
        Patient patient = new Patient();
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

        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setComplaint(complaint);
        patient.setUser(user);
        patientRepository.save(patient);

        patientDTO.setId(patient.getId());

        return ApiResult.success("Patient created successfully");
    }

    @Transactional
    @Override
    public ApiResult<PatientDTO> delete(Integer id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id: " + id);
        }

        patientRepository.deletePatientById(id);
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