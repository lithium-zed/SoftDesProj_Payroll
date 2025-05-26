package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FireStoreDatabase {
    private Firestore db;
    private static FireStoreDatabase instance;

    private final ExecutorService executorService = Executors.newCachedThreadPool();


    public FireStoreDatabase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) { // Check if FirebaseApp is already initialized
                InputStream serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("payroll-cle-firebase-adminsdk-fbsvc-f2a423f709.json");

                if (serviceAccount == null) {
                    throw new IOException("Firebase service account key file not found. " +
                            "Ensure it's in src/main/resources or equivalent. " +
                            "Current working directory: " + System.getProperty("user.dir"));
                }

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://payroll-cle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("FirebaseApp initialized successfully for Firestore.");
            } else {
                System.out.println("FirebaseApp already initialized (via singleton).");
            }

            db = FirestoreClient.getFirestore();
            System.out.println("Firestore instance obtained successfully.");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load Firebase service account key. " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Firestore initialization failed due to service account key error.", e);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize FirebaseApp or get Firestore instance. " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Firestore initialization failed due to an unknown error.", e);
        }
    }
    public static synchronized FireStoreDatabase getInstance() {
        if (instance == null) {
            instance = new FireStoreDatabase(); // Initialize ONLY IF NULL
        }
        return instance;
    }

    public CompletableFuture<ArrayList<Employee>> getAllEmployeeRecords() {
        CompletableFuture<ArrayList<Employee>> future = new CompletableFuture<>();
        ApiFuture<QuerySnapshot> query = db.collection("employees").get();

        query.addListener(() -> {
            try {
                List<QueryDocumentSnapshot> documentSnapshots = query.get().getDocuments();
                ArrayList<Employee> employeeList = new ArrayList<>(); // Create list here
                for(QueryDocumentSnapshot document : documentSnapshots){
                    Employee employee = document.toObject(Employee.class);
                    if(employee != null){
                        employeeList.add(employee); // Add to local list
                    }else{
                        System.err.println("Warning: Document " + document.getId() + " could not be converted to Employee object. It might be malformed.");
                    }
                }
                future.complete(employeeList); // Complete the future with the list
            }catch (Exception e){
                System.err.println("Error fetching all employee records: " + e.getMessage());
                e.printStackTrace();
                future.completeExceptionally(e); // Complete with error
            }
        }, executorService); // Use the executorService for the listener
        return future; // Return the future immediately
    }

    public CompletableFuture<Boolean> employeeExists(String employeeID) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        ApiFuture<DocumentSnapshot> documentFuture = db.collection("employees").document(employeeID).get();

        documentFuture.addListener(() -> {
            try {
                DocumentSnapshot document = documentFuture.get();
                future.complete(document.exists()); // True if document exists, false otherwise
            } catch (Exception e) {
                System.err.println("Error checking employee existence for ID " + employeeID + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, executorService); // Use the executorService
        return future;
    }
    public CompletableFuture<Void> updateEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        // Use set() with merge true to update specific fields without overwriting the entire document
        ApiFuture<WriteResult> result = db.collection("employees").document(employee.getEmployeeID()).set(employee, SetOptions.merge());

        result.addListener(() -> {
            try {
                result.get();
                System.out.println("Employee " + employee.getFullName() + " updated successfully.");
                future.complete(null);
            } catch (Exception e) {
                System.err.println("Error updating employee " + employee.getFullName() + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, executorService);
        return future;
    }
    public CompletableFuture<Void> deleteEmployee(String employeeID) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ApiFuture<WriteResult> result = db.collection("employees").document(employeeID).delete();

        result.addListener(() -> {
            try {
                result.get();
                System.out.println("Employee with ID " + employeeID + " deleted successfully.");
                future.complete(null);
            } catch (Exception e) {
                System.err.println("Error deleting employee with ID " + employeeID + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, executorService);
        return future;
    }
    public CompletableFuture<Employee> getEmployee(String employeeID) {
        CompletableFuture<Employee> future = new CompletableFuture<>();
        ApiFuture<DocumentSnapshot> documentFuture = db.collection("employees").document(employeeID).get();

        documentFuture.addListener(() -> {
            try {
                DocumentSnapshot document = documentFuture.get();
                if (document.exists()) {
                    Employee employee = document.toObject(Employee.class);
                    future.complete(employee);
                } else {
                    future.complete(null);
                }
            } catch (Exception e) {
                System.err.println("Error retrieving employee " + employeeID + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, executorService);
        return future;
    }
    public CompletableFuture<Void> clockInEmployee(String employeeID, String timeIn) { // Added timeIn parameter
        CompletableFuture<Void> future = new CompletableFuture<>();
        Map<String, Object> updates = new HashMap<>();
        updates.put("time_in", timeIn);
        updates.put("time_out", null);

        ApiFuture<WriteResult> result = db.collection("employees").document(employeeID).update(updates);
        result.addListener(() -> {
            try {
                result.get();
                System.out.println("Employee " + employeeID + " clocked in successfully at " + timeIn + ".");
                future.complete(null);
            } catch (Exception e) {
                System.err.println("Error clocking in employee " + employeeID + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, executorService);
        return future;
    }
    public CompletableFuture<Double> clockOutEmployee(String employeeID, String timeOut) {
        CompletableFuture<Double> future = new CompletableFuture<>();

        getEmployee(employeeID).thenAccept(employee -> {
            if (employee == null || employee.getTime_in() == null || employee.getTime_out() != null) {
                future.completeExceptionally(new IllegalStateException("Employee not found or not currently clocked in."));
                return;
            }


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime timeInParsed = LocalTime.parse(employee.getTime_in(), formatter);
            LocalTime timeOutParsed = LocalTime.parse(timeOut, formatter);

            double hoursWorkedThisSession = (double) ChronoUnit.MINUTES.between(timeInParsed, timeOutParsed) / 60.0;

            Map<String, Object> updates = new HashMap<>();
            updates.put("time_out", timeOut);


            double currentCumulativeHours = employee.getHoursWorked();
            double newCumulativeHours = currentCumulativeHours + hoursWorkedThisSession;

            updates.put("hoursWorked", newCumulativeHours);

            ApiFuture<WriteResult> result = db.collection("employees").document(employeeID).update(updates);
            result.addListener(() -> {
                try {
                    result.get();
                    System.out.println("Employee " + employeeID + " clocked out successfully at " + timeOut + ". Hours added this session: " + hoursWorkedThisSession + ". New total: " + newCumulativeHours);
                    future.complete(newCumulativeHours); // Complete with the new total
                } catch (Exception e) {
                    System.err.println("Error clocking out employee " + employeeID + ": " + e.getMessage());
                    future.completeExceptionally(e);
                }
            }, executorService);
        }).exceptionally(e -> {
            future.completeExceptionally(new RuntimeException("Failed to get employee data for clock out: " + e.getMessage(), e));
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> addEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ApiFuture<WriteResult> result = db.collection("employees").document(employee.getEmployeeID()).set(employee);
        result.addListener(() -> {
            try {
                result.get();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, executorService);
        return future;
    }

    public CompletableFuture<Void> saveMonthlyLog(String monthName, String logContent) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        // Create a unique document ID for the month, e.g., "2025_May"
        String documentId = YearMonth.now().getYear() + "_" + monthName;

        Map<String, Object> data = new HashMap<>();
        data.put("logs", logContent);
        data.put("lastUpdated", FieldValue.serverTimestamp()); // Good practice to track when it was last updated

        // Use set() to create or overwrite the document for that month.
        // set() with a document ID acts like an upsert (update if exists, create if not).
        ApiFuture<WriteResult> result = db.collection("monthlyAttendanceLogs").document(documentId).set(data);

        result.addListener(() -> {
            try {
                result.get();
                System.out.println("Monthly log for " + monthName + " saved/updated successfully.");
                future.complete(null);
            } catch (Exception e) {
                System.err.println("Error saving monthly log for " + monthName + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, future.defaultExecutor());
        return future;
    }

    public CompletableFuture<String> getMonthlyLog(String monthName) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String documentId = YearMonth.now().getYear() + "_" + monthName;

        ApiFuture<DocumentSnapshot> docRefFuture = db.collection("monthlyAttendanceLogs").document(documentId).get();

        docRefFuture.addListener(() -> {
            try {
                if (docRefFuture.get().exists()) {
                    String logs = docRefFuture.get().getString("logs");
                    future.complete(logs);
                } else {
                    future.complete(null); // Log for this month not found
                }
            } catch (Exception e) {
                System.err.println("Error retrieving monthly log for " + monthName + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        }, future.defaultExecutor());
        return future;
    }

}
