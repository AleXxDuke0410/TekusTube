package co.tekus.tekustube.tekustube.models;

/**
 * Created by wrmej on 27/01/2018.
 */

public class Video {
    private String nameVideo;
    private String dir;
    private String duration;

    // TODO:CONSTRUCTOR
    public Video(String nameVideo, String dir, String duration) {
        this.nameVideo = nameVideo;
        this.dir = dir;
        this.duration = duration;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
