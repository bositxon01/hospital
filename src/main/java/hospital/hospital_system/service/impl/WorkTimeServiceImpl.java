package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.enums.DayEnum;
import hospital.hospital_system.mapper.WorkTimeMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.WorkTimeDTO;
import hospital.hospital_system.payload.WorkTimeWithIdDTO;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.TurnRepository;
import hospital.hospital_system.repository.WorkTimeRepository;
import hospital.hospital_system.service.WorkTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkTimeServiceImpl implements WorkTimeService {

    private final WorkTimeRepository workTimeRepository;
    private final EmployeeRepository employeeRepository;
    private final TurnRepository turnRepository;
    private final WorkTimeMapper workTimeMapper;

    @Override
    public ApiResult<WorkTimeDTO> createWorkTime(WorkTimeWithIdDTO dto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(dto.getEmployee_id());

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id: " + dto.getEmployee_id());

        Optional<Turn> optionalTurn = turnRepository.findByIdAndDeletedFalse(dto.getTurn_id());

        if (optionalTurn.isEmpty())
            return ApiResult.error("Turn not found with id: " + dto.getTurn_id());

        Employee employee = optionalEmployee.get();
        Turn turn = optionalTurn.get();

        boolean exists = workTimeRepository.existsByEmployeeIdAndTurnIdAndDayAndDeletedFalse(
                employee.getId(),
                turn.getId(),
                dto.getDay());

        if (exists)
            return ApiResult.error("Employee already assigned to this turn on this day");

        WorkTime workTime = new WorkTime();

        workTime.setEmployee(employee);
        workTime.setTurn(turn);
        workTime.setDay(dto.getDay());

        workTimeRepository.save(workTime);

        return ApiResult.success("Work-time created successfully", workTimeMapper.toDTO(workTime));
    }

    @Override
    public ApiResult<List<WorkTimeDTO>> getWorkTimes() {
        List<WorkTime> workTimeList = workTimeRepository.findByDeletedFalse();

        if (workTimeList.isEmpty())
            return ApiResult.success("Work-times not found");

        return ApiResult.success(workTimeMapper.toDTO(workTimeList));
    }

    @Override
    public ApiResult<WorkTimeDTO> getWorkTimeById(Integer id) {
        return workTimeRepository.findByIdAndDeletedFalse(id)
                .map(workTimeMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Work-time not found with id: " + id));
    }

    @Override
    public ApiResult<List<WorkTimeDTO>> getWorkTimesByEmployeeAndDay(Integer employeeId, DayEnum day) {
        List<WorkTime> workTimes = workTimeRepository.findByEmployeeIdAndDayAndDeletedFalse(employeeId, day);

        if (workTimes.isEmpty())
            return ApiResult.success("Work-times not found");

        return ApiResult.success(workTimeMapper.toDTO(workTimes));
    }

    @Override
    public ApiResult<WorkTimeDTO> updateWorkTime(Integer id, WorkTimeWithIdDTO dto) {
        Optional<WorkTime> optionalWorkTime = workTimeRepository.findById(id);

        if (optionalWorkTime.isEmpty())
            return ApiResult.error("Work-time not found with id: " + id);

        Optional<Employee> optionalEmployee = employeeRepository.findById(dto.getEmployee_id());

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id: " + dto.getEmployee_id());

        Optional<Turn> optionalTurn = turnRepository.findById(dto.getTurn_id());

        if (optionalTurn.isEmpty())
            return ApiResult.error("Turn not found with id: " + dto.getTurn_id());

        WorkTime workTime = optionalWorkTime.get();
        Employee employee = optionalEmployee.get();
        Turn turn = optionalTurn.get();

        boolean exists = workTimeRepository.existsByEmployeeIdAndTurnIdAndDayAndDeletedFalse(
                employee.getId(), turn.getId(), dto.getDay());

        if (exists)
            return ApiResult.error("Employee already assigned to this turn on this day");

        workTime.setEmployee(employee);
        workTime.setTurn(turn);
        workTime.setDay(dto.getDay());

        workTimeRepository.save(workTime);

        return ApiResult.success("Work-time updated successfully", workTimeMapper.toDTO(workTime));
    }

    @Override
    public ApiResult<String> deleteWorkTime(Integer id) {
        Optional<WorkTime> optionalWorkTime = workTimeRepository.findById(id);

        if (optionalWorkTime.isEmpty())
            return ApiResult.error("Work-time not found with id: " + id);

        WorkTime workTime = optionalWorkTime.get();

        workTime.setDeleted(true);
        workTimeRepository.save(workTime);

        return ApiResult.success("Work-time deleted successfully");
    }

}