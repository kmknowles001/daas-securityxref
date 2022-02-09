package com.daas.securityxref.repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;

public class BaseRepository {

    private final String PROJECT_ID = "data-services-337013";
    private Firestore ds;

    public BaseRepository(){
        connect();
    }

    public boolean connect(){
        try {
            FirestoreOptions firestoreOptions =
                    FirestoreOptions.getDefaultInstance().toBuilder()
                            .setProjectId(PROJECT_ID)
                            .setCredentials(GoogleCredentials.getApplicationDefault())
                            .build();
            ds = firestoreOptions.getService();
            return true;
        }
        catch (IOException ioe) {
            System.out.println(ioe);
            return false;
        }
        catch(Error e){
            System.out.println(e);
            return false;
        }
    }

    public Firestore getRepo() {
        return ds;
    }
}
