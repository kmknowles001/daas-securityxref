package com.daas.securityxref.controller;

//import com.daas.securityxref.entity.SecurityXRef;
import com.daas.securityxref.service.SecurityXRefService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/securityxref")
public class SecurityXRefController {

    private static SecurityXRefService svcSecXRef;
    private String svcId ="683527d0-76d3-493f-a2ed-97a2a47f079a";
    private String svcName="com.daas.securityxref";
    private String svcVersion = "1.1.0";

    public SecurityXRefController (){
        svcSecXRef = new SecurityXRefService(svcId,svcName,svcVersion);
    }

    @GetMapping("/getServiceInfo") @CrossOrigin()
    public String getServiceInfo(){
        return svcSecXRef.getServiceInfo().getJSON();
    }

    @PostMapping("/getSecurityXref") @CrossOrigin()
    public String getSecurityXRef(@RequestBody String req){
        return svcSecXRef.getSecurityXRef(req);
    }

    @PostMapping("/requestNewSecurityXref") @CrossOrigin()
    public String requestNewSecurityXref(@RequestBody String req){
        return svcSecXRef.RequestSecurityXRef(req);
    }


    @PostMapping("/checkMatchStrength") @CrossOrigin()
    public String checkMatchStrength(@RequestBody String req){
        return svcSecXRef.CheckMatchStrength(req);
    }
}
