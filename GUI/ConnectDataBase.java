package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDataBase {
    private  final  String url ="jdbc:postgresql://flora.db.elephantsql.com:5432/mfxccspr";
    private final String user="mfxccspr";
    private final String pass="rz5fi8BGDPy41xNxBhAo6OJQfVCkCoa2";
    public  Connection connection;

    public ConnectDataBase(){
        try {
            connection= DriverManager.getConnection(url,user,pass);
            System.out.println("lux");
        }
        catch (SQLException e){
            System.out.println("blad "+ e.getMessage());
        }
    }

}
