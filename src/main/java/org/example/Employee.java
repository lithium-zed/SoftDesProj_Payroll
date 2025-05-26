package org.example;

public class Employee {

    String FullName, EmployeeID, Department, time_in, time_out;
    double Salary, hoursWorked;
    private boolean isWorking;
    private double hourlyrate;

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

    public double getHourlyrate() {
        return hourlyrate;
    }

    public void setHourlyrate(double hourlyrate) {
        this.hourlyrate = hourlyrate;
    }
    public double getyearlyrate(double hourlyrate){
        return hoursWorked * hourlyrate;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
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
