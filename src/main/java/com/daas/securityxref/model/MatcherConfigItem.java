package com.daas.securityxref.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;


public class MatcherConfigItem {

    @Setter @Getter private String code;
    @Setter @Getter private String category;
    @Setter @Getter private String type;
    @Setter @Getter private String maxLength;
    @Setter @Getter private String minLength;
    @Setter @Getter private String matchValue = "0";
    @Setter @Getter private String validValues;
    @Setter @Getter private String includeInMatchKey;


    public MatcherConfigItem(){}

    public MatcherConfigItem(JSONObject jso){
        code = getValue(jso,"code");
        category = getValue(jso,"category");
        type = getValue(jso,"type");
        maxLength = getValue(jso,"maxLength");
        minLength = getValue(jso,"minLength");
        matchValue = getValue(jso,"matchValue");
        validValues = getValue(jso,"validValues");
        includeInMatchKey = getValue(jso,"includeInMatchKey");
    }

    private String getValue(JSONObject jso, String key){
        try{
            String value = (String)jso.get(key);
            return value;
        }catch (Exception e){
            return null;
        }
    }
}
