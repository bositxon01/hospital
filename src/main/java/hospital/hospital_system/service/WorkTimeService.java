package hospital.hospital_system.service;

import hospital.hospital_system.enums.DayEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.WorkTimeDTO;
import hospital.hospital_system.payload.WorkTimeWithIdDTO;

import java.util.List;

public interface WorkTimeService {

    ApiResult<WorkTimeDTO> getWorkTimeById(Integer id);

    ApiResult<List<WorkTimeDTO>> getWorkTimes();

    ApiResult<WorkTimeDTO> createWorkTime(WorkTimeWithIdDTO workTimeWithIdDTO);

    ApiResult<List<WorkTimeDTO>> getWorkTimesByEmployeeAndDay(Integer employeeId, DayEnum day);

    ApiResult<WorkTimeDTO> updateWorkTime(Integer id, WorkTimeWithIdDTO workTimeWithIdDTO);

    ApiResult<String> deleteWorkTime(Integer id);

}