package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AttendanceFrame extends JFrame implements ActionListener {
        JLabel time, employeeID;
        JTextField time_FL, employeeID_FL;
        JButton time_in, time_out;
        JTextArea logsArea;
        JPanel checkerPanel;
        JComboBox<String> dateList;

        Map<String,String> monthlyLogs;
        BorderLayout layout;
        Container container;

    public AttendanceFrame() throws HeadlessException {
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
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == time_in){
            addLog((String)dateList.getSelectedItem(),String.format("Employee [%s]: has clocked-in at %s", this.employeeID_FL.getText(),this.time_FL.getText()));
            this.employeeID_FL.setText("");
            this.time_FL.setText("");
        } else if (e.getSource() == time_out) {
            addLog((String)dateList.getSelectedItem(),String.format("Employee [%s]: has clocked-out at %s", this.employeeID_FL.getText(),this.time_FL.getText()));
            this.employeeID_FL.setText("");
            this.time_FL.setText("");
        }
    }

    public static void main(String[] args) {
        AttendanceFrame frame = new AttendanceFrame();

    }
}
