package hospital.hospital_system.service;

import hospital.hospital_system.payload.*;

import java.util.List;
import java.util.Objects;

public interface EmployeeService {

    ApiResult<EmployeeGetDTO> getEmployeeById(Integer id);

    ApiResult<List<EmployeeGetDTO>> getAllEmployees();

    ApiResult<EmployeeAndUserDTO> createEmployee(EmployeeAndUserDTO employeeDTO);

    ApiResult<EmployeeAndUserDTO> updateEmployee(Integer id, EmployeeUpdateDTO employeeUpdateDto);

    ApiResult<String> deleteEmployee(Integer id);

    ApiResult<?> verify(String email, String code);

    ApiResult<Object> updateEmployeeAttachment(EmployeeAttachmentDto attachmentDto);

}