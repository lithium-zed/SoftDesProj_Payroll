package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceFrame extends JFrame implements ActionListener {
        JLabel time, employeeID;
        JTextField time_FL, employeeID_FL;
        JButton time_in, time_out;
        JTextArea logsArea;
        JPanel checkerPanel;
        JComboBox<String> dateList;
        EmployeeAbstractTableModel employeeList;
        Map<String,String> monthlyLogs;
        BorderLayout layout;
        Container container;
        private FireStoreDatabase db;

    public AttendanceFrame(EmployeeAbstractTableModel employees) throws HeadlessException {
        employeeList = employees;
        this.db = FireStoreDatabase.getInstance();

        container = this.getContentPane();
        layout = new BorderLayout();
        container.setLayout(layout);

        employeeID = new JLabel("Employee ID:");
        employeeID_FL = new JTextField(10);
        JPanel employeePanel = new JPanel(new FlowLayout());
        employeePanel.add(employeeID);
        employeePanel.add(employeeID_FL);
        container.add(employeePanel, BorderLayout.NORTH);


        time = new JLabel("Time:");
        time_FL = new JTextField(10);
        time_in = new JButton("Time-In");
        time_out = new JButton("Time-Out");
        time_in.addActionListener(this);
        time_out.addActionListener(this);

        monthlyLogs = new HashMap<>();
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            String initialLog = "Logs for " + month + ":\n";
            monthlyLogs.put(month, initialLog);
            loadMonthlyLogFromFirestore(month);
        }
        dateList = new JComboBox<>(months);

        checkerPanel = new JPanel(new GridBagLayout());
        addToCheckerPanel(time,0,0,1);
        addToCheckerPanel(time_FL,1,0,1);
        addToCheckerPanel(dateList,2,0,1);
        addToCheckerPanel(time_in,0,1,1);
        addToCheckerPanel(time_out,1,1,1);
        container.add(checkerPanel,BorderLayout.CENTER);

        logsArea = new JTextArea(10, 30);
        logsArea.setEditable(false);
        JScrollPane logsScrollPane = new JScrollPane(logsArea);
        container.add(logsScrollPane, BorderLayout.SOUTH);

        dateList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMonth = (String) dateList.getSelectedItem();
                if (selectedMonth != null && monthlyLogs.containsKey(selectedMonth)) {
                    logsArea.setText(monthlyLogs.get(selectedMonth));
                    logsArea.setCaretPosition(0);
                } else {
                    logsArea.setText("");
                }
            }
        });

        if (months.length > 0) {
            int currentMonthIndex = YearMonth.now().getMonthValue() - 1;
            if (currentMonthIndex >= 0 && currentMonthIndex < months.length) {
                dateList.setSelectedIndex(currentMonthIndex);
            } else {
                dateList.setSelectedIndex(0);
            }
        }

        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("Attendance Checker");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveAllMonthlyLogsToFirestore();
            }
        });
    }
    public void addToCheckerPanel(Component component, int gridx, int gridy, int width){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = width;
        checkerPanel.add(component,gbc);
    }
    public void addLog(String month, String logMessage) {
        if (monthlyLogs.containsKey(month)) {
            String currentLog = monthlyLogs.get(month);
            monthlyLogs.put(month, currentLog + logMessage + "\n");

            if (dateList.getSelectedItem().equals(month)) {
                logsArea.setText(monthlyLogs.get(month));
                logsArea.setCaretPosition(logsArea.getText().length());
            }
        }
    }
    private void loadMonthlyLogFromFirestore(String month) {
        db.getMonthlyLog(month).thenAccept(logContentFromDb -> {
            SwingUtilities.invokeLater(() -> {
                String currentLogContent = "";

                if (logContentFromDb != null) {
                    String initialHeader = "Logs for " + month + ":\n"; // Changed log format here

                    if (!logContentFromDb.startsWith(initialHeader)) {
                        currentLogContent = initialHeader + logContentFromDb;
                    } else {
                        currentLogContent = logContentFromDb;
                    }
                } else {

                    currentLogContent = "Logs for " + month + ":\n";
                }

                monthlyLogs.put(month, currentLogContent);

                if (dateList.getSelectedItem().equals(month)) {
                    logsArea.setText(currentLogContent);
                    logsArea.setCaretPosition(0);
                }
            });
        }).exceptionally(e -> {
            SwingUtilities.invokeLater(() -> {
                System.err.println("Failed to load log for " + month + ": " + e.getMessage());
            });
            return null;
        });
    }
    private void saveAllMonthlyLogsToFirestore() {
        for (Map.Entry<String, String> entry : monthlyLogs.entrySet()) {
            String month = entry.getKey();
            String logContent = entry.getValue();
            db.saveMonthlyLog(month, logContent).exceptionally(e -> {
                System.err.println("Failed to save log for " + month + " on shutdown: " + e.getMessage());
                return null;
            });
        }
        System.out.println("Initiated saving all monthly logs to Firestore.");
    }

    public double calculateTimeElapsed(String time_in, String time_out){
        LocalTime start = LocalTime.parse(time_in);
        LocalTime end = LocalTime.parse(time_out);

        long minutesLapsed = ChronoUnit.MINUTES.between(start,end);
        return (double) minutesLapsed / 60.0;
    }
    private LocalTime parseTime(String timeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(timeString, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid time format. Please use HH:mm (24-hour format).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String employeeIDInput = employeeID_FL.getText().trim();
        String timeInputText = time_FL.getText().trim();
        LocalTime parsedTime = parseTime(timeInputText);

        if (parsedTime == null) {
            return;
        }

        // Format the time for database storage (HH:mm:ss)
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTimeForDb = parsedTime.format(dbFormatter);

        if (e.getSource() == time_in) {
            db.getEmployee(employeeIDInput).thenAccept(employee -> {
                SwingUtilities.invokeLater(() -> {
                    if (employee != null) {
                        // Check if timeIn is already set AND timeOut is null (meaning they are currently clocked in)
                        if (employee.getTime_in() != null && employee.getTime_out() == null) {
                            JOptionPane.showMessageDialog(this, "Employee " + employeeIDInput + " is already clocked in.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        db.clockInEmployee(employeeIDInput, formattedTimeForDb).thenRun(() -> {
                            SwingUtilities.invokeLater(() -> {
                                addLog((String) dateList.getSelectedItem(), String.format("Employee [%s]: has clocked-in at %s", employeeIDInput, formattedTimeForDb));

                                Employee localEmployee = employeeList.getEmployeeByID(employeeIDInput);
                                if (localEmployee != null) {
                                    localEmployee.setTime_in(formattedTimeForDb);
                                    localEmployee.setTime_out(null); // Explicitly clear time out
                                    employeeList.fireTableDataChanged();
                                }
                                this.employeeID_FL.setText("");
                                this.time_FL.setText("");
                            });
                        }).exceptionally(ex -> {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Error clocking in employee: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                            });
                            return null;
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee with ID " + employeeIDInput + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).exceptionally(ex -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error checking employee existence: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
                return null;
            });
        } else if (e.getSource() == time_out) {
            db.getEmployee(employeeIDInput).thenAccept(employee -> {
                SwingUtilities.invokeLater(() -> {
                    if (employee != null) {
                        // Check if timeIn is not null AND timeOut is null (meaning they are currently clocked in and haven't clocked out yet)
                        if (employee.getTime_in() == null || employee.getTime_out() != null) {
                            JOptionPane.showMessageDialog(this, "Employee " + employeeIDInput + " has not timed in yet or is already clocked out.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        db.clockOutEmployee(employeeIDInput, formattedTimeForDb).thenAccept(totalHoursWorked -> {
                            SwingUtilities.invokeLater(() -> {
                                addLog((String) dateList.getSelectedItem(), String.format("Employee [%s]: has clocked-out at %s", employeeIDInput, formattedTimeForDb));

                                Employee localEmployee = employeeList.getEmployeeByID(employeeIDInput);
                                if (localEmployee != null) {
                                    localEmployee.setTime_out(formattedTimeForDb);
                                    localEmployee.setHoursWorked(totalHoursWorked);
                                    employeeList.fireTableDataChanged();
                                }
                                this.employeeID_FL.setText("");
                                this.time_FL.setText("");
                            });
                        }).exceptionally(ex -> {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Error clocking out employee: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                            });
                            return null;
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee with ID " + employeeIDInput + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).exceptionally(ex -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error checking employee existence: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
                return null;
            });
        }
    }

    public static void main(String[] args) {
        EmployeeRecordsFrame employeeRecordsFrame = new EmployeeRecordsFrame();
        AttendanceFrame frame = new AttendanceFrame(employeeRecordsFrame.Model);


    }
}
