package com.sleightholme.micromysql;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jonathan coustick
 */
public class DBC {

    private Connection connection;
    private MysqlDataSource ds;

    public enum Databases {
        MYSQL, MARIADB
    }

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "amazon";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public void connect() {
        try {
            
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUser(USER);
            mysqlDataSource.setPassword(PASSWORD);
            mysqlDataSource.setServerName(HOST);
            mysqlDataSource.setDatabaseName(DATABASE);
            mysqlDataSource.setPort(PORT);
            connection = mysqlDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Unable to get connection");
            System.out.println(e);
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println("Connected");
        } else {
            System.out.println("Not connected");
        }
    }

    public ResultSet runSqlQuery(String query) {
        ResultSet rs = null;
        try {
            Statement stmnt = connection.createStatement();
            rs = stmnt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute query");
            System.out.println(query);
            System.out.println(e);
            e.printStackTrace();
        }
        return rs;
    }

    public void printQueryOutput(ResultSet rs) throws SQLException {
        if (rs != null) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                if (rsmd.getColumnLabel(i).equals("insert_id")) {
                    System.out.print("Successfully executed query");
                    return;
                } else {
                    System.out.print(rsmd.getColumnLabel(i) + "\t");
                }
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.print("\n");
            }

        } else {
            System.out.println("No result set.");
        }
    }

    public void close() {
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException e) {
            System.out.println("Cannot close connection");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
