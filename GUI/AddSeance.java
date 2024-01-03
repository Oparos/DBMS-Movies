package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSeance extends JFrame{
    private final ConnectDataBase dataBase;

    private JPanel mainPanel;
    private JTextField idMovieField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField firstSeatField;
    private JButton addButton;
    private JLabel startDate;
    private JLabel endDate;
    private JLabel firstSeat;
    private JLabel idMovie;
    private JLabel lastSeat;
    private JTextField lastSeatField;
    private JTextField no_seatsField;
    private JLabel no_seats;

    public AddSeance(ConnectDataBase conn) {
        this.dataBase=conn;
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst =dataBase.connection.prepareStatement("insert into seanse (film_id, data_poczatek,data_koniec,numer_poczatkowy_miejsca,numer_koncowy_miejsca,liczba_dostepnych_biletow)  values (?,?,?,?,?,?)");
                    pst.setInt(1, Integer.parseInt(idMovieField.getText()));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date parsedDate = dateFormat.parse(startDateField.getText());
                    Timestamp timestamp = new Timestamp(parsedDate.getTime());
                    pst.setTimestamp(2, timestamp);
                    parsedDate = dateFormat.parse(endDateField.getText());
                    timestamp = new Timestamp(parsedDate.getTime());
                    pst.setTimestamp(3, timestamp);
                    pst.setInt(4, Integer.parseInt(firstSeatField.getText()));
                    pst.setInt(5, Integer.parseInt(lastSeatField.getText()));
                    pst.setInt(6, Integer.parseInt(no_seatsField.getText()));
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Seans dodany pomyslnie");
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
