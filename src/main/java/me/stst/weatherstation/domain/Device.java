package me.stst.weatherstation.domain;

import javax.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_id", nullable = false)
    private int id;

    @Column(name = "d_name", nullable = false)
    private String name;

    public Device() {
    }

    public Device(String name) {
        this.name=name;
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
}
