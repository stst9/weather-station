package me.stst.weatherstation.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RestAuthUser {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
