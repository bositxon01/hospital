package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Appointment;
import hospital.hospital_system.payload.AppointmentGetDTO;
import hospital.hospital_system.payload.AppointmentPostDTO;
import org.mapstruct.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentMapper {

    @Mapping(source = "patient", target = "patient")
    @Mapping(source = "employee", target = "employee")
    @Mapping(source = "room", target = "room")
    @Mapping(source = "appointmentResult", target = "appointmentResultId")
    AppointmentGetDTO toDTO(Appointment appointment);

    List<AppointmentGetDTO> toDTO(List<Appointment> appointments);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "appointmentResult", ignore = true)
    @Mapping(source = "appointmentTime", target = "appointmentTime", qualifiedByName = "mapLocalDateTimeToTimestamp")
    Appointment toEntity(AppointmentPostDTO dto);

    @Named("mapLocalDateTimeToTimestamp")
    static Timestamp mapLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime != null ? Timestamp.valueOf(localDateTime) : null;
    }

}