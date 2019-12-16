package com.spring.demo.entities;

public class RestInfo {

    private String URI;
    private String query;
    private String ip;
    private String time;
    private String type;
    private String contentType;
    private int statusCode;

    public RestInfo(String URI, String query, String ip, String time, String type, String contentType, int statusCode) {
        this.URI = URI;
        this.query = query;
        this.ip = ip;
        this.time = time;
        this.type = type;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public RestInfo(){
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
