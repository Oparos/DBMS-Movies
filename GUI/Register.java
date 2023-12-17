package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
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
    private JComboBox roleBox;
    private JLabel role;

    public Register(){
        this.setContentPane(mainPanel);
        this.setTitle("Rejestracja uzytkownika");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        zarejestrujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                insert Into pewnie w try catchu
                dispose();
                JOptionPane.showMessageDialog(null,"");
                Login login =new Login();
            }
        });
    }
}
