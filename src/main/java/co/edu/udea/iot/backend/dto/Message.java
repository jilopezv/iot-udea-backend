package co.edu.udea.iot.backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty(value = "home_id")
    private String homeId;
    @JsonProperty(value = "dev_id")
    private String devId;
    private String msg;

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
