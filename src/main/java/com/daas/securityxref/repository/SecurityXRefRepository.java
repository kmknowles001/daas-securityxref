package com.daas.securityxref.repository;

import com.daas.securityxref.common.SecurityXRef;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;
import java.util.List;

public class SecurityXRefRepository extends BaseRepository {

    private static final String collectionName = "security-xref";
    private JSONObject js;
    public SecurityXRefRepository() {}

    //
    // add a new security xref records
    //
    public ApiFuture save(SecurityXRef secXRef)
    {
        ApiFuture<DocumentReference> newSecXRefRecord = getRepo().collection(collectionName).add(secXRef.ToJSONString());
        return newSecXRefRecord;
    }

    //
    // get all security xref records
    //
    public List<SecurityXRef> getAll(){
        try{
            ApiFuture<QuerySnapshot> query = getRepo().collection(collectionName).get();
            List<SecurityXRef> allRecords = new ArrayList();
            SecurityXRef xref = null;

            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> docs = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : docs){
                allRecords.add(document.toObject(SecurityXRef.class));
            }
            return allRecords;
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    //
    // get a xref record by a single field name and value
    //
    public SecurityXRef getById(String fieldName, String id) {
        try
        {
            CollectionReference col = getRepo().collection(collectionName);

            Query whereClause = col.whereEqualTo(fieldName,id);
            ApiFuture<QuerySnapshot> qry = whereClause.get();
            QuerySnapshot qrySnapshot = qry.get();

            List<QueryDocumentSnapshot> secXref = qrySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : secXref){
                return document.toObject(SecurityXRef.class);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    //
    // get a xref record by a single field name and value
    //
    public SecurityXRef getById(String qryXRefObject) {
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject js = (JSONObject) parser.parse(qryXRefObject);
            CollectionReference col = getRepo().collection(collectionName);
            Query whereClause = col;

            Iterator iterator = js.keySet().iterator();
            while (iterator.hasNext()){
                String fieldName = (String)iterator.next();
                String value = (String)js.get(fieldName);
                whereClause = whereClause.whereEqualTo( fieldName, value);
            }

            ApiFuture<QuerySnapshot> qry = whereClause.get();
            QuerySnapshot qrySnapshot = qry.get();
            List<QueryDocumentSnapshot> secXref = qrySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : secXref){
                return document.toObject(SecurityXRef.class);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}