package ankit.hld.week1.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPoolUsingBoundedQueue {

    private final BlockingQueue<Connection> pool;
    public ConnectionPoolUsingBoundedQueue(int poolSize,
                                            ConnectionManager connectionManager){
        pool = new ArrayBlockingQueue<>(poolSize);
        for(int i=0; i<poolSize; i++){
            pool.add(connectionManager.createAndGetConnection());
        }
    }

    public Connection getConnection() throws InterruptedException {
        return pool.take();
    }

    public void releaseConnection(Connection connection) {
        pool.add(connection);
    }

    public static void testDirectConnections() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager();
        LocalDateTime startDateTime = LocalDateTime.now();
        for (int i=0; i<1500; i++){
            Connection connection = connectionManager.createAndGetConnection();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute("Select sleep(0.1)");
            } finally {
                if(statement!=null && !statement.isClosed()){
                    statement.close();
                }
                if(connection!=null && !connection.isClosed()){
                    connection.close();
                }
            }
        }
        System.out.println("Time Taken : "+ChronoUnit.SECONDS.between(startDateTime, LocalDateTime.now()) );
    }

    public static void testConnectionPool() throws InterruptedException, SQLException {
        ConnectionManager connectionManager = new ConnectionManager();
        ConnectionPoolUsingBoundedQueue pool =
                    new ConnectionPoolUsingBoundedQueue(10, connectionManager);
        LocalDateTime startDateTime = LocalDateTime.now();
        for (int i=0; i<500; i++){
            Connection connection = pool.getConnection();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute("Select sleep(0.1)");
            } finally {
                if(statement!=null && !statement.isClosed()){
                    statement.close();
                }
                pool.releaseConnection(connection);
            }
        }
        System.out.println("Time Taken : "+ChronoUnit.SECONDS.between(startDateTime, LocalDateTime.now()) );
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        //testDirectConnections();
        testConnectionPool();
    }


}
