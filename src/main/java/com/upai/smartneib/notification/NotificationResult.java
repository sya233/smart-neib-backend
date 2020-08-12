package com.upai.smartneib.notification;

import java.util.List;

public class NotificationResult {

    private String result;
    private List<Notification> notification;

    public NotificationResult(String result, List<Notification> notification) {
        this.result = result;
        this.notification = notification;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }
}
