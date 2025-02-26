package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDTO;
import hospital.hospital_system.payload.AppointmentPostDTO;
import hospital.hospital_system.payload.EmployeeAvailableSlotsDTO;
import hospital.hospital_system.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@Tag(name = "Appointment API", description = "Appointment CRUD API")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<ApiResult<List<AppointmentGetDTO>>> getAppointments() {
        ApiResult<List<AppointmentGetDTO>> all = appointmentService.getAllAppointments();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<AppointmentGetDTO>> getAppointment(@PathVariable Integer id) {
        ApiResult<AppointmentGetDTO> appointmentGetDtoApiResult = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentGetDtoApiResult);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<AppointmentGetDTO>> createAppointment(@RequestBody AppointmentPostDTO appointmentPostDto) {
        ApiResult<AppointmentGetDTO> apiResult = appointmentService.createAppointment(appointmentPostDto);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<EmployeeAvailableSlotsDTO>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAvailableSlots(date));
    }

}
