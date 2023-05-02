package co.edu.udea.iot.backend.service;

import co.edu.udea.iot.backend.broker.HomeBroker;
import co.edu.udea.iot.backend.broker.webclient.WebPublisher;
import co.edu.udea.iot.backend.dto.DeviceDTO;
import co.edu.udea.iot.backend.mapper.DeviceMapper;
import co.edu.udea.iot.backend.model.Device;
import co.edu.udea.iot.backend.model.Home;
import co.edu.udea.iot.backend.model.Message;
import co.edu.udea.iot.backend.repository.DeviceRepository;
import co.edu.udea.iot.backend.repository.HomeRepository;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    private HomeRepository homeRepository;

    private DeviceRepository deviceRepository;

    private DeviceMapper deviceMapper;

    private final HomeBroker broker;

    private final WebPublisher  webPublisher;


    public HomeService(HomeRepository homeRepository, DeviceRepository deviceRepository,
                       DeviceMapper deviceMapper, HomeBroker broker, WebPublisher webPublisher) {
        this.homeRepository = homeRepository;
        this.deviceRepository = deviceRepository;
        this.broker = broker;
        this.webPublisher = webPublisher;
        this.deviceMapper = deviceMapper;
        try {
            this.broker.listen("home_outbound", this::processMessageFromHome);
        } catch (MqttException mqttException) {
            mqttException.printStackTrace();
        }
    }

    private void processMessageFromHome(String topic, MqttMessage message) {
        System.out.println("Mensaje recibido para manejar con lógica de negocio: "+ message);
    }

    public List<Home> findAllHomes() {
        return homeRepository.findAll();
    }

    public void sendMessageToHome(String message) {
        try {
            this.broker.publish(message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param message with the structure: home_name; {device_name, device_status}
     */
    public void processMessage(String message) {
        // splitting the received message
        String[] tokens = message.split(";");
        // getting home name
        String homeName = tokens[0];
        // searching for the home in the db
        Optional<Home> homeOptional = homeRepository.findByName(homeName);
        if (!homeOptional.isPresent()) {
            System.err.println("A MESSAGE FROM AN UNKNOWN HOME HAS BEEN RECEIVED {" + homeName + "}");
            return;
        }
        Home home = homeOptional.get();
        // checking whether the home status is OFFLINE and updating it to ONLINE
        if (Home.Status.OFFLINE.equals(home.getStatus())) {
            home.setStatus(Home.Status.ONLINE);
            homeRepository.save(home);
        }
        // parsing every device notification
        for (int i = 1; i < tokens.length; i++) {
            String[] subtoken = tokens[i].split(",");
            String deviceName = subtoken[0];
            String deviceStatus = subtoken[1];
            Optional<Device> deviceOptional = deviceRepository.findByName(deviceName);
            if (deviceOptional.isEmpty()) {
                System.err.println("A MESSAGE FROM A UNKNOWN DEVICE HAS BEEN RECEIVED {" + deviceName + "}");
                return;
            }
            Device device = deviceOptional.get();
            Device.Status status;
            try {
                status = Device.Status.valueOf(deviceStatus);
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                return;
            }
            device.setStatus(status);
            device.setLastUpdated(LocalDateTime.now());
            deviceRepository.save(device);
        }
    }

    public List<DeviceDTO> findAllDevices() {
        return deviceMapper.toDto(deviceRepository.findAll());
    }

    public void sendMessageToWebClient(String message) {
        try {
            this.webPublisher.publish(message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
