package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FireStoreDatabase {
    Firestore db;
    EmployeeRecordsFrame employeeUI;

    public FireStoreDatabase(EmployeeRecordsFrame employeeRecordsFrame) {
        db = null;
        this.employeeUI = employeeRecordsFrame;
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/java/org/example/payroll-cle-firebase-adminsdk-fbsvc-f2a423f709.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://payroll-cle-default-rtdb.asia-southeast1.firebasedatabase.app/").build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Employee> getAllEmployeeRecords(){
        try{
            ApiFuture<QuerySnapshot> query = db.collection("employees").get();
            List<QueryDocumentSnapshot> documentSnapshots = query.get().getDocuments();
            employeeUI.Model.employees.clear();
            for(QueryDocumentSnapshot document : documentSnapshots){
               Employee employee = document.toObject(Employee.class);
               if(employee != null){
                   employeeUI.Model.addEmployee(employee);
               }else{
                   System.err.println("Warning: Document " + document.getId() + " could not be converted to Employee object. It might be malformed.");
               }

            }

            employeeUI.Model.fireTableDataChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public CompletableFuture<Void> addEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        ApiFuture<WriteResult> result = db.collection("employees").document(employee.getEmployeeID()).set(employee);

        result.addListener(() -> {
            try {
                result.get();
                future.complete(null);
            } catch (Exception e) {
                System.err.println("Error adding employee " + employee.getFullName() + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, future.defaultExecutor()); // Use your executorService
        return future;
    }

}
