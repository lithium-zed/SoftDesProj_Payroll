package org.example;

import javax.swing.*;
import java.awt.*;

public class EmployeeRecordsFrame extends JFrame {
    JPanel panel1, panel2;
    Container container;
    JLabel FullNameLabel,EmployeeIDLabel, DepartmentLabel, EditLabel;
    JTextField FullNameField, EmployeeIDField, DepartmentField, EditField;
    JButton AddButton, DeleteButton, EditButton;
    JTable Table;
    EmployeeAbstractTableModel Model;

    GridBagLayout layout;

    public EmployeeRecordsFrame(){
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


        EditLabel = new JLabel("Edit:");
        EditField = new JTextField(15);

        EditButton = new JButton("Edit");
        EditButton.setMnemonic('E');

        DeleteButton = new JButton("Delete");
        DeleteButton.setMnemonic('D');


        addToPanel(panel2, EditLabel, 4,0);
        addToPanel(panel2, EditField, 5,0);

        addToPanel(panel2, EditButton, 6,0);

        addToPanel(panel2, DeleteButton, 7,0);

        addToContainer(panel2, 0,1);

        Model = new EmployeeAbstractTableModel();
        Table = new JTable(Model);

        Table.getTableHeader().setReorderingAllowed(false);
        Table.getTableHeader().setResizingAllowed(false);

        addToContainer(new JScrollPane(Table), 0,2);

        this.setSize(700, 400);
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




}
