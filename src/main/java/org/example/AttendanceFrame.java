package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
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
        private Employee employee;

    public AttendanceFrame(EmployeeAbstractTableModel employees) throws HeadlessException {
        employeeList = employees;
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
            monthlyLogs.put(month, "Logs for " + month + ":\n"); // Initialize with a header
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
            dateList.setSelectedIndex(0); // Trigger the ActionListener for the first month
        }

        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("Attendance Checker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private boolean isExisting(ArrayList<Employee> employees, String ID){
        for (Employee employee : employees) {
            if (ID.equals(employee.getEmployeeID())) {
                setEmployee(employee);
                return true;
            }
        }
        return false;
    }

    private Employee getEmployee() {
        return employee;
    }

    private void setEmployee(Employee employee) {
        this.employee = employee;
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

        if (e.getSource() == time_in) {
            String employeeID = employeeID_FL.getText().trim();
            String timeInText = time_FL.getText().trim();
            LocalTime parsedTime = parseTime(timeInText);

            if (parsedTime != null) {
                if (isExisting(employeeList.employees, employeeID)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String formattedTime = parsedTime.format(formatter);
                    addLog((String) dateList.getSelectedItem(), String.format("Employee [%s]: has clocked-in at %s", employeeID, formattedTime));
                    getEmployee().setWorking(true);
                    getEmployee().setTime_in(formattedTime);
                } else {
                    JOptionPane.showMessageDialog(null, "Employee does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
                this.employeeID_FL.setText("");
                this.time_FL.setText("");
            }

        } else if (e.getSource() == time_out) {
            String employeeID = employeeID_FL.getText().trim();
            String timeOutText = time_FL.getText().trim();
            LocalTime parsedTime = parseTime(timeOutText);

            if (parsedTime != null) {
                if (isExisting(employeeList.employees, employeeID)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String formattedTime = parsedTime.format(formatter);
                    addLog((String) dateList.getSelectedItem(), String.format("Employee [%s]: has clocked-out at %s", employeeID, formattedTime));
                    getEmployee().setWorking(false);
                    getEmployee().setTime_out(formattedTime);

                    if(getEmployee().getHoursWorked() > 0){
                        double currentHours = getEmployee().getHoursWorked();
                        currentHours += calculateTimeElapsed(getEmployee().getTime_in(), getEmployee().getTime_out());
                        getEmployee().setHoursWorked(currentHours);
                    }else{
                        getEmployee().setHoursWorked(calculateTimeElapsed(getEmployee().getTime_in(), getEmployee().getTime_out()));
                    }

                    employeeList.fireTableDataChanged();
                } else {
                    JOptionPane.showMessageDialog(null, "Employee does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
                this.employeeID_FL.setText("");
                this.time_FL.setText("");
            }

        }
    }

    public static void main(String[] args) {
        EmployeeRecordsFrame employeeRecordsFrame = new EmployeeRecordsFrame();
        AttendanceFrame frame = new AttendanceFrame(employeeRecordsFrame.Model);


    }
}
