//package org.example;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.firestore.Firestore;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.cloud.FirestoreClient;
//
//import java.io.FileInputStream;
//
//public class FireStoreDatabase {
//    Firestore db;
//
//    public FireStoreDatabase() {
//        db = null;
//        try {
//            FileInputStream serviceAccount =
//                    new FileInputStream("src/main/java/org/example/payroll-cle-firebase-adminsdk-fbsvc-f2a423f709.json");
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://payroll-cle-default-rtdb.asia-southeast1.firebasedatabase.app/").build();
//            FirebaseApp.initializeApp(options);
//            db = FirestoreClient.getFirestore();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
