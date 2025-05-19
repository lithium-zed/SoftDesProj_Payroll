package org.example;

import javax.swing.*;

public class Buttons {
    private EmployeeAbstractTableModel model;
    public Buttons (EmployeeAbstractTableModel model, JTable table){
        this.model = model;
    }
    public void addEmployee(String fullname,String employeeID,String department){
        if (!fullname.isEmpty() && !employeeID.isEmpty() && !department.isEmpty()){
            model.addEmployee(fullname,employeeID,department);
        } else {
            JOptionPane.showMessageDialog(null,"Please fill in all the fields","Input Error",JOptionPane.ERROR_MESSAGE);
        }
    }

}
