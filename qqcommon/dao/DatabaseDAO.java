package qqcommon.dao;

import java.sql.*;

public class DatabaseDAO {
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public static Connection getConnection() throws Exception {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/users";
        String userName = "root";
        String password = "tangming666";
        Class.forName(driverName);
        return DriverManager.getConnection(url, userName, password);
    }

    public void updateSql(String sql, Object... args) {
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            for (int i = 1; i <= args.length; i++) {
                ps.setObject(i, args[i - 1]);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSql(String sql) {
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectSql(String sql) {
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            con.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectClose() {
        try {
            con.close();
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
