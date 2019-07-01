package com.wildbeeslabs.jentle.algorithms.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ceki G&uuml;lc&uuml;
 */
public class DBHelper {

    static public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqle) {
                // static utility classes should not log without an explicit repository
                // reference
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqle) {
            }
        }
    }
}
