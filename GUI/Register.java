package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends JFrame {
    public  ConnectDataBase dataBase;
    private JPanel mainPanel;
    private JPanel filedsPanel;
    private JPanel buttonPanel;
    private JButton zarejestrujButton;
    private JTextField userField;
    private JTextField fnameField;
    private JPasswordField passField;
    private JLabel user;
    private JLabel fname;
    private JLabel lname;
    private JLabel pass;
    private JTextField lnameField;
    private JLabel role;
    private JCheckBox roleCheckBox;

    public Register(ConnectDataBase con){
        this.dataBase=con;
        this.setContentPane(mainPanel);
        this.setTitle("Rejestracja uzytkownika");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        zarejestrujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement(
                            "insert into uzytkownicy (login,haslo,imie,nazwisko,recenzent) values (?,?,?,?,?) ");
                    pst.setString(1,userField.getText());
                    pst.setString(2,passField.getText());
                    pst.setString(3,fnameField.getText());
                    pst.setString(4,lnameField.getText());
                    pst.setBoolean(5,roleCheckBox.isSelected());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Uzytkownik zajrestrowany pomyslnie");
                    dispose();
                    Login login =new Login(dataBase);
                }
                catch (SQLException exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage());
                }
            }
        });
    }
}
