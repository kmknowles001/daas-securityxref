package com.daas.securityxref.common;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import java.util.UUID;

//
// standard daas service class.
//

public class ServiceInfo {

    @Getter @Setter private String serviceId;
    @Getter @Setter private String serviceName;
    @Getter @Setter private String serviceInstanceId;
    @Getter @Setter private String serviceVersion;
    @Getter @Setter private String serviceInstanceCreatedDt;
    @Getter @Setter private int numberOfServiceCalls;

    public ServiceInfo(String id, String serviceName, String serviceVersion) {

        this.serviceId = id;
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;

        // standard attributes to be created on instance.
        this.serviceInstanceId = UUID.randomUUID().toString();
        this.serviceInstanceCreatedDt = java.time.Instant.now().toString();
    }

    // return serviceInfo as JSON.
    public String getJSON() {
        JSONObject jsServiceInfo = new JSONObject();
        jsServiceInfo.put("serviceId", this.serviceId);
        jsServiceInfo.put("serviceName", this.serviceName);
        jsServiceInfo.put("serviceVersion", this.serviceVersion);
        jsServiceInfo.put("serviceInstanceId", this.serviceInstanceId);
        jsServiceInfo.put("serviceInstanceCreatedDt", this.serviceInstanceCreatedDt);
        jsServiceInfo.put("numberOfServiceCalls", Integer.toString(this.numberOfServiceCalls));
        return jsServiceInfo.toJSONString();
    }

    public int setNoOfServiceCalls(int numberOfServiceCalls) {
        this.numberOfServiceCalls = numberOfServiceCalls;
        return this.numberOfServiceCalls;
    }

    public int increamentNoOfCalls() {
        return this.numberOfServiceCalls++;
    }
}