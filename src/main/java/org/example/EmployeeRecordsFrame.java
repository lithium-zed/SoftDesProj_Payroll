package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeRecordsFrame extends JFrame implements ActionListener {
    JPanel panel1, panel2;
    Container container;
    JLabel FullNameLabel,EmployeeIDLabel, DepartmentLabel, EditLabel, SalaryLabel;
    JTextField FullNameField, EmployeeIDField, DepartmentField, EditField, SalaryField;
    JButton AddButton, DeleteButton, EditButton,PayrollButton,AttendanceButton,YearendButton;
    FireStoreDatabase db;
    JTable Table;
    EmployeeAbstractTableModel Model;

    GridBagLayout layout;

    public EmployeeRecordsFrame(){
        db = FireStoreDatabase.getInstance();

        layout = new GridBagLayout();
        container = this.getContentPane();
        container.setLayout(layout);

        panel1 = new JPanel();

        FullNameLabel = new JLabel("FullName:");
        FullNameField = new JTextField(15);

        EmployeeIDLabel = new JLabel("Employee's ID:");
        EmployeeIDField = new JTextField(15);

        DepartmentLabel = new JLabel("Department:");
        DepartmentField = new JTextField(15);

        SalaryLabel = new JLabel("Salary:");
        SalaryField = new JTextField(15);

        AddButton = new JButton("Add");
        AddButton.setMnemonic('A');
        AddButton.addActionListener(this);

        addToPanel(panel1, FullNameLabel,0,0);
        addToPanel(panel1, FullNameField,1,0);

        addToPanel(panel1, EmployeeIDLabel,2,0);
        addToPanel(panel1, EmployeeIDField,3,0);

        addToPanel(panel1, DepartmentLabel,4,0);
        addToPanel(panel1, DepartmentField,5,0);

        addToPanel(panel1, SalaryLabel, 0, 1);
        addToPanel(panel1, SalaryField, 1, 1);

        addToPanel(panel1, AddButton,6,1);

        addToContainer(panel1,0,0);

        panel2 = new JPanel();


        EditLabel = new JLabel("Edit:");
        EditField = new JTextField(15);

        EditButton = new JButton("Edit");
        EditButton.setMnemonic('E');
        EditButton.addActionListener(this);

        DeleteButton = new JButton("Delete");
        DeleteButton.setMnemonic('D');
        DeleteButton.addActionListener(this);

        AttendanceButton = new JButton("Attendance");
        AttendanceButton.setMnemonic('A');
        AttendanceButton.addActionListener(this);

        PayrollButton = new JButton("Payroll");
        PayrollButton.setMnemonic('P');
        PayrollButton.addActionListener(this);


        addToPanel(panel2, EditLabel, 4,0);
        addToPanel(panel2, EditField, 5,0);

        addToPanel(panel2, EditButton, 6,0);

        addToPanel(panel2, DeleteButton, 7,0);

        addToPanel(panel2,AttendanceButton,7,0);

        addToPanel(panel2,PayrollButton,8,0);


        addToContainer(panel2, 0,1);

        Model = new EmployeeAbstractTableModel();
        Table = new JTable(Model);

        Table.getTableHeader().setReorderingAllowed(false);
        Table.getTableHeader().setResizingAllowed(false);

        addToContainer(new JScrollPane(Table), 0,2);

        db.getAllEmployeeRecords();
        this.setSize(800,450);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Employee Records");

        loadEmployeesFromFirestore();
    }

    public void addToPanel(JPanel panel, Component component,
                           int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(3,3,3,3);
        panel.add(component, constraints);
    }

    public void addToContainer(Component component,
                               int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(3,3,3,3);
        this.add(component, constraints);
    }

    private void loadEmployeesFromFirestore() {
        db.getAllEmployeeRecords().thenAccept(employees -> {
            SwingUtilities.invokeLater(() -> {
                Model.employees.clear();
                for (Employee emp : employees) {
                    Model.addEmployee(emp);
                }
                Model.fireTableDataChanged(); // Notify table that data has changed
                System.out.println("Employee data loaded from Firestore successfully.");
            });
        }).exceptionally(e -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            });
            System.err.println("Exception loading employees: " + e.getMessage());
            e.printStackTrace();
            return null;
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == AddButton) {
            String fullName = FullNameField.getText().trim();
            String employeeID = EmployeeIDField.getText().trim();
            String department = DepartmentField.getText().trim();
            String salaryText = SalaryField.getText().trim();
            if (fullName.isEmpty() || employeeID.isEmpty() || department.isEmpty() || salaryText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double salary = Double.parseDouble(salaryText);

                db.employeeExists(employeeID).thenAccept(exists -> {
                    SwingUtilities.invokeLater(() -> { // Ensure this block is on EDT
                        if (exists) {
                            JOptionPane.showMessageDialog(null, "Employee with ID already exists in the database.");
                        } else {
                            Employee newEmployee = new Employee(fullName, employeeID, department, salary);
                            db.addEmployee(newEmployee).thenRun(() -> {
                                SwingUtilities.invokeLater(() -> {
                                    Model.addEmployee(newEmployee);
                                    FullNameField.setText("");
                                    EmployeeIDField.setText("");
                                    DepartmentField.setText("");
                                    SalaryField.setText("");
                                    JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                });
                            }).exceptionally(ex -> {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(this, "Error adding employee: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                                });
                                return null;
                            });
                        }
                    });
                }).exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Error checking employee ID existence: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    });
                    return null;
                });

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Salary or Hours Worked format. Please enter numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == DeleteButton) {
            int selectedRow = Table.getSelectedRow();
            if (selectedRow >= 0) {
                String employeeIDToDelete = (String) Model.getValueAt(selectedRow, 1);
                db.deleteEmployee(employeeIDToDelete).thenRun(() -> {
                    SwingUtilities.invokeLater(() -> {
                        Model.deleteEmployee(selectedRow);
                        JOptionPane.showMessageDialog(this, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    });
                }).exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    });
                    return null;
                });
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == EditButton) {
            int selectedRow = Table.getSelectedRow();
            int selectedColumn = Table.getSelectedColumn();
            if (selectedRow >= 0 && selectedColumn >= 0) {
                String editValue = EditField.getText().trim();
                if (editValue.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Edit field cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Employee employeeToUpdate = Model.employees.get(selectedRow);
                String originalEmployeeID = employeeToUpdate.getEmployeeID();

                boolean changed = false;
                try {
                    if (selectedColumn == 0) { // FullName
                        employeeToUpdate.setFullName(editValue);
                        changed = true;
                    } else if (selectedColumn == 1) { // Employee ID
                        String newEmployeeID = editValue;
                        if (!originalEmployeeID.equals(newEmployeeID)) {
                            db.employeeExists(newEmployeeID).thenAccept(exists -> {
                                SwingUtilities.invokeLater(() -> {
                                    if (exists) {
                                        JOptionPane.showMessageDialog(this, "New Employee ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        Employee finalNewEmployee = new Employee(employeeToUpdate.getFullName(), newEmployeeID, employeeToUpdate.getDepartment(), employeeToUpdate.getSalary(), employeeToUpdate.getHoursWorked());
                                        db.deleteEmployee(originalEmployeeID)
                                                .thenCompose(v -> db.addEmployee(finalNewEmployee))
                                                .thenRun(() -> {
                                                    SwingUtilities.invokeLater(() -> {
                                                        Model.deleteEmployee(selectedRow);
                                                        Model.addEmployee(finalNewEmployee);
                                                        EditField.setText("");
                                                        JOptionPane.showMessageDialog(this, "Employee ID updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                                    });
                                                }).exceptionally(ex -> {
                                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error updating Employee ID: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE));
                                                    return null;
                                                });
                                    }
                                });
                            }).exceptionally(ex -> {
                                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error checking new Employee ID: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE));
                                return null;
                            });
                            return;
                        }
                    } else if (selectedColumn == 2) { // Department
                        employeeToUpdate.setDepartment(editValue);
                        changed = true;
                    } else if (selectedColumn == 3) { // Salary
                        double editSalary = Double.parseDouble(editValue);
                        employeeToUpdate.setSalary(editSalary);
                        changed = true;
                    } else if (selectedColumn == 4) { // Hours Worked
                        double editHoursWorked = Double.parseDouble(editValue);
                        employeeToUpdate.setHoursWorked(editHoursWorked);
                        changed = true;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format. Please enter a number for Salary or Hours Worked.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (changed) {
                    db.updateEmployee(employeeToUpdate).thenRun(() -> {
                        SwingUtilities.invokeLater(() -> {
                            Model.updateEmployee(selectedRow, employeeToUpdate);
                            EditField.setText("");
                            JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        });
                    }).exceptionally(ex -> {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "Error updating employee: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                        });
                        return null;
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row and a column to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
            EditField.setText("");
        } else if(e.getSource() == AttendanceButton){
            AttendanceFrame attendanceFrame = new AttendanceFrame(this.Model);
            attendanceFrame.setVisible(true);
        } else if (e.getSource() == PayrollButton){
            PayrollFrame payrollFrame = new PayrollFrame(this.Model);
            payrollFrame.setVisible(true);
        }
    }
}
