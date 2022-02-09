package com.daas.securityxref.service;

import com.daas.securityxref.common.DaasServiceBase;
import com.daas.securityxref.common.SecurityXRef;
import com.daas.securityxref.model.MatcherConfigItem;
import com.daas.securityxref.model.MatcherConfigList;
import com.daas.securityxref.repository.SecurityXRefRepository;
import com.daas.securityxref.util.Util;
import com.daas.securityxref.util.PublisherUtility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

public class SecurityXRefService extends DaasServiceBase{

    private MatcherConfigList matcherConfig;

    // constructor
    public SecurityXRefService(String id, String serviceName, String serviceVersion){
        super(id, serviceName,serviceVersion);
        try{
            this.matcherConfig = loadMatcherConfig();
        }catch(Exception e){
            System.out.println("error occurred in securityxref.service start-up.");
        }
    }

    //
    // load matcher json config file
    //
    private MatcherConfigList loadMatcherConfig() throws Exception{

        final String key = "matcherConfig";
        final String path = "matcher-config.json";
//        final String path = ".\\src\\main\\resources\\matcher-config.json";

        MatcherConfigList loadList = new MatcherConfigList();
        String executionPath =  new File(".").getCanonicalPath().toString();

        System.out.println("current execution path:" + executionPath);
        System.out.println("checking for matcher config file:" + path);

        JSONParser parser = new JSONParser();
        JSONObject jsonFile = (JSONObject) parser.parse(new FileReader(path));
        JSONArray jsMatcherItemArray = (JSONArray) jsonFile.get(key);

        for (Object c : jsMatcherItemArray){
            System.out.println("loading config file. Processing:" + c.toString());
            loadList.add(new MatcherConfigItem((JSONObject)c));
        }
        return loadList;
    }


    //
    // find a securityxref record from the db based on rest request
    //
    public String getSecurityXRef(String req)
    {
        this.serviceInfo.increamentNoOfCalls();

        // get security from db
        SecurityXRefRepository repo = new SecurityXRefRepository();
        SecurityXRef xref = repo.getById(req);

        if (xref != null) {
            return xref.ToJSONString();
        }else{
            JSONObject noRecordFound = new JSONObject();
            noRecordFound.put("asofdate", Util.getUTCDate());
            noRecordFound.put("message","No record found.");
            return noRecordFound.toJSONString();
        }
    }

    //
    // check security match strength from rest request. This is used to evaluate input record i.e. (a) likelihood of a strong match or (b) creation of new record.
    //

    public String CheckMatchStrength(String js){

        JSONObject matchStrength;
        JSONObject rtn = new JSONObject();
        JSONArray rtnMatchStrength = new JSONArray();
        JSONParser parser = new JSONParser();

        this.serviceInfo.increamentNoOfCalls();

        rtn.put("serviceId", this.serviceInfo.getServiceId());
        rtn.put("serviceInstance", this.serviceInfo.getServiceInstanceId());
        rtn.put("serviceName", this.serviceInfo.getServiceName());
        rtn.put("matchEvaluation",rtnMatchStrength);

        try {
            // ensure we have a proper json request - expect array of requests.
            JSONArray requestJson = (JSONArray) parser.parse(js);

            // loop through array of security xref records to evaluate
            for(Object jsoSecurityXrefItem : requestJson){
                matchStrength = evaluateStrength((JSONObject) jsoSecurityXrefItem);
                rtnMatchStrength.add(matchStrength);
            }
        } catch (ParseException pe) {
            rtn.put("unhandledErrorIdentifierd", pe.toString());
        } catch (Exception e) {
            rtn.put("unhandledErrorIdentifierd", e.toString());
        } finally {
            return rtn.toJSONString();
        }
    }

    //
    // evaulate specific securityxref record and return match evaluation (result)
    //
    public JSONObject evaluateStrength(JSONObject request){

        MatcherConfigItem matcherConfigItem;
        int matchStrength = 0;
        int matchIdentifierStrength = 0;
        int matchQualifierStrength = 0;
        int counter = 1;
        String matchFields = "";
        String matchKey = "";

        Iterator<String> keys = request.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            String value = (String)request.get(key);

            matcherConfigItem = this.matcherConfig.get(key);
            if (matcherConfigItem != null) {
                if (matcherConfigItem.getCategory().equals("QUALIFIER")) {
                    matchQualifierStrength = matchQualifierStrength +  Integer.parseInt(matcherConfigItem.getMatchValue());
                }else{
                    matchIdentifierStrength = matchIdentifierStrength +  Integer.parseInt(matcherConfigItem.getMatchValue());
                }
                matchStrength = matchStrength + Integer.parseInt(matcherConfigItem.getMatchValue());
                if (matcherConfigItem.getIncludeInMatchKey().equals("Y")){
                    if (matchFields.length() > 1) {
                        matchFields = matchFields + "-" + key;
                        matchKey = matchKey + "-" + value;
                    }else {
                        matchFields = key;
                        matchKey = value;
                    }
                }
            }
            counter++;
        }
        JSONObject rtn  = new JSONObject();
        rtn.put("request",request);
        JSONObject jsoStrengthEvaluation = new JSONObject();
        jsoStrengthEvaluation.put("totalMatchStrength:", Integer.toString(matchStrength));
        jsoStrengthEvaluation.put("totalMatchQualifierStrength:", Integer.toString(matchQualifierStrength));
        jsoStrengthEvaluation.put("totalMatchIdentifierStrength:", Integer.toString(matchIdentifierStrength));
        jsoStrengthEvaluation.put("matchItems:", matchFields);
        jsoStrengthEvaluation.put("matchKey:", matchKey);
        jsoStrengthEvaluation.put("numberOfAttributeEvaluated:", Integer.toString(counter));
        rtn.put("matchEvaluationResponse",jsoStrengthEvaluation);
        return rtn;
    }

    //
    // request a new security xref.
    //
    public String RequestSecurityXRef(String js) {

        // pubsub configuration
        final String TOPIC_ID = "projects/data-services-337013/topics/security-xref-new-request";
        final String TOPIC_NAME = "projects/data-services-337013/topics/security-xref-new-request";
        final String PROJECT_ID = "data-services-337013";

        String returnMsg = "Non-initialised value";
        JSONObject rtn = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONArray arr = new JSONArray();

        try {
            // parse webservice request
            JSONArray jArray = (JSONArray) parser.parse(js);

            // loop through array
            for(int i = 0; i < jArray.size();i++){
                JSONObject wrkObj = (JSONObject)jArray.get(i);
                // publish message
                PublisherUtility.publishMessage(PROJECT_ID, TOPIC_ID, TOPIC_NAME, wrkObj.toJSONString());
                // add to array
                arr.add(wrkObj);
            }
        } catch (Exception e) {
            returnMsg = e.toString();
        } finally {
            rtn.put("requested",arr);
            rtn.put("asOfDate", Util.getUTCDate());
            rtn.put("message",returnMsg);
            return rtn.toJSONString();
        }
    }
}
