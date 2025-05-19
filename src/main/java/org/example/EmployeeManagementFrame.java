package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeManagementFrame extends JFrame {
    JPanel panel1, panel2;
    Container container;
    JLabel FullNameLabel,EmployeeIDLabel, DepartmentLabel, EditLabel,TimeLabel;
    JTextField FullNameField, EmployeeIDField, DepartmentField, EditField,TimeField;
    JButton AddButton, DeleteButton, EditButton,InButton,OutButton;
    JTable Table;
    EmployeeAbstractTableModel Model;


    GridBagLayout layout;

    public EmployeeManagementFrame(){
        layout = new GridBagLayout();
        container = this.getContentPane();
        container.setLayout(layout);

        Model = new EmployeeAbstractTableModel();
        Table = new JTable(Model);


        Buttons buttons = new Buttons(Model,Table);
        panel1 = new JPanel();

        FullNameLabel = new JLabel("FullName:");
        FullNameField = new JTextField(15);

        EmployeeIDLabel = new JLabel("Employee's ID:");
        EmployeeIDField = new JTextField(15);

        DepartmentLabel = new JLabel("Department:");
        DepartmentField = new JTextField(15);

        AddButton = new JButton("Add");
        AddButton.setMnemonic('A');

        addToPanel(panel1, FullNameLabel,0,0);
        addToPanel(panel1, FullNameField,1,0);

        addToPanel(panel1, EmployeeIDLabel,2,0);
        addToPanel(panel1, EmployeeIDField,3,0);

        addToPanel(panel1, DepartmentLabel,4,0);
        addToPanel(panel1, DepartmentField,5,0);

        addToPanel(panel1, AddButton,6,0);

        addToContainer(panel1,0,0);

        panel2 = new JPanel();

        TimeLabel = new JLabel("Time:");
        TimeField = new JTextField(10);

        InButton = new JButton("In");
        OutButton = new JButton("Out");

        EditLabel = new JLabel("Edit:");
        EditField = new JTextField(15);

        EditButton = new JButton("Edit");
        EditButton.setMnemonic('E');

        DeleteButton = new JButton("Delete");
        DeleteButton.setMnemonic('D');


        addToPanel(panel2, TimeLabel, 0,0);
        addToPanel(panel2, TimeField, 1,0);

        addToPanel(panel2, InButton, 2,0);
        addToPanel(panel2, OutButton, 3,0);

        addToPanel(panel2, EditLabel, 4,0);
        addToPanel(panel2, EditField, 5,0);

        addToPanel(panel2, EditButton, 6,0);

        addToPanel(panel2, DeleteButton, 7,0);

        addToContainer(panel2, 0,1);


        Table.getTableHeader().setReorderingAllowed(false);
        Table.getTableHeader().setResizingAllowed(false);

        addToContainer(new JScrollPane(Table), 0,2);

        AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = FullNameField.getText();
                String id = EmployeeIDField.getText();
                String dept = DepartmentField.getText();
                buttons.addEmployee(fullName,id,dept);
                FullNameField.setText("");
                EmployeeIDField.setText("");
                DepartmentField.setText("");
            }
        });

        this.setSize(700, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Employee Management");
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





}
