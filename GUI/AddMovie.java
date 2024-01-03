package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMovie extends JFrame{
    private final ConnectDataBase dataBase;

    private JPanel mainPanel;
    private JTextField titleField;
    private JTextField directorField;
    private JTextField yearField;
    private JTextField producerField;
    private JButton addButton;
    private JLabel director;
    private JLabel yearProd;
    private JLabel producer;
    private JLabel title;

    public AddMovie(ConnectDataBase conn) {
        this.dataBase=conn;
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("insert  into  filmy (tytul,rezyser,rok_produkcji,ocena,producent) values (?,?,?,0,?)");
                    pst.setString(1,titleField.getText());
                    pst.setString(2,directorField.getText());
                    pst.setInt(3, Integer.parseInt(yearField.getText()));
                    pst.setString(4,producerField.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Film dodany pomyslnie");
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
    }
}
