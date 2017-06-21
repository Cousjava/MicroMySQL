/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *    Copyright (c) [2017] Payara Foundation and/or its affiliates. All rights reserved.
 * 
 *     The contents of this file are subject to the terms of either the GNU
 *     General Public License Version 2 only ("GPL") or the Common Development
 *     and Distribution License("CDDL") (collectively, the "License").  You
 *     may not use this file except in compliance with the License.  You can
 *     obtain a copy of the License at
 *     https://github.com/payara/Payara/blob/master/LICENSE.txt
 *     See the License for the specific
 *     language governing permissions and limitations under the License.
 * 
 *     When distributing the software, include this License Header Notice in each
 *     file and include the License file at glassfish/legal/LICENSE.txt.
 * 
 *     GPL Classpath Exception:
 *     The Payara Foundation designates this particular file as subject to the "Classpath"
 *     exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 *     file that accompanied this code.
 * 
 *     Modifications:
 *     If applicable, add the following below the License Header, with the fields
 *     enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyright [year] [name of copyright owner]"
 * 
 *     Contributor(s):
 *     If you wish your version of this file to be governed by only the CDDL or
 *     only the GPL Version 2, indicate your decision by adding "[Contributor]
 *     elects to include this software in this distribution under the [CDDL or GPL
 *     Version 2] license."  If you don't indicate a single choice of license, a
 *     recipient has the option to distribute your version of this file under
 *     either the CDDL, the GPL Version 2 or to extend the choice of license to
 *     its licensees as provided above.  However, if you add GPL Version 2 code
 *     and therefore, elected the GPL Version 2 license, then the option applies
 *     only if the new code is made subject to such option by the copyright
 *     holder.
 */
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
