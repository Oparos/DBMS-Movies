package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;

public class Review extends JFrame{
    private final ConnectDataBase dataBase;
    private final int id;
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField titleField;
    private JTextField reviewField;
    private JTextField ratingField;
    private JButton przeslijButton;

    public Review(ConnectDataBase conn,int ident) {
        this.dataBase=conn;
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        this.id=ident;
        przeslijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("insert into recenzje (film_id,uzytkownik_id,tytul_recenzji,tresc_recenzji,ocena_recenzji) values (?,?,?,?,?)");
                    pst.setInt(1,Integer.parseInt(idField.getText()));
                    pst.setInt(2,id);
                    pst.setString(3,titleField.getText());
                    pst.setString(4,reviewField.getText());
                    pst.setDouble(5,Double.parseDouble(ratingField.getText()));
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Recenzja dodana pomyslnie");
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
    }
}
