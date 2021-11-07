package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    /*1st Rule*/
    private static DbConnection dbConnection=null;
    private final Connection connection;

    /*2nd Rule*/
    private DbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/SportShop_360",
                "root",
                "Janith20010121");
    }

    /*3rd Rule*/
    public static DbConnection getInstance() throws ClassNotFoundException, SQLException {
        if (dbConnection==null){
            dbConnection= new DbConnection();
        }
        return dbConnection;

       /* return (dbConnection==null)?(dbConnection= new DbConnection()):(dbConnection);*/
    }

    public Connection getConnection(){
        return connection;
    }
}
