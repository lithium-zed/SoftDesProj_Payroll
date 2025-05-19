package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class EmployeeAbstractTableModel extends AbstractTableModel {

    ArrayList<Employee> employees;

    String []columns = {"FullName", "Employee ID", "Department", "Salary", "Time-In", "Time-Out"};
    public EmployeeAbstractTableModel(){
        employees = new ArrayList<>();
    }

    @Override
    public String getColumnName(int index){
        return columns[index];
    }
    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Employee employee = employees.get(rowIndex);

        if(columnIndex == 0){
            return employee.getFullName();
        }else if (columnIndex == 1){
            return employee.getEmployeeID();
        }else if (columnIndex == 2){
            return employee.getDepartment();
        }else if (columnIndex == 3){
            return employee.getSalary();
        }else if (columnIndex == 4){
            return employee.getTimeIn();
        }else if (columnIndex == 5){
            return employee.getTimeOut();
        }else{
            return null;
        }

    }

    public void addEmployee(String fullname, String employeeID, String department) {
    double salary = 0.0;
    String timeIn = "";
    String timeOut = "";

     Employee employee = new Employee(fullname,employeeID,department,salary,timeIn,timeOut);
     employees.add(employee);
     fireTableRowsInserted(employees.size() -1, employees.size() - 1);
    }
}
