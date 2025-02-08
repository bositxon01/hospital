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

    ApiResult<String> createEmployee(EmployeeAndUserDTO employeeDTO);

    ApiResult<String> updateEmployee(Integer id, EmployeeAndUserDTO employeeDTO);

    ApiResult<String> deleteEmployee(Integer id);

    ApiResult<List<EmployeeGetDTO>> searchByFirstNameAndLastName(String firstName, String lastName);

    ApiResult<List<EmployeeGetDTO>> searchSpecialization(String specialization);

    ApiResult<String> verify(String email, String code);
}
