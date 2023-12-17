package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JPanel robiRobote;
    private JTextField userText;
    private JPasswordField passText;
    private JButton zalogujButton;
    private JButton rejestracjaButton;
    private JLabel userLable;
    private JLabel passLabel;
    private JPanel buttonsPanel;

    public  Login(){
        this.setContentPane(robiRobote);
        this.setTitle("Logowanie do bazy z filmami");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setSize(400,600);
        this.pack();
        this.setVisible(true);
        rejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Register register = new Register();
//                selecct  pozniej try catch i glowne okno
            }
        });
    }
}
