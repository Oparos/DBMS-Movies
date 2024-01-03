package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyTicket extends JFrame{
    private final ConnectDataBase dataBase;
    private final int id;

    private JPanel mainPanel;
    private JTextField seanceField;
    private JTextField seatsField;
    private JButton buyButton;

    private JLabel idSeance;
    private JButton pokazZajeteMiejscaNaButton;
    private JLabel no_seat;

    public BuyTicket(ConnectDataBase conn,int id) {
        this.id=id;
        this.dataBase=conn;
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("insert into bilety (seans_id,uzytkownik_id,numer_miejsca,cena) values (?,?,?,30)");
                    pst.setInt(1, Integer.parseInt(seanceField.getText()));
                    pst.setInt(2, id);
                    pst.setInt(3, Integer.parseInt(seatsField.getText()));
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Bilet kupiony");
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });

        pokazZajeteMiejscaNaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("select numer_miejsca from bilety where seans_id=? ");
                    PreparedStatement pst1 =dataBase.connection.prepareStatement("select numer_poczatkowy_miejsca, numer_koncowy_miejsca  from seanse where seans_id=? ");
                    pst.setInt(1,Integer.parseInt(seanceField.getText()));
                    pst1.setInt(1,Integer.parseInt(seanceField.getText()));
                    ResultSet resultSet1 =pst1.executeQuery();
                    if(resultSet1.next()==false){
                        JOptionPane.showMessageDialog(null,"Nie ma takiego senasu");
                    }
                    else {
                    ResultSet resultSet =pst.executeQuery();

                    StringBuilder no_seats= new StringBuilder(new String("Zajete miejsca:"));

                    while (resultSet.next()){
                        no_seats.append(" ").append(resultSet.getInt(1));
                    }

                    no_seats.append("\nZakres miejsc do wyboru to: ");

                   do {
                        no_seats.append(resultSet1.getInt(1)).append("-").append(resultSet1.getInt(2));
                    } while (resultSet1.next());

                    JOptionPane.showMessageDialog(null,no_seats);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
    }
}
