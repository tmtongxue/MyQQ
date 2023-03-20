package qqcommon.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoManager {
    private final DatabaseDAO db = new DatabaseDAO();

    public void deleteOnlineUser(String account) {
        String sql = "delete from online_user where account=" + account;
        db.updateSql(sql);
        db.close();
    }

    public void modifyPassword(String password, String account) {
        String sql = "update user set password=? where account=?";
        db.updateSql(sql, password, account);
        db.close();
    }

    public ConcurrentHashMap<String, String> selectUser(int index1, int index2) {
        String sql = "select * from user";
        ResultSet rs = db.selectSql(sql);
        ConcurrentHashMap<String, String> account_password = new ConcurrentHashMap<>();
        try {
            while (rs.next()) {
                account_password.put(rs.getString(index1), rs.getString(index2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.selectClose();
        }
        return account_password;
    }

    public ConcurrentHashMap<String, String> selectOnlineUser() {
        String sql = "select * from online_user";
        ResultSet rs = db.selectSql(sql);
        ConcurrentHashMap<String, String> onlineUser = new ConcurrentHashMap<>();
        try {
            while (rs.next()) {
                onlineUser.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.selectClose();
        }
        return onlineUser;
    }
}
