package org.example;

import javax.swing.*;
import java.awt.*;

public class YearEndFrame extends JFrame {
    public YearEndFrame(EmployeeAbstractTableModel model) {
        setTitle("Year-End Summary");
        StringBuilder report = new StringBuilder("Year-End Salary Report: ");
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = (String) model.getValueAt(i, 0);
            String id = (String) model.getValueAt(i, 1);
            String dept = (String) model.getValueAt(i, 2);
            double BaseSalary = (double) model.getValueAt(i, 3);
            report.append(String.format("ID: %s | Name: %s | Department: %s | Salary: %.2f\n", name, id, dept, BaseSalary));

        }
        JTextArea reportarea = new JTextArea(report.toString());
        reportarea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportarea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
