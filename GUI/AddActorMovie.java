package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddActorMovie extends JFrame{
    private final ConnectDataBase dataBase;

    private JPanel mainPanel;
    private JTextField idMovieField;
    private JTextField idActorField;
    private JButton relateButton;

    private JLabel movie;
    private JLabel actor;
    private JLabel role;
    private JTextField roleField;

    public AddActorMovie(ConnectDataBase conn) {
        this.dataBase=conn;
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        relateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("insert into filmaktor (film_id,id_aktora,rola) values (?,?,?)");
                    pst.setInt(1, Integer.parseInt(idMovieField.getText()));
                    pst.setInt(2, Integer.parseInt(idActorField.getText()));
                    pst.setString(3, roleField.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Aktor powiazany");
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
    }
}
