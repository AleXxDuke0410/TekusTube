package co.tekus.tekustube.tekustube.models;

/**
 * Created by wrmej on 27/01/2018.
 */

public class Notification {
    private int notificationId;
    private String date;
    private int duration;

    // TODO:CONSTRUCTOR
    public Notification(int notificationId, String date, int duration) {
        this.notificationId = notificationId;
        this.date = date;
        this.duration = duration;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
