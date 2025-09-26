package app;

import java.sql.*;

public class Sql {
    String url = "jdbc:mysql://devop.integraa.net:3306/integraa_20250410";
    String user = "integraa_web_all";
    String password = "Int3gr@4W.W.W.";
    Statement stmt;

    public Sql() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        this.stmt = conn.createStatement();
    }

    public ResultSet select(String query) throws SQLException {
        return stmt.executeQuery(query);
    }
}
