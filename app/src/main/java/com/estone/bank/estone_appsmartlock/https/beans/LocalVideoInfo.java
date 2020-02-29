package com.estone.bank.estone_appsmartlock.https.beans;

public class LocalVideoInfo {

    private String name;
 private String path;
 private long duration;
 private long size;
 private String setThumbPath;

    public String getSetThumbPath() {
        return setThumbPath;
    }

    public void setSetThumbPath(String setThumbPath) {
        this.setThumbPath = setThumbPath;
    }

    public LocalVideoInfo(String name, String path, long duration, long size) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.size = size;
    }

    public LocalVideoInfo() {

    }

    @Override
    public String toString() {
        return "LocalVideoInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
