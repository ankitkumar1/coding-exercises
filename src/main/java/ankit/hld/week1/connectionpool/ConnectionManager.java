package ankit.hld.week1.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    String url = "jdbc:mysql://127.0.0.1:3306/hld_classes";
    String user = "root"; // your mysql username
    String password = "root";

    public Connection createAndGetConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            if(connection!=null){
                //System.out.println("Connection Created!");
                return connection;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void closeConnection(Connection connection) throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
