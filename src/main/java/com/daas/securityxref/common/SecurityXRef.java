package com.daas.securityxref.common;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class SecurityXRef {


    public static final String TYPE_TAG = "type";
    public static final String SECURITY_ISSUE_TAG = "issue";
    public static final String SECURITY_CCY_TAG = "ccy";
    public static final String EXCHANGE_TAG = "exchange";

    public static final String UID_TAG = "uid";
    public static final String ISIN_CODE_TAG = "isinCode";
    public static final String SEDOL_CODE_TAG = "sedolCode";
    public static final String CUSIP_CODE_TAG = "cusipCode";
    public static final String BCUSIP_CODE_TAG = "bcusipCode";
    public static final String VALOREN_CODE_TAG = "valorenCode";
    public static final String RIC_CODE_TAG = "ricCode";
    public static final String BLOOMBERG_TICKER_CODE_TAG = "bbTicker";

    public static final String MATCH_ITEMS_TAG = "matchItems";
    public static final String MATCH_KEY_TAG = "matchKey";
    public static final String MATCH_STRENGTH_TAG = "matchStrength";

    @Getter private static final String[] qualifierTagArray = {SECURITY_ISSUE_TAG,TYPE_TAG,SECURITY_CCY_TAG,EXCHANGE_TAG};
    @Getter private static final String[] identifierTagArray = {UID_TAG,ISIN_CODE_TAG,SEDOL_CODE_TAG,CUSIP_CODE_TAG,BCUSIP_CODE_TAG,VALOREN_CODE_TAG,RIC_CODE_TAG,BLOOMBERG_TICKER_CODE_TAG};

    @Getter @Setter private String asOfDate;
    @Getter @Setter private String securityId;
    @Getter @Setter private String uid;
    @Getter @Setter private String type;
    @Getter @Setter private String ccy;
    @Getter @Setter private String issueName;
    @Getter @Setter private String exchange;
    @Getter @Setter private String isinCode;
    @Getter @Setter private String sedolCode;
    @Getter @Setter private String valorenCode;
    @Getter @Setter private String bloombergTicker;
    @Getter @Setter private String cusipCode;
    @Getter @Setter private String bcusipCode;
    @Getter @Setter private String ricCode;
    @Getter @Setter private String matchItems;
    @Getter @Setter private String matchKey;
    @Getter @Setter private String matchStrength;

    public SecurityXRef(){}

    public SecurityXRef(JSONObject jso){

        // qualifiers
        this.type = (String) jso.get(TYPE_TAG);
        this.issueName = (String) jso.get(SECURITY_ISSUE_TAG);
        this.ccy =(String)jso.get(SECURITY_CCY_TAG);
        this.exchange = (String) jso.get(EXCHANGE_TAG);

        // ids
        this.uid = (String)jso.get(UID_TAG);
        this.isinCode = (String) jso.get(ISIN_CODE_TAG);
        this.sedolCode = (String) jso.get(SEDOL_CODE_TAG);
        this.cusipCode = (String) jso.get(CUSIP_CODE_TAG);
        this.bcusipCode = (String) jso.get(BCUSIP_CODE_TAG);
        this.valorenCode = (String) jso.get(VALOREN_CODE_TAG);
        this.ricCode = (String) jso.get(RIC_CODE_TAG);
        this.bloombergTicker = (String) jso.get(BLOOMBERG_TICKER_CODE_TAG);

        // match criteria
        this.matchItems = (String)jso.get(MATCH_ITEMS_TAG);
        this.matchKey = (String)jso.get(MATCH_KEY_TAG);
        this.matchStrength = (String)jso.get(MATCH_STRENGTH_TAG);
    }

    public String ToJSONString()
    {
        JSONObject obj = new JSONObject();
        // qualifiers
        if (this.type != null) obj.put(TYPE_TAG,this.type);
        if (this.issueName != null) obj.put(SECURITY_ISSUE_TAG,this.issueName);
        if (this.ccy != null) obj.put(SECURITY_CCY_TAG, this.ccy);
        if (this.exchange != null) obj.put(EXCHANGE_TAG,this.exchange);

        // ids
        if (this.uid != null) obj.put(UID_TAG,this.uid);
        if (this.isinCode != null) obj.put(ISIN_CODE_TAG,this.isinCode);
        if (this.sedolCode != null) obj.put(SEDOL_CODE_TAG,this.sedolCode);
        if (this.cusipCode != null) obj.put(CUSIP_CODE_TAG,this.cusipCode);
        if (this.bcusipCode != null) obj.put(BCUSIP_CODE_TAG,this.bcusipCode);
        if (this.valorenCode != null) obj.put(VALOREN_CODE_TAG,this.valorenCode);
        if (this.ricCode != null) obj.put(RIC_CODE_TAG,this.ricCode);
        if (this.bloombergTicker != null) obj.put(BLOOMBERG_TICKER_CODE_TAG,this.bloombergTicker);

        // match criteria
        if (this.matchItems != null) obj.put(MATCH_ITEMS_TAG,this.matchItems);
        if (this.matchKey != null) obj.put(MATCH_KEY_TAG,this.matchKey);
        if (this.matchStrength != null) obj.put(MATCH_STRENGTH_TAG,this.matchStrength);
        return obj.toJSONString();
    }
}
