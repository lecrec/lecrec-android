package com.lecrec.lecrec.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Record implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("user")
    private User user;
    @JsonProperty("title")
    private String title;
    @JsonProperty("filename")
    private String filename;
    @JsonProperty("text")
    private String text;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("created")
    private String created;
    @JsonProperty("is_uploaded")
    private Boolean isUploaded;
    @JsonProperty("is_converted")
    private Boolean isConverted;

    public Record(String id, String title, String filename, String text, String duration, String updated, String created, Boolean isUploaded, Boolean isConverted, User user) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.text = text;
        this.duration = duration;
        this.updated = updated;
        this.created = created;
        this.isUploaded = isUploaded;
        this.isConverted = isConverted;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getUploaded() {
        return isUploaded;
    }

    public void setUploaded(Boolean uploaded) {
        isUploaded = uploaded;
    }

    public Boolean getConverted() {
        return isConverted;
    }

    public void setConverted(Boolean converted) {
        isConverted = converted;
    }
}
