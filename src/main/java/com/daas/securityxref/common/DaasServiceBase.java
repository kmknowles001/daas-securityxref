package com.daas.securityxref.common;

public class DaasServiceBase {
    public ServiceInfo serviceInfo;
    public DaasServiceBase(String id, String serviceName, String serviceVersion){
        serviceInfo = new ServiceInfo(id,serviceName,serviceVersion);
    }
    public ServiceInfo getServiceInfo(){
        return serviceInfo;
    }
}
