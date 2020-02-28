package me.stst.weatherstation.domain;

import javax.persistence.*;

@Entity
@Table(name = "api_tokens")
public class ApiToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "at_id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "d_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "at_user")
    private User user;

    @Column(name = "at_token")
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User creator) {
        this.user = creator;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
