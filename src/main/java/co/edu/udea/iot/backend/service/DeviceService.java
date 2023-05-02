package co.edu.udea.iot.backend.service;

import co.edu.udea.iot.backend.dto.DeviceDTO;
import co.edu.udea.iot.backend.exception.DataDuplicatedException;
import co.edu.udea.iot.backend.mapper.DeviceMapper;
import co.edu.udea.iot.backend.model.Device;
import co.edu.udea.iot.backend.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class DeviceService {

    private final DeviceMapper mapper;
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceMapper mapper, DeviceRepository deviceRepository) {
        this.mapper = mapper;
        this.deviceRepository = deviceRepository;
    }

    public DeviceDTO saveDevice(DeviceDTO deviceDTO) {
        Optional<Device> deviceOptional = deviceRepository.findByName(deviceDTO.getName());
        if (deviceOptional.isPresent()) {
            throw new DataDuplicatedException(String.format("There is already a device with name: %s", deviceDTO.getName()));
        }
        Device device = mapper.toEntity(deviceDTO);
        device.setHomeId(1L);
        Device savedDevice = deviceRepository.save(device);
        return mapper.toDto(savedDevice);
    }
}
