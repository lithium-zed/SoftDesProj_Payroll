package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class EmployeeAbstractTableModel extends AbstractTableModel {

    ArrayList<Employee> employees;

    String []columns = {"FullName", "Employee ID", "Department", "Salary", "Hours Worked"};
    public EmployeeAbstractTableModel(){
        employees = new ArrayList<>();
    }
    public void addEmployee(Employee employee){
        employees.add(employee);
        fireTableDataChanged();
    }
    public void deleteEmployee(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < employees.size()) {
            employees.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    public void updateEmployee(int rowIndex, Employee employee) {
        if (rowIndex >= 0 && rowIndex < employees.size()) {
            employees.set(rowIndex, employee);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
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
            return employee.getHoursWorked();
        }
        return null;
    }
}
