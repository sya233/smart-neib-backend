package com.upai.smartneib.repair;

public class RepairResult {

    private String user;
    private String name;
    private String result;

    public RepairResult(String user, String name, String result) {
        this.user = user;
        this.name = name;
        this.result = result;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
