package hospital.hospital_system.mapper;

import hospital.hospital_system.entity.Attachment;
import hospital.hospital_system.payload.AttachmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper {

    AttachmentDTO toDTO(Attachment attachment);

}