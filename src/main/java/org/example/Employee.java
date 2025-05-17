package org.example;

public class Employee {

    String FullName, EmployeeID, Department, TimeIn, TimeOut;
    double Salary;

    public Employee(String fullName, String employeeID, String department, double salary, String timeIn, String timeOut) {
        FullName = fullName;
        EmployeeID = employeeID;
        Department = department;
        Salary = salary;
        TimeIn = timeIn;
        TimeOut = timeOut;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getTimeIn() {
        return TimeIn;
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }

    public double getSalary() {
        return Salary;
    }

    public void setSalary(double salary) {
        Salary = salary;
    }
}
