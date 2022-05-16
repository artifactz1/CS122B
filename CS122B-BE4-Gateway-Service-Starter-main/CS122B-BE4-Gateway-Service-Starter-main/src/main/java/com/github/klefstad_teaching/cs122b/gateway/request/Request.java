package com.github.klefstad_teaching.cs122b.gateway.request;

import org.springframework.http.server.RequestPath;

public class Request {

    public String ip_address;
    public String call_time;
    public String path;

    public String getIp_address() {
        return ip_address;
    }

    public Request setIp_address(String ip_address) {
        this.ip_address = ip_address;
        return this;
    }

    public String getCall_time() {
        return call_time;
    }

    public Request setCall_time(String call_time) {
        this.call_time = call_time;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Request setPath(String requestPath) {
        this.path = requestPath;
        return this;
    }
}