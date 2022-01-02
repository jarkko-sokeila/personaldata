package com.sokeila.personaldata.model;

public class Online {
    private String username;
    private String password;
    private String passwordHashMd5;
    private String passwordHashSha1;
    private String ipAddress;
    private String lastLoginIpAddress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHashMd5() {
        return passwordHashMd5;
    }

    public void setPasswordHashMd5(String passwordHashMd5) {
        this.passwordHashMd5 = passwordHashMd5;
    }

    public String getPasswordHashSha1() {
        return passwordHashSha1;
    }

    public void setPasswordHashSha1(String passwordHashSha1) {
        this.passwordHashSha1 = passwordHashSha1;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLastLoginIpAddress() {
        return lastLoginIpAddress;
    }

    public void setLastLoginIpAddress(String lastLoginIpAddress) {
        this.lastLoginIpAddress = lastLoginIpAddress;
    }
}
