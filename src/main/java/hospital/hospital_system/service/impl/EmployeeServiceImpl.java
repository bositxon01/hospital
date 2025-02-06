package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Attachment;
import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeAndUserDTO;
import hospital.hospital_system.payload.EmployeeGetDTO;
import hospital.hospital_system.payload.EmployeeUpdateDto;
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


    private final Map<String, Integer> attemptCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> codeExpiryTimes = new ConcurrentHashMap<>();
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, User> codeAndUserMap = new ConcurrentHashMap<>();
    private final Map<String, Employee> codeAndEmployeeMap = new ConcurrentHashMap<>();


    @Override
    public ApiResult<List<EmployeeGetDTO>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return ApiResult.success("Employees not found");
        }

        return getListApiResult(employees);
    }


    @Override
    public ApiResult<EmployeeGetDTO> getEmployeeById(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        Employee employee = optionalEmployee.get();

        EmployeeGetDTO employeeGetDTO = new EmployeeGetDTO(
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

        return ApiResult.success(employeeGetDTO);
    }

    @Override
    public ApiResult<EmployeeAndUserDTO> createEmployee(EmployeeAndUserDTO employeeDTO) {
        String userEmail = employeeDTO.getUsername();
        Optional<User> username = userRepository.findByUsername(userEmail);
        if (username.isPresent()) {
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

//        employee.setUser(user);

//        employeeRepository.save(employee);

//        user.setEmployee(employee);
//        userRepository.save(user);

        String verificationCode = String.valueOf(new Random().nextInt(100000, 999999));
        verificationCodes.put(userEmail, verificationCode);
        codeAndUserMap.put(verificationCode, user);
        codeAndEmployeeMap.put(verificationCode, employee);
        codeExpiryTimes.put(userEmail, System.currentTimeMillis() + 60000);


        emailService.sendVerificationEmail(userEmail, verificationCode);

        // frontda boshqa page ochilib code  kiritadi

        return ApiResult.success("Verification code sent to " + userEmail);
    }

    @Override
    public ApiResult<EmployeeAndUserDTO> updateEmployee(Integer id, EmployeeUpdateDto employeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Optional<Position> positionById = positionRepository.findPositionById(employeeDTO.getPositionId());
        if (optionalEmployee.isEmpty() || positionById.isEmpty()) {
            return ApiResult.error("Employee or Position not found with id: " + id + " " + employeeDTO.getPositionId());
        }

        Employee employee = optionalEmployee.get();

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDateOfBirth(employeeDTO.getBirthDate());
        employee.setSpecialization(employeeDTO.getSpecialization());
        User user = employee.getUser();
        user.setPosition(positionById.get());

        employeeRepository.save(employee);
        return ApiResult.success("Employee updated successfully");
    }

    @Transactional
    @Override
    public ApiResult<EmployeeAndUserDTO> deleteEmployee(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        Employee employee = optionalEmployee.get();
        employee.setDeleted(true);
        employeeRepository.save(employee);

        return ApiResult.success("Employee deleted successfully");
    }

    @Override
    public ApiResult<List<EmployeeGetDTO>> findByFirstNameOrLastName(String firstName, String lastName) {
        List<Employee> searchFirstNameOrLastname = employeeRepository.findByFirstNameOrLastName(firstName, lastName);
        if (searchFirstNameOrLastname.isEmpty()) {
            return ApiResult.error("Employee not found with " + firstName + " " + lastName);
        }


        return getListApiResult(searchFirstNameOrLastname);

    }

    @Override
    public ApiResult<List<EmployeeGetDTO>> searchSpecialization(String specialization) {
        List<Employee> employees = employeeRepository.findBySpecializationContaining(specialization);
        if (employees.isEmpty()) {
            return ApiResult.error("Employee not found with specialization " + specialization);
        }


        return getListApiResult(employees);
    }

    private ApiResult<List<EmployeeGetDTO>> getListApiResult(List<Employee> employees) {
        List<EmployeeGetDTO> employeeGetDTOList = employees.stream()
                .map(employee -> new EmployeeGetDTO(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getDateOfBirth(),
                        employee.getSpecialization(),
                        employee.getAttachment().getId(),
                        employee.getUser().getPosition().getName(),
                        employee.getUser().getPosition().getId(),
                        employee.getUser().getPosition().getSalary()

                )).toList();

        return ApiResult.success(employeeGetDTOList);
    }


    @Override
    public ApiResult<?> verify(String email, String code) {
        String verificationCode = verificationCodes.get(email);

        // Agar kod mavjud bo‘lmasa yoki muddati o‘tgan bo‘lsa
        if (verificationCode == null || System.currentTimeMillis() > codeExpiryTimes.getOrDefault(email, 0L)) {
            verificationCodes.remove(email);
            attemptCounts.remove(email);
            return ApiResult.error("The verification code has expired. Please request a new one.");
        }

        // Urinishlar sonini olish yoki boshlang‘ich qiymat berish
        int attempts = attemptCounts.getOrDefault(email, 0);

        // Noto‘g‘ri kod kiritilgan holat
        if (!code.equals(verificationCode)) {
            attempts++;
            attemptCounts.put(email, attempts);

            // Maksimal urinishlar soni (masalan, 3 marta)
            if (attempts >= 3) {
                verificationCodes.remove(email);
                attemptCounts.remove(email);
                codeAndUserMap.remove(email);
                codeExpiryTimes.remove(email);
                codeAndEmployeeMap.remove(email);
                return ApiResult.error("Too many incorrect attempts. Please request a new verification code.");
            }

            return ApiResult.error("Incorrect verification code. You have " + (3 - attempts) + " attempts left.");
        }

        // Agar kod to‘g‘ri bo‘lsa:
        User user = codeAndUserMap.get(verificationCode);
        Employee employee = codeAndEmployeeMap.get(verificationCode);

        userRepository.save(user);
        employee.setUser(user);
        employeeRepository.save(employee);

        // Muvaffaqiyatli tasdiqlashdan so‘ng barcha vaqtinchalik ma’lumotlarni o‘chiramiz
        verificationCodes.remove(email);
        codeAndUserMap.remove(verificationCode);
        codeAndEmployeeMap.remove(verificationCode);
        attemptCounts.remove(email);
        codeExpiryTimes.remove(email);

        return ApiResult.success("Verification successful.");
    }

}
