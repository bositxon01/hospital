package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.EmployeeDTO;
import hospital.hospital_system.payload.HomeDto;
import hospital.hospital_system.service.HomeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Override
    public ApiResult<HomeDto> getHome() {


        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            List<PositionPermission> positionPermissionList = user.getPosition().getPositionPermissionList();
            List<PermissionEnum> list = positionPermissionList.stream()
                    .map(PositionPermission::getPermission)
                    .toList();

            HomeDto homeDto = getHomeDtoLinks(list);
            Employee employee = user.getEmployee();
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setFirstName(employee.getFirstName());
            employeeDTO.setLastName(employee.getLastName());
            employeeDTO.setBirthDate(employee.getDateOfBirth());
            employeeDTO.setSpecialization(employee.getSpecialization());
            homeDto.setEmployeeDto(employeeDTO);
            if (employee.getAttachment() != null) {
                String path = employee.getAttachment().getPath().replace("\\", "/");
                homeDto.setAttachmentPath(path);
            }


            return ApiResult.success(homeDto);
        } catch (Exception e) {
            return ApiResult.error(e.getMessage());
        }
    }

    public HomeDto getHomeDtoLinks(List<PermissionEnum> permissionList) {
        HomeDto homeDto = new HomeDto();
        if (permissionList.contains(PermissionEnum.VIEW_POSITION)) {
            homeDto.setPositionLink("Position.html");
        }
        if (permissionList.contains(PermissionEnum.VIEW_APPOINTMENT)) {
            homeDto.setAppointmentLink("Appointment.html");
        }
        if (permissionList.contains(PermissionEnum.VIEW_WORK_TIME)) {
            homeDto.setWorkTimeLink("WorkTime.html");
        }
        if (permissionList.contains(PermissionEnum.VIEW_EMPLOYEE)) {
            homeDto.setEmployeeLink("Employee.html");
        }
        if (permissionList.contains(PermissionEnum.VIEW_EMPLOYEE_ROOM)) {
            homeDto.setEmployeeRoomLink("EmployeeRoom.html");
        }
        return homeDto;
    }

}
