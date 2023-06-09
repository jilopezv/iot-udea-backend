package co.edu.udea.iot.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "homes")
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;

    //relationships
    @OneToMany(mappedBy = "home", fetch = FetchType.LAZY)
    private Set<Device> devices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return Status.valueOf(status);
    }

    public void setStatus(Status status) {
        this.status = status.name();
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public enum Status {
        ONLINE, OFFLINE;
    }

}
