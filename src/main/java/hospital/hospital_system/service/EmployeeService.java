package hospital.hospital_system.service;

import hospital.hospital_system.payload.*;

import java.util.List;

public interface EmployeeService {

    ApiResult<List<EmployeeGetDTO>> getAllEmployees();

    ApiResult<EmployeeGetDTO> getEmployeeById(Integer id);

    ApiResult<EmployeeAndUserDTO> createEmployee(EmployeeAndUserDTO employeeDTO);

    ApiResult<EmployeeAndUserDTO> updateEmployee(Integer id, EmployeeUpdateDTO employeeUpdateDto);

    ApiResult<String> deleteEmployee(Integer id);

    ApiResult<?> verify(String email, String code);

    ApiResult<String> updateEmployeeAttachment(EmployeeAttachmentDto attachmentDto);
}
