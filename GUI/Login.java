package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private String[] userData =new String[3];
    private boolean userRole;
    private  int id;
    public ConnectDataBase dataBase;
    private JPanel robiRobote;
    private JTextField userText;
    private JPasswordField passText;
    private JButton zalogujButton;
    private JButton rejestracjaButton;
    private JLabel userLable;
    private JLabel passLabel;
    private JPanel buttonsPanel;

    public  Login(ConnectDataBase con){
        this.dataBase=con;
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
                Register register = new Register(con);
            }
        });

        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreparedStatement pst = null;
                try {
                    pst = dataBase.connection.prepareStatement("select u.uzytkownik_id, u.login ,u.haslo,u.imie,u.nazwisko,u.recenzent  from  uzytkownicy u where u.login=? and u.haslo=?");
                    pst.setString(1,userText.getText());
                    pst.setString(2,passText.getText());
                    ResultSet rs= pst.executeQuery();
                    while (rs.next()){
                        id=rs.getInt("uzytkownik_id");
                        userData[0]=rs.getString("login");
                        userData[1]=rs.getString("imie");
                        userData[2]=rs.getString("nazwisko");
                        userRole=rs.getBoolean("recenzent");
                        System.out.println(rs.getString("login")+" "+rs.getString("haslo"));
                    }
                    dispose();
                    MainApp mainApp= new MainApp(dataBase,id,userData[0],userData[1],userData[2],userRole);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }


            }
        });
    }
}
