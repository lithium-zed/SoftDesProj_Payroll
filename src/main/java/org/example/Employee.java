package org.example;

public class Employee {

    String FullName, EmployeeID, Department;
    double Salary, hoursWorked;

    public Employee(String fullName, String employeeID, String department, double salary, double hoursWorked) {
        this.FullName = fullName;
        this.EmployeeID = employeeID;
        this.Department = department;
        this.Salary = salary;
        this.hoursWorked = hoursWorked;
    }
    public Employee(String fullName, String employeeID, String department, double salary) {
        this.FullName = fullName;
        this.EmployeeID = employeeID;
        this.Department = department;
        this.Salary = salary;
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

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getSalary() {
        return Salary;
    }

    public void setSalary(double salary) {
        Salary = salary;
    }
}
