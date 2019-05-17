package model;

import java.util.Date;

public class User {

    private String name;
    private String surname;
    private String email;
    private String id;
    private Membership membership;

    public User() {}

    public User(String name, String surname, String email, String id, Date init, Date end, double value) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.id = id;
        membership = new Membership(init,end,value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
