package hospital.hospital_system.service;

import hospital.hospital_system.enums.DayEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.WorkTimeDTO;
import hospital.hospital_system.payload.WorkTimeWithIdDTO;

import java.util.List;

public interface WorkTimeService {
    ApiResult<WorkTimeDTO> createWorkTime(WorkTimeWithIdDTO workTimeWithIdDTO);

    ApiResult<List<WorkTimeDTO>> getWorkTimes();

    ApiResult<WorkTimeDTO> getWorkTimeById(int id);

    ApiResult<List<WorkTimeDTO>> getWorkTimesByEmployeeAndDay(Integer employeeId, DayEnum day);

    ApiResult<WorkTimeDTO> updateWorkTime(int id, WorkTimeWithIdDTO workTimeWithIdDTO);

    ApiResult<WorkTimeDTO> deleteWorkTime(int id);
}
