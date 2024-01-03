package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddActor extends JFrame{
    private final ConnectDataBase dataBase;

    private JPanel mainPanel;
    private JTextField fnameField;
    private JTextField lnameField;
    private JButton addButton;

    private JLabel fname;
    private JLabel lname;

    public AddActor(ConnectDataBase conn) {
        this.dataBase=conn;
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("insert into  aktorzy (imie,nazwisko) values (?,?)");
                    pst.setString(1, fnameField.getText());
                    pst.setString(2, lnameField.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Aktor dodany");
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
    }
}
