package org.example.api;

public class CourierJSON {

    private String login;
    private String password;
    private String firstName;

    public CourierJSON(String login, String password, String firstName) {
        this.firstName = firstName;
        this.password  = password;
        this.login     = login;
    }

    public CourierJSON(String login, String password) {
        this.password = password;
        this.login    = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
