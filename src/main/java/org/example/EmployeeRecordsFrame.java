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
        db = new FireStoreDatabase(this);

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

        YearendButton = new JButton("Year-end Summary");
        YearendButton.setMnemonic('Y');
        YearendButton.addActionListener(this);

        addToPanel(panel2, EditLabel, 4,0);
        addToPanel(panel2, EditField, 5,0);

        addToPanel(panel2, EditButton, 6,0);

        addToPanel(panel2, DeleteButton, 7,0);

        addToPanel(panel2,AttendanceButton,7,0);

        addToPanel(panel2,PayrollButton,8,0);

        addToPanel(panel2,YearendButton,9,0);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == AddButton) {
            String fullName = FullNameField.getText();
            String employeeID = EmployeeIDField.getText();
            String department = DepartmentField.getText();
            String salaryText = SalaryField.getText();
            boolean exists =false;
            for (int i = 0; i < Model.getRowCount(); i++){
                String existing = (String) Model.getValueAt(i,1);
                if (existing.equals(employeeID)){
                    exists=true;
                    break;
                }
            }
            if(exists){
                JOptionPane.showMessageDialog(null,"Employee with ID already exists");
            } else{

            try {
                double salary = Double.parseDouble(salaryText);
                Employee newEmployee = new Employee(fullName, employeeID, department, salary);
                Model.addEmployee(newEmployee);

                db.addEmployee(newEmployee);

                FullNameField.setText("");
                EmployeeIDField.setText("");
                DepartmentField.setText("");
                SalaryField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Salary format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }}
        } else if (e.getSource() == DeleteButton) {
            int selectedRow = Table.getSelectedRow();
            if (selectedRow >= 0) {
                Model.deleteEmployee(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == EditButton) {
            int selectedRow = Table.getSelectedRow();
            if (selectedRow >= 0) {
                String editValue = EditField.getText();
                if(Table.getSelectedColumn() == 0){
                    Employee employeeToUpdate = new Employee(editValue,
                            (String) Model.getValueAt(selectedRow, 1),
                            (String) Model.getValueAt(selectedRow, 2),
                            (double) Model.getValueAt(selectedRow, 3));
                    Model.updateEmployee(selectedRow, employeeToUpdate);
                } else if (Table.getSelectedColumn() == 1) {
                    Employee employeeToUpdate = new Employee(
                            (String)Model.getValueAt(selectedRow,0),
                            editValue,
                            (String) Model.getValueAt(selectedRow, 2),
                            (double) Model.getValueAt(selectedRow, 3));
                    Model.updateEmployee(selectedRow, employeeToUpdate);
                }else if (Table.getSelectedColumn() == 2) {
                    Employee employeeToUpdate = new Employee(
                            (String)Model.getValueAt(selectedRow,0),
                            (String) Model.getValueAt(selectedRow, 1),
                            editValue,
                            (double) Model.getValueAt(selectedRow, 3));
                    Model.updateEmployee(selectedRow, employeeToUpdate);
                } else if (Table.getSelectedColumn() == 3) {
                    try{
                        double editSalary = Double.parseDouble(editValue);
                        Employee employeeToUpdate = new Employee(
                                (String)Model.getValueAt(selectedRow,0),
                                (String) Model.getValueAt(selectedRow, 1),
                                (String) Model.getValueAt(selectedRow, 2),
                                editSalary);
                        Model.updateEmployee(selectedRow, employeeToUpdate);
                    }catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(this, "Invalid Salary format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                EditField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if(e.getSource() == AttendanceButton){
            AttendanceFrame attendanceFrame = new AttendanceFrame(this.Model);
            attendanceFrame.setVisible(true);
        } else if (e.getSource() == PayrollButton){
            PayrollFrame payrollFrame = new PayrollFrame(this.Model);
            payrollFrame.setVisible(true);
        } else if (e.getSource() == YearendButton){
            YearEndFrame yearEndFrame = new YearEndFrame(this.Model);
            yearEndFrame.setVisible(true);
        }
    }
}
