package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainApp extends JFrame{
    private ConnectDataBase dataBase;
    private String login;
    private  String fname;
    private String lname;
    private final int id;
    private Boolean adminRole;
    private JButton showTickets;
    private JButton showFIlms;
    private JPanel buttonsPanel;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JPanel infoPanel;
    private JScrollPane scrollPane;
    private JLabel roleLabel;
    private JLabel loginAsLabel;
    private JTable resultTable;
    private JButton actorsInMovie;
    private JButton addReview;
    private JButton addMovie;
    private JButton buyTicket;
    private JButton addSeance;
    private JButton addActor;
    private JButton relateActorMovie;
    private JButton showSeance;
    private JButton showReview;
    private JTextField titleOfMovie;
    private JLabel typeOfSort;
    private JComboBox sortComboBox;
    private JButton summarySoldTickets;
    private JButton actorsInNOMovies;
    private JTextField numberMovies;
    private JButton showActors;

    public MainApp(ConnectDataBase connectDataBase,int id,String login,String fname,String lname,Boolean adminRole){
        this.setTitle("Programson");
        this.dataBase=connectDataBase;
        this.login=login;
        this.adminRole=adminRole;
        this.id=id;
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginAsLabel.setText(loginAsLabel.getText()+": "+fname+" "+lname);
        if(adminRole){
            roleLabel.setText(roleLabel.getText()+" Administrator");

        }else {
            roleLabel.setText(roleLabel.getText()+ " Uzytkownik");
            relateActorMovie.setEnabled(false);
            addActor.setEnabled(false);
            addSeance.setEnabled(false);
            addMovie.setEnabled(false);
            summarySoldTickets.setEnabled(false);
        }
        this.pack();
        this.setVisible(true);

        showTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("""
                            SELECT b.numer_miejsca,
                                f.tytul AS tytul_filmu,
                                s.data_poczatek AS data_poczatek_seansu
                            FROM  Bilety b\s
                            JOIN Uzytkownicy u ON b.uzytkownik_id = u.uzytkownik_id
                            JOIN Seanse s ON b.seans_id = s.seans_id
                            JOIN Filmy f ON s.film_id = f.film_id
                            WHERE u.uzytkownik_id = ?""");
                    pst.setInt(1,id);
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("Numer miejsca");
                    model.addColumn("Tytul filmu");
                    model.addColumn("data rozpoczecia film");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getString(1),rs.getString(2), rs.getString(3) });
                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        showFIlms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("SELECT * FROM  filmy");
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("ID");
                    model.addColumn("tytul");
                    model.addColumn("rezyser");
                    model.addColumn("rok produkcji");
                    model.addColumn("ocena");
                    model.addColumn("producent");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3),rs.getInt(4), rs.getDouble(5),rs.getString(6) });
//                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        actorsInMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("select a.imie,a.nazwisko,fa.rola from aktorzy a join filmaktor fa on a.aktor_id=fa.id_aktora join filmy f on fa.film_id=f.film_id where f.tytul=?");
                    ResultSet rs;
                    JFrame findWindow =new JFrame("wyszukiwacz");
                    findWindow.setLayout(new BoxLayout(findWindow.getContentPane(), BoxLayout.Y_AXIS));
                    JLabel tresc=new JLabel("Podaj tytul filmu");
                    findWindow.add(tresc);
                    JTextField title = new JTextField();
                    findWindow.add(title);
                    JButton findButton=new JButton("Szukaj");
