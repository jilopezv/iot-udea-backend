package co.edu.udea.iot.backend.mapper;

import co.edu.udea.iot.backend.dto.DeviceDTO;
import co.edu.udea.iot.backend.model.Device;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    Device toEntity(DeviceDTO dto);

    DeviceDTO toDto(Device device);

    List<Device> toEntity(List<DeviceDTO> dto);

    List<DeviceDTO> toDto(List<Device> device);
}
