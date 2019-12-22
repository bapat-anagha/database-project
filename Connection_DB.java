/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;

/**
 *
 * @author vithakar
 */

/**
 *
 * Class for creating DB connection.
 */
import java.sql.*;

public class Connection_DB {

    public static Connection get_DBConn() {
        Connection DBConn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            //DBConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle");
            DBConn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/system", "system", "oracle");
            return DBConn;
        } catch (SQLException e) {
            System.out.println("Not connected to database ! : " + e.getMessage());
        }
        return DBConn;

    }
}