package com.daas.securityxref.util;
import org.json.simple.JSONObject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.*;

public final class Util {

    public static String getValue(JSONObject jso, String key) {
        try {
            String value = (String) jso.get(key);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUTCDate()
    {
        OffsetDateTime now = OffsetDateTime.now( ZoneOffset.UTC );
        return now.toString();
    }

    public static void writeLog(String value){

        java.time.Instant logDt = Instant.now();

        System.out.println(logDt.toString() + " " + "INFO --- [ " + value + "]");
    }
}
