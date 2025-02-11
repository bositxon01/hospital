package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import hospital.hospital_system.payload.DoctorAvailableSlotsDto;
import hospital.hospital_system.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<ApiResult<List<AppointmentGetDto>>> getAppointments() {
        ApiResult<List<AppointmentGetDto>> all = appointmentService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<AppointmentGetDto>> getAppointment(@PathVariable Integer id) {
        ApiResult<AppointmentGetDto> appointmentGetDtoApiResult = appointmentService.get(id);
        return ResponseEntity.ok(appointmentGetDtoApiResult);
    }

    /*@PostMapping("/create")
    public ResponseEntity<ApiResult<String>> createAppointment() {

    }*/

    @GetMapping("/available-slots")
    public ResponseEntity<List<DoctorAvailableSlotsDto>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAvailableSlots(date));
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentPostDto requestDto) {
        appointmentService.bookAppointment(requestDto);
        return ResponseEntity.ok("Appointment booked successfully!");
    }
}
