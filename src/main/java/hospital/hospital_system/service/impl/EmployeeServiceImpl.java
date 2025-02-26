package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Attachment;
import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.*;
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
    public ApiResult<List<EmployeeGetDTO>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findByDeletedFalse();

        List<EmployeeGetDTO> employeeDTOS = employees.stream()
                .map(this::getEmployeeGetDTO)
                .toList();
        return ApiResult.success(employeeDTOS);
    }


    @Override
    public ApiResult<EmployeeGetDTO> getEmployeeById(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(id);
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        Employee employee = optionalEmployee.get();

        EmployeeGetDTO employeeGetDTO = getEmployeeGetDTO(employee);
        return ApiResult.success(employeeGetDTO);
    }

    @Override
    @Transactional
    public ApiResult<EmployeeAndUserDTO> createEmployee(EmployeeAndUserDTO employeeDTO) {
        String userEmail = employeeDTO.getUsername();
        if (userRepository.existsByUsernameAndDeletedFalse(userEmail)) {
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
//        userRepository.save(user);

        Optional<Attachment> optionalAttachment;
        Employee employee = new Employee();

        Integer attachmentId = employeeDTO.getAttachmentId();
        if (Objects.nonNull(attachmentId)) {
            optionalAttachment = attachmentRepository.findByIdAndDeletedFalse(attachmentId);
            if (optionalAttachment.isEmpty()) {
                return ApiResult.error("Attachment not found with id: " + attachmentId);
            }
            employee.setAttachment(optionalAttachment.get());
        }

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDateOfBirth(employeeDTO.getBirthDate());
        employee.setSpecialization(employeeDTO.getSpecialization());

        String verificationCode = getVerificationCode();

        VerificationInfo verificationInfo = new VerificationInfo(
                verificationCode,
                user,
                employee,
                System.currentTimeMillis() + EXPIRY_TIME,
                0
        );

        verificationData.put(userEmail, verificationInfo);

        emailService.sendVerificationEmail(userEmail, verificationCode);

        // frontda boshqa page ochilib code  kiritadi

        return ApiResult.success("Verification code sent to " + userEmail);
    }

    @Override
    @Transactional
    public ApiResult<EmployeeAndUserDTO> updateEmployee(Integer id, EmployeeUpdateDTO employeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(id);
        Optional<Position> positionById = positionRepository.findPositionById(employeeDTO.getPositionId());

        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        if (positionById.isEmpty()) {
            return ApiResult.error("Position not found with id: " + employeeDTO.getPositionId());
        }

        Employee employee = optionalEmployee.get();

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDateOfBirth(employeeDTO.getBirthDate());
        employee.setSpecialization(employeeDTO.getSpecialization());
        User user = employee.getUser();
        user.setPosition(positionById.get());
        userRepository.save(user);

        employeeRepository.save(employee);
        return ApiResult.success("Employee updated successfully");
    }

    @Transactional
    @Override
    public ApiResult<String> deleteEmployee(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(id);
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        Employee employee = optionalEmployee.get();

        employee.setDeleted(true);
        employeeRepository.save(employee);

        employee.getUser().setDeleted(true);
        userRepository.save(employee.getUser());

        return ApiResult.success("Employee deleted successfully");
    }

    @Override
    @Transactional
    public ApiResult<?> verify(String email, String code) {
        VerificationInfo verificationInfo = verificationData.get(email);

        // Agar kod mavjud bo‘lmasa yoki muddati o‘tgan bo‘lsa
        if (verificationInfo == null || System.currentTimeMillis() > verificationInfo.getExpiryTime()) {
            verificationData.remove(email);
            return ApiResult.error("The verification code has expired. Please request a new one.");
        }

        // Noto‘g‘ri kod kiritilgan holat
        if (!code.equals(verificationInfo.getCode())) {
            int attempts = verificationInfo.getAttempts();
            verificationInfo.setAttempts(attempts + 1);

            // Maksimal urinishlar soni (masalan, 3 marta)
            if (attempts >= 3) {
                verificationData.remove(email);
                return ApiResult.error("Too many incorrect attempts. Please request a new verification code.");
            }

            return ApiResult.error("Incorrect verification code. You have " + (3 - attempts) + " attempts left.");
        }

        // Agar kod to‘g‘ri bo‘lsa:
        User user = verificationInfo.getUser();
        userRepository.save(user);
        Employee employee = verificationInfo.getEmployee();
        employee.setUser(user);
        employeeRepository.save(employee);

        // Muvaffaqiyatli tasdiqlashdan so‘ng barcha vaqtinchalik ma’lumotlarni o‘chiramiz
        verificationData.remove(email);
        return ApiResult.success("Verification successful.");
    }

    @Override
    @Transactional
    public ApiResult<String> updateEmployeeAttachment(EmployeeAttachmentDto attachmentDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(attachmentDto.getEmployeeId());
        Optional<Attachment> optionalAttachment = attachmentRepository.findByIdAndDeletedFalse(attachmentDto.getAttachmentId());
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + attachmentDto.getEmployeeId());
        }
        if (optionalAttachment.isEmpty()) {
            return ApiResult.error("Attachment not found with id: " + attachmentDto.getAttachmentId());
        }
        Employee employee = optionalEmployee.get();
        Attachment attachment = optionalAttachment.get();
        employee.setAttachment(attachment);
        employeeRepository.save(employee);
        return ApiResult.success("Employee updated successfully");
    }


    private EmployeeGetDTO getEmployeeGetDTO(Employee employee) {
        EmployeeGetDTO employeeGetDTO = new EmployeeGetDTO();
        employeeGetDTO.setId(employee.getId());
        employeeGetDTO.setFirstName(employee.getFirstName());
        employeeGetDTO.setLastName(employee.getLastName());
        employeeGetDTO.setBirthDate(employee.getDateOfBirth());
        employeeGetDTO.setSpecialization(employee.getSpecialization());
        if (employee.getAttachment() != null) {
            employeeGetDTO.setAttachmentId(employee.getAttachment().getId());
        }
        employeeGetDTO.setUsername(employee.getUser().getUsername());
        employeeGetDTO.setPosition(employee.getUser().getPosition().getName());
        employeeGetDTO.setPositionId(employee.getUser().getPosition().getId());
        employeeGetDTO.setSalary(employee.getUser().getPosition().getSalary());
        return employeeGetDTO;
    }

    private static String getVerificationCode() {
        return String.valueOf(new Random().nextInt(100000, 999999));
    }
}
