package org.example;

import javax.swing.*;
import java.awt.*;

public class PayrollFrame extends JFrame {

    JLabel EmployeeID;
    JTextField EmployeeIDField;
    JButton CalculateButton;
    JTextArea CalculationArea;
    GridBagLayout layout;
    Container container;
    JScrollPane CalculationScrollPane;
    JPanel panel;

    public PayrollFrame(){

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


        CalculationArea.setText("\t TESTTTT");

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
