package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Turn;
import hospital.hospital_system.entity.WorkTime;
import hospital.hospital_system.payload.*;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.TurnRepository;
import hospital.hospital_system.repository.WorkTimeRepository;
import hospital.hospital_system.service.WorkTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkTimeServiceImpl implements WorkTimeService {

    private final WorkTimeRepository workTimeRepository;
    private final EmployeeRepository employeeRepository;
    private final TurnRepository turnRepository;

    @Override
    public ApiResult<WorkTimeDTO> create(WorkTimeWithIdDTO workTimeWithIdDTO) {
        Integer employeeId = workTimeWithIdDTO.getEmployee_id();
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + employeeId);
        }

        Integer turnId = workTimeWithIdDTO.getTurn_id();
        Optional<Turn> optionalTurn = turnRepository.findById(turnId);

        if (optionalTurn.isEmpty()) {
            return ApiResult.error("Turn not found with id: " + turnId);
        }

        Employee employee = optionalEmployee.get();
        Turn turn = optionalTurn.get();

        boolean existsByEmployeeIdAndTurnIdAndDay = workTimeRepository.existsByEmployeeIdAndTurnIdAndDay(
                employee.getId(),
                turn.getId(),
                workTimeWithIdDTO.getDay()
        );

        if (existsByEmployeeIdAndTurnIdAndDay) {
            return ApiResult.error("Employee already exists with id: " + employee.getId() + " and turn id: " + turn.getId());
        }

        WorkTime workTime = new WorkTime();
        workTime.setEmployee(employee);
        workTime.setTurn(turn);
        workTime.setDay(workTimeWithIdDTO.getDay());
        workTimeRepository.save(workTime);

        return ApiResult.success("Work-time created successfully");
    }

    @Override
    public ApiResult<List<WorkTimeDTO>> getWorkTimes() {
        List<WorkTime> workTimeList = workTimeRepository.findAll();

        if (workTimeList.isEmpty()) {
            return ApiResult.success("No work-time found");
        }

        List<WorkTimeDTO> workTimeDTOS = workTimeList.stream()
                .map(workTime ->
                {
                    Employee employee = workTime.getEmployee();

                    Turn turn = workTime.getTurn();

                    return new WorkTimeDTO(
                            new EmployeeDTO(
                                    employee.getId(),
                                    employee.getFirstName(),
                                    employee.getLastName(),
                                    employee.getDateOfBirth(),
                                    employee.getSpecialization()),
                            workTime.getDay(),
                            new TurnDTO(
                                    turn.getId(),
                                    turn.getName(),
                                    turn.getStartTime(),
                                    turn.getEndTime()
                            )
                    );
                }).toList();

        return ApiResult.success(workTimeDTOS);
    }

    @Override
    public ApiResult<WorkTimeDTO> getWorkTimeById(int id) {
        Optional<WorkTime> optionalWorkTime = workTimeRepository.findById(id);

        if (optionalWorkTime.isEmpty()) {
            return ApiResult.error("Work-time not found with id: " + id);
        }

        WorkTime workTime = optionalWorkTime.get();

        Employee employee = workTime.getEmployee();

        Turn turn = workTime.getTurn();

        WorkTimeDTO workTimeDTO = new WorkTimeDTO(
                new EmployeeDTO(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getDateOfBirth(),
                        employee.getSpecialization()),
                workTime.getDay(),
                new TurnDTO(
                        turn.getId(),
                        turn.getName(),
                        turn.getStartTime(),
                        turn.getEndTime()
                )
        );

        return ApiResult.success(workTimeDTO);
    }

    @Override
    public ApiResult<WorkTimeDTO> updateWorkTime(int id, WorkTimeWithIdDTO workTimeWithIdDTO) {

        Optional<WorkTime> optionalWorkTime = workTimeRepository.findById(id);

        if (optionalWorkTime.isEmpty()) {
            return ApiResult.error("Work-time not found with id: " + id);
        }

        WorkTime workTime = optionalWorkTime.get();

        Optional<Employee> optionalEmployee = employeeRepository.findById(workTimeWithIdDTO.getEmployee_id());

        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id: " + id);
        }

        Optional<Turn> optionalTurn = turnRepository.findById(workTimeWithIdDTO.getTurn_id());

        if (optionalTurn.isEmpty()) {
            return ApiResult.error("Turn not found with id: " + id);
        }

        Employee employee = optionalEmployee.get();
        Turn turn = optionalTurn.get();

        boolean existsByEmployeeIdAndTurnIdAndDay = workTimeRepository.existsByEmployeeIdAndTurnIdAndDay(
                employee.getId(),
                turn.getId(),
                workTimeWithIdDTO.getDay()
        );

        if (existsByEmployeeIdAndTurnIdAndDay) {
            return ApiResult.error("Employee already exists with id: " + id + " and turn id: " + turn.getId());
        }

        workTime.setEmployee(employee);
        workTime.setTurn(turn);
        workTime.setDay(workTimeWithIdDTO.getDay());
        workTimeRepository.save(workTime);

        return ApiResult.success("Work-time updated successfully");
    }

    @Override
    public ApiResult<WorkTimeDTO> delete(int id) {
        Optional<WorkTime> optionalWorkTime = workTimeRepository.findById(id);

        if (optionalWorkTime.isEmpty()) {
            return ApiResult.error("Work-time not found with id: " + id);
        }

        WorkTime workTime = optionalWorkTime.get();
        workTimeRepository.delete(workTime);
        return ApiResult.success("Work-time deleted successfully");
    }
}