//                    findButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    findWindow.add(findButton);
                    findButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                pst.setString(1,title.getText());
                                ResultSet rs =pst.executeQuery();
                                DefaultTableModel model =new DefaultTableModel();
                                model.addColumn("Imie");
                                model.addColumn("Nazwisko");
                                model.addColumn("Rola w filmie");
                                while (rs.next()){
                                    model.addRow(new Object[]{rs.getString(1),rs.getString(2), rs.getString(3) });
                                    System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                                }
                                resultTable.setModel(model);
                                scrollPane.setViewportView(resultTable);
                                rs.close();
                                findWindow.dispose();
                            } catch (SQLException ex) {
//                                throw new RuntimeException(ex);
                                System.out.println(ex.getMessage());
                            }

                        }
                    });
                    findWindow.pack();
                    findWindow.setVisible(true);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        addReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Review review=new Review(dataBase,id);
            }
        });

        addMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AddMovie addMovie =new AddMovie(dataBase);
            }
        });

        buyTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            BuyTicket buyTicket= new BuyTicket(dataBase,id);
            }
        });

        addSeance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSeance addSeance =new AddSeance(dataBase);
            }
        });

        addActor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddActor addActor = new AddActor(dataBase);
            }
        });

        relateActorMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddActorMovie addActorMovie =new AddActorMovie(dataBase);
            }
        });

        showSeance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("select s.seans_id,f.tytul ,s.data_poczatek ,s.data_koniec ,s.liczba_dostepnych_biletow from seanse s join filmy f on s.film_id =f.film_id");
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("ID seansu");
                    model.addColumn("Tytul filmu");
                    model.addColumn("Poczatek seansu");
                    model.addColumn("Koniec seansu");
                    model.addColumn("Wolnych miejsc");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getInt(1),rs.getString(2), rs.getTimestamp(3),rs.getTimestamp(4), rs.getInt(5) });
//                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        showReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("select concat( concat(u.imie,' '), u.nazwisko) as ludek,r.tytul_recenzji, r.tresc_recenzji, r.ocena_recenzji\n" +
                            "from recenzje r join filmy f on r.film_id =f.film_id \n" +
                            "join uzytkownicy u on r.uzytkownik_id =u.uzytkownik_id \n" +
                            "where f.tytul =? order by r.ocena_recenzji  " + (sortComboBox.getSelectedItem().toString().equals("A-Z") ? "ASC" : "DESC"));
                    pst.setString(1,titleOfMovie.getText());
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("Imie i nazwisko");
                    model.addColumn("Tytul");
                    model.addColumn("Treść");
                    model.addColumn("Ocena filmu");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getString(1),rs.getString(2), rs.getString(3),rs.getDouble(4) });
//                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        summarySoldTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("select f.tytul, count(*)  from bilety b \n" +
                            "join seanse s on b.seans_id =s.seans_id \n" +
                            "join filmy f on s.film_id =f.film_id \n" +
                            "group  by f.tytul ;");
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("Tytul");
                    model.addColumn("Ilosc sprzedanch biletow");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getString(1),rs.getInt(2) });
//                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        actorsInNOMovies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("SELECT a.imie, a.nazwisko, COUNT(fa.film_id) AS liczba_filmow\n" +
                            "FROM Aktorzy a\n" +
                            "JOIN FilmAktor fa ON a.aktor_id = fa.id_aktora\n" +
                            "GROUP BY a.imie, a.nazwisko\n" +
                            "HAVING COUNT(fa.film_id) >= ?;");
                    pst.setInt(1,Integer.parseInt(numberMovies.getText()));
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("Imie");
                    model.addColumn("Nazwisko");
                    model.addColumn("Ilosc filmow");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getString(1),rs.getString(2), rs.getInt(3) });
//                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        showActors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pst= dataBase.connection.prepareStatement("select * from aktorzy");
                    ResultSet rs=pst.executeQuery();
                    DefaultTableModel model =new DefaultTableModel();
                    model.addColumn("ID");
                    model.addColumn("Imie");
                    model.addColumn("Nazwisko");
                    while (rs.next()){
                        model.addRow(new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3) });
//                        System.out.println(rs.getString(1)+ " "+rs.getString(2)+" "+ rs.getString(3) );
                    }
                    resultTable.setModel(model);
                    scrollPane.setViewportView(resultTable);
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }
}
