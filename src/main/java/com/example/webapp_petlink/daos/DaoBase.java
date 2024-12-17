    package com.example.webapp_petlink.daos;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;

    public abstract class DaoBase {

        public Connection getConnection() throws SQLException {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            return DriverManager.getConnection("jdbc:mysql://34.23.49.79:3306/mydb", "root", "1}\"?V>Ln_YCP?DGk");
        }
    }