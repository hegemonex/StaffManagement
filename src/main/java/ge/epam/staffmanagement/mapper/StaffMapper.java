package ge.epam.staffmanagement.mapper;

import ge.epam.staffmanagement.entity.Staff;
import ge.epam.staffmanagement.model.RequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "image", ignore = true)
    Staff toEntity(RequestDTO requestDTO);

    @Mapping(source = "department.id", target = "departmentId")
    RequestDTO toDTO(Staff staff);
}