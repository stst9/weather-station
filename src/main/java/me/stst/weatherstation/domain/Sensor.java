package me.stst.weatherstation.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_id",nullable = false)
    private int id;

    @Column(name = "s_name",nullable = false)
    private String name;

    @Column(name = "s_model")
    private String model;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "d_id", nullable = false)
    private Device device;

    @OneToMany(mappedBy = "sensor")
    private List<SensorValue> sensorValues=new ArrayList<>();

    public Sensor() {
    }


    public Sensor(String name,String model,Device device) {
        this.name=name;
        this.model=model;
        this.device=device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<SensorValue> getSensorValues() {
        return sensorValues;
    }
}
