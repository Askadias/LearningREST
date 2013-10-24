package ru.forxy.common.pojo.status;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Includes the information about System health status
 */
@XmlRootElement(name = "system_status")
public class SystemStatus implements Serializable {

    private String name;

    private String location;

    private String version;

    private StatusType status;

    private List<ComponentStatus> componentStatuses;

    public SystemStatus() {
    }

    public SystemStatus(String name,
                        String location,
                        String version,
                        StatusType status,
                        List<ComponentStatus> componentStatuses) {
        this.name = name;
        this.location = location;
        this.version = version;
        this.status = status;
        this.componentStatuses = componentStatuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public List<ComponentStatus> getComponentStatuses() {
        return componentStatuses;
    }

    public void setComponentStatuses(List<ComponentStatus> componentStatuses) {
        this.componentStatuses = componentStatuses;
    }

    public void addComponentStatuses(ComponentStatus componentStatus) {
        if (componentStatuses == null) {
            componentStatuses = new ArrayList<ComponentStatus>();
        }
        componentStatuses.add(componentStatus);
    }
}
