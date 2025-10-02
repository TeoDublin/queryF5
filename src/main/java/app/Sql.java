package app;

import java.sql.*;

@SuppressWarnings({"SqlSourceToSinkFlow"})
public class Sql {
    String url;
    String user;
    String password;
    Statement stmt;

    public Sql() throws SQLException {

        this.url = "jdbc:mysql://integraa.net:53006/integraa_prod";
        this.user = "integraa_web_all";
        this.password = "Int3gr@4W.W.W.";
        Connection conn = DriverManager.getConnection(url, user, password);
        this.stmt = conn.createStatement();
    }

    public ResultSet select(String query) throws SQLException {
        return stmt.executeQuery(query);
    }
}