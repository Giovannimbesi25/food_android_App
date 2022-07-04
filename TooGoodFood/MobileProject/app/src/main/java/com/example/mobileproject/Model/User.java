//Class used to store User's information as a child of "users" firebase's root

package com.example.mobileproject.Model;

public class User
{
    private String Email;
    private String Password;
    private String Username;
    private String Address;
    private Boolean Boss;

    public User() {
    }

    public User(String email, String password, String username, String address) {
        Email = email;
        Password = password;
        Username = username;
        Address = address;
    }

    public User(String email, String password, String username, String address, Boolean boss) {
        Email = email;
        Password = password;
        Username = username;
        Address = address;
        Boss = boss;
    }

    public Boolean getBoss() {
        return Boss;
    }

    public void setBoss(Boolean boss) {
        Boss = boss;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() { return Username;}

    public void setUsername(String username) { Username = username;}
}
