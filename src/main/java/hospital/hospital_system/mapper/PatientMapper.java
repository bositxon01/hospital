package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Complaint;
import hospital.hospital_system.entity.Patient;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ComplaintDTO;
import hospital.hospital_system.payload.PatientDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientMapper {

    @Mapping(source = "complaint", target = "complaintDTO")
    @Mapping(source = "user.username", target = "username")
    PatientDTO toDTO(Patient patient);

    List<PatientDTO> toDTO(List<Patient> patients);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "complaintDTO", target = "complaint")
    Patient toEntity(PatientDTO patientDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "complaintDTO", target = "complaint")
    void updateEntity(@MappingTarget Patient patient, PatientDTO patientDTO);

    @Mapping(target = "id", ignore = true)
    void updateComplaintEntity(@MappingTarget Complaint complaint, ComplaintDTO complaintDTO);

    @Mapping(target = "id", ignore = true)
    Complaint toEntity(ComplaintDTO complaintDTO);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntityFromDTO(PatientDTO patientDTO);

}