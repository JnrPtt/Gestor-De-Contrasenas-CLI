package org.example;

public class Entry {
    private String service;
    private String username;
    private String salt;
    private String iv;
    private String encryptedPassword;

    public Entry(String service, String username, String salt, String iv, String encryptedPassword) {
        this.service = service;
        this.username = username;
        this.salt = salt;
        this.iv = iv;
        this.encryptedPassword = encryptedPassword;
    }

    //Getters
    public String getService() {
        return service;
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getIv() {
        return iv;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    //Setters
    public void setService(String service) {
        this.service = service;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
