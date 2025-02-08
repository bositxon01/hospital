package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Attachment;
import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeAndUserDTO;
import hospital.hospital_system.payload.EmployeeGetDTO;
import hospital.hospital_system.payload.VerificationInfo;
import hospital.hospital_system.repository.AttachmentRepository;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PositionRepository;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.service.EmailService;
import hospital.hospital_system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;
    private final EmailService emailService;

    private final Map<String, VerificationInfo> verificationData = new ConcurrentHashMap<>();

    private static final Integer EXPIRY_TIME = 60_000;

    @Override
    @Transactional(readOnly = true)
    public ApiResult<List<EmployeeGetDTO>> getAllEmployees() {
        return ApiResult.success(employeeRepository.findAll()
                .stream()
                .map(EmployeeServiceImpl::getEmployeeGetDTO)
                .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResult<EmployeeGetDTO> getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(EmployeeServiceImpl::getEmployeeGetDTO)
                .map(ApiResult::success)
                .orElseGet(() -> ApiResult.error("Employee not found with id: " + id));
    }

    @Override
    @Transactional
    public ApiResult<String> createEmployee(EmployeeAndUserDTO employeeDTO) {
        String userEmail = employeeDTO.getUsername();

        if (userRepository.existsByUsername(userEmail)) {
            return ApiResult.error("Employee already exists with username: " + userEmail);
        }

        Optional<Position> optionalPosition = positionRepository.findById(employeeDTO.getPositionId());

        if (optionalPosition.isEmpty()) {
            return ApiResult.error("Position not found with id: " + employeeDTO.getPositionId());
        }

        User user = new User();

        user.setUsername(userEmail);
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        user.setPosition(optionalPosition.get());

        Optional<Attachment> optionalAttachment;
        Employee employee = new Employee();
        Integer attachmentId = employeeDTO.getAttachmentId();

        if (Objects.nonNull(attachmentId)) {
            optionalAttachment = attachmentRepository.findById(attachmentId);
            if (optionalAttachment.isEmpty()) {
                return ApiResult.error("Attachment not found with id: " + attachmentId);
            }
            employee.setAttachment(optionalAttachment.get());
        }

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDateOfBirth(employeeDTO.getBirthDate());
        employee.setSpecialization(employeeDTO.getSpecialization());

        String verificationCode = generateVerificationCode();

        VerificationInfo verificationInfo = new VerificationInfo(
                verificationCode,
                user,
                employee,
                System.currentTimeMillis() + EXPIRY_TIME,
                0
        );

        verificationData.put(userEmail, verificationInfo);
        emailService.sendVerificationEmail(userEmail, verificationCode);

        // frontda boshqa page ochilib code kiritadi

        return ApiResult.success("Verification code sent to " + userEmail);
    }

    @Override
    @Transactional
    public ApiResult<String> verify(String email, String code) {
        VerificationInfo verificationInfo = verificationData.get(email);

        if (verificationInfo == null || System.currentTimeMillis() > verificationInfo.getExpiryTime()) {
            verificationData.remove(email);
            return ApiResult.error("The verification code has expired. Please try again.");
        }

        if (!code.equals(verificationInfo.getCode())) {
            int attempts = verificationInfo.getAttempts();
            verificationInfo.setAttempts(attempts + 1);
            if (attempts >= 3) {
                verificationData.remove(email);
                return ApiResult.error("Too many incorrect attempts. Please request a new verification code.");
            }
            return ApiResult.error("Incorrect verification code. You have " + (3 - attempts) + " attempts left.");
        }

        User user = verificationInfo.getUser();
        userRepository.save(user);

        Employee employee = verificationInfo.getEmployee();
        employee.setUser(user);
        employeeRepository.save(employee);

        verificationData.remove(email);
        return ApiResult.success("Verification successful.");
    }

    @Override
    @Transactional
    public ApiResult<String> updateEmployee(Integer id, EmployeeAndUserDTO employeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        Employee employee = optionalEmployee.get();

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDateOfBirth(employeeDTO.getBirthDate());
        employee.setSpecialization(employeeDTO.getSpecialization());

        employeeRepository.save(employee);

        return ApiResult.success("Employee updated successfully");
    }

    @Override
    @Transactional
    public ApiResult<String> deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employeeRepository.delete(employee);

        return ApiResult.success("Employee deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResult<List<EmployeeGetDTO>> searchByFirstNameAndLastName(String firstName, String lastName) {
        List<Employee> searchFirstNameOrLastname = employeeRepository.findByFirstNameOrLastName(firstName, lastName);

        if (searchFirstNameOrLastname.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }

        return getListApiResult(searchFirstNameOrLastname);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResult<List<EmployeeGetDTO>> searchSpecialization(String specialization) {
        List<Employee> employees = employeeRepository.findBySpecializationContaining(specialization);

        if (employees.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }

        return getListApiResult(employees);
    }

    private static EmployeeGetDTO getEmployeeGetDTO(Employee employee) {
        return new EmployeeGetDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDateOfBirth(),
                employee.getSpecialization(),
                employee.getAttachment().getId(),
                employee.getUser().getPosition().getName(),
                employee.getUser().getPosition().getId(),
                employee.getUser().getPosition().getSalary()
        );
    }

    private ApiResult<List<EmployeeGetDTO>> getListApiResult(List<Employee> employees) {
        List<EmployeeGetDTO> employeeGetDTOList = employees
                .stream()
                .map(EmployeeServiceImpl::getEmployeeGetDTO)
                .toList();

        return ApiResult.success(employeeGetDTOList);
    }

    private static String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(100_000, 999_999));
    }
}
