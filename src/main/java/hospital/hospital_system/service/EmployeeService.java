package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeAndUserDTO;
import hospital.hospital_system.payload.EmployeeGetDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    ApiResult<List<EmployeeGetDTO>> getAllEmployees();

    ApiResult<EmployeeGetDTO> getEmployeeById(Integer id);

    ApiResult<EmployeeAndUserDTO> createEmployee(EmployeeAndUserDTO employeeDTO);

    ApiResult<EmployeeAndUserDTO> updateEmployee(Integer id, EmployeeAndUserDTO employeeDTO);

    ApiResult<EmployeeAndUserDTO> deleteEmployee(Integer id);

    ApiResult<List<EmployeeGetDTO>> findByFirstNameOrLastName(String firstName, String lastName);

    ApiResult<List<EmployeeGetDTO>> searchSpecialization(String specialization);

    ApiResult<?> verify(String email, String code);

    ApiResult<?> forgetPassword(String email);

    ApiResult<?> verifyResetCode(String email, String code);

    ApiResult<?> resetPassword(String email, String newPassword);
}
