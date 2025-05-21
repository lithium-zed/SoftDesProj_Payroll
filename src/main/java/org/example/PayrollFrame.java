package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayrollFrame extends JFrame {

    JLabel EmployeeID;
    JTextField EmployeeIDField;
    JButton CalculateButton;
    JTextArea CalculationArea;
    GridBagLayout layout;
    Container container;
    JScrollPane CalculationScrollPane;
    JPanel panel;

    private EmployeeAbstractTableModel model;

    public PayrollFrame(EmployeeAbstractTableModel model){
        this.model = model;
        layout = new GridBagLayout();
        container = this.getContentPane();
        container.setLayout(layout);

        EmployeeID = new JLabel("Employee ID:");
        EmployeeIDField = new JTextField(10);

        CalculateButton = new JButton("Calculate");

        CalculationArea = new JTextArea(35, 35);
        CalculationArea.setLineWrap(true);
        CalculationArea.setWrapStyleWord(true);
        CalculationArea.setEditable(false);
        CalculationArea.setFocusable(false);

        CalculationScrollPane = new JScrollPane(CalculationArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel = new JPanel();

        panel.add(EmployeeID);
        panel.add(EmployeeIDField);
        panel.add(CalculateButton);

        addToContainer(panel,0,0);
        addToContainer(CalculationArea,0,1);


        this.setSize(800,450);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Payroll");

        CalculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String entered = EmployeeIDField.getText();
                int index = IndexFinder(entered);

                if (index != -1) {
                    Employee employee = model.employees.get(index);

                    double monthlySalary = employee.getSalary();
                    double HourlyRate = monthlySalary / 160;
                    double SalaryBasedHours = HourlyRate * employee.getHoursWorked();

                    Compliance compliance = new Compliance(SalaryBasedHours);

                    double annualSalary = monthlySalary * 12;

                    StringBuilder sb = new StringBuilder();
                    sb.append("PAYROLL SUMMARY\n");
                    sb.append("Employee ID: ").append(employee.getEmployeeID()).append("\n");
                    sb.append("Name: ").append(employee.getFullName()).append("\n");
                    sb.append("Gross Salary:\t\t₱").append(String.format("%.2f", SalaryBasedHours)).append("\n");
                    sb.append("Monthly Salary:\t\t₱").append(String.format("%.2f", monthlySalary)).append("\n");
                    sb.append("Annual Salary:\t\t₱").append(String.format("%.2f", annualSalary)).append("\n\n");

                    sb.append("DEDUCTIONS:\n");
                    sb.append("SSS (5%):\t\t₱").append(String.format("%.2f", compliance.getSSS())).append("\n");
                    sb.append("PhilHealth (2.5%):\t₱").append(String.format("%.2f", compliance.getPhilhealth())).append("\n");
                    sb.append("PAG-IBIG (2%):\t\t₱").append(String.format("%.2f", compliance.getPagibig())).append("\n");
                    sb.append("BIR Monthly Tax:\t₱").append(String.format("%.2f", compliance.getMonthly())).append("\n");
                    sb.append("-----------------------------------\n");
                    sb.append("Total Deductions:\t₱").append(String.format("%.2f", compliance.getTotaldeductions())).append("\n");
                    sb.append("Net Pay:\t\t₱").append(String.format("%.2f", compliance.getNetpay())).append("\n");

                    CalculationArea.setText(sb.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "ID does not exist!");
                }
            }
        });
    }

    public int IndexFinder(String ID){

        int index = 0;

        for (int i = 0; i < PayrollFrame.this.model.getRowCount(); i++){
            String id = (String) PayrollFrame.this.model.getValueAt(i,1);
            if (ID.equals(id)){
                index = i;
                CalculationArea.setText("Employee ID Found");
                break;
            }else{
                index = -1; //no employee on index -1
            }
        }

        return index;
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



}
