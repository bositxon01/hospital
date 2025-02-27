package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Attachment;
import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.mapper.EmployeeMapper;
import hospital.hospital_system.payload.*;
import hospital.hospital_system.repository.AttachmentRepository;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PositionRepository;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.service.EmailService;
import hospital.hospital_system.service.EmployeeService;
import hospital.hospital_system.utils.VerificationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final PositionRepository positionRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;

    private final Map<String, VerificationInfo> verificationData = new ConcurrentHashMap<>();
    private static final Integer EXPIRY_TIME = 60_000;

    @Override
    public ApiResult<List<EmployeeGetDTO>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findByDeletedFalse();

        if (employees.isEmpty())
            return ApiResult.success("Employees not found");

        return ApiResult.success(employeeMapper.toGetDTO(employees));
    }

    @Override
    public ApiResult<EmployeeGetDTO> getEmployeeById(Integer id) {
        return employeeRepository.findByIdAndDeletedFalse(id)
                .map(employeeMapper::toGetDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Employee not found with id: " + id));
    }

    @Override
    @Transactional
    public ApiResult<EmployeeAndUserDTO> createEmployee(EmployeeAndUserDTO dto) {
        if (userRepository.existsByUsernameAndDeletedFalse(dto.getUsername()))
            return ApiResult.error("Employee already exists with username: " + dto.getUsername());


        Optional<Position> optionalPosition = positionRepository.findByIdAndDeletedFalse(dto.getPositionId());

        if (optionalPosition.isEmpty())
            return ApiResult.error("Position not found with id: " + dto.getPositionId());

        Employee employee = employeeMapper.toEntity(dto);
        User user = employeeMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPosition(optionalPosition.get());

        Optional<Attachment> optionalAttachment = attachmentRepository.findByIdAndDeletedFalse(dto.getAttachmentId());

        if (optionalAttachment.isEmpty())
            return ApiResult.error("Attachment not found with id: " + dto.getAttachmentId());

        Attachment attachment = optionalAttachment.get();
        employee.setAttachment(attachment);

        String verificationCode = generateVerificationCode();

        verificationData.put(dto.getUsername(),
                new VerificationInfo(verificationCode, user, employee, System.currentTimeMillis() + EXPIRY_TIME, 0));

        emailService.sendVerificationEmail(dto.getUsername(), verificationCode);

        return ApiResult.success("Verification code sent to " + dto.getUsername());
    }

    @Override
    @Transactional
    public ApiResult<EmployeeAndUserDTO> updateEmployee(Integer id, EmployeeUpdateDTO dto) {
        Optional<Position> optionalPosition = positionRepository.findPositionByIdAndDeletedFalse(dto.getPositionId());

        if (optionalPosition.isEmpty())
            return ApiResult.error("Position not found with id: " + id);

        Position position = optionalPosition.get();

        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(id);

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id: " + id);

        Employee employee = optionalEmployee.get();

        employeeMapper.updateEntity(employee, dto);
        User user = employee.getUser();
        user.setPosition(position);
        employeeRepository.save(employee);

        return ApiResult.success("Employee updated successfully", employeeMapper.toDTO(employee));
    }

    @Override
    @Transactional
    public ApiResult<String> deleteEmployee(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(id);

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id: " + id);

        Employee employee = optionalEmployee.get();
        employee.setDeleted(true);

        User user = employee.getUser();
        user.setDeleted(true);
        userRepository.save(user);

        employeeRepository.save(employee);

        return ApiResult.success("Employee deleted successfully");
    }

    @Override
    @Transactional
    public ApiResult<?> verify(String email, String code) {
        VerificationInfo verificationInfo = verificationData.get(email);

        if (verificationInfo == null || System.currentTimeMillis() > verificationInfo.getExpiryTime()) {
            verificationData.remove(email);
            return ApiResult.error("The verification code has expired. Please request a new one.");
        }

        if (!code.equals(verificationInfo.getCode())) {
            if (verificationInfo.getAttempts() >= 3) {
                verificationData.remove(email);
                return ApiResult.error("Too many incorrect attempts. Please request a new verification code.");
            }
            verificationInfo.setAttempts(verificationInfo.getAttempts() + 1);
            return ApiResult.error("Incorrect verification code. Attempts left: " + (3 - verificationInfo.getAttempts()));
        }

        User user = verificationInfo.getUser();
        Employee employee = verificationInfo.getEmployee();
        userRepository.save(user);
        employee.setUser(user);
        employeeRepository.save(employee);

        verificationData.remove(email);
        return ApiResult.success("Verification successful.");
    }

    @Override
    @Transactional
    public ApiResult<Object> updateEmployeeAttachment(EmployeeAttachmentDto dto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(dto.getEmployeeId());

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id: " + dto.getEmployeeId());

        Employee employee = optionalEmployee.get();

        Optional<Attachment> optionalAttachment = attachmentRepository.findByIdAndDeletedFalse(dto.getAttachmentId());

        if (optionalAttachment.isEmpty())
            return ApiResult.error("Attachment not found with id: " + dto.getAttachmentId());

        Attachment attachment = optionalAttachment.get();

        employee.setAttachment(attachment);
        employeeRepository.save(employee);

        return ApiResult.success("Employee attachment updated successfully", employeeMapper.toDTO(employee));

    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(100000, 999999));
    }
}