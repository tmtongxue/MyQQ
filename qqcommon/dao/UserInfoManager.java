package qqcommon.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoManager {
    private final DatabaseDAO db = new DatabaseDAO();

    public void createUser(String account) {
        String sql = "create table u_" + account + "(f_account varchar(11) primary key,f_name varchar(20),f_type int)";
        db.updateSql(sql);
        db.close();
    }

    public void addUser(String account, String password, String userName, String securityAnswer) {
        String sql = "insert into user values(?,?,?,?,?)";
        String headImage = "qqLogo.jpg";
        db.updateSql(sql, account, password, userName, securityAnswer, headImage);
        db.close();
    }

    public void addUserMember(String account, String addAcc, String userName, int type) {
        String sql = "insert into u_" + account + " values(?,?,?)";
        db.updateSql(sql, addAcc, userName, type);
        db.close();
    }

    public void addOnlineUser(String account, String useName) {
        String sql = "insert into online_user values(?,?)";
        db.updateSql(sql, account, useName);
        db.close();
    }

    public void addRem_auto(String account, String password, String rem, String auto) {
        String sql = "insert into rem_auto values(?,?,?,?)";
        db.updateSql(sql, account, password, rem, auto);
        db.close();
    }

    public void addUserState(String account, String state) {
        String sql = "insert into user_state values(?,?)";
        db.updateSql(sql, account, state);
        db.close();
    }

    public void deleteUserMember(String account, String del_acc) {
        String sql = "delete from u_" + account + " where f_account=" + del_acc;
        db.updateSql(sql);
        db.close();
    }

    public void deleteOnlineUser(String account) {
        String sql = "delete from online_user where account=" + account;
        db.updateSql(sql);
        db.close();
    }

    public void deleteRem_auto() {
        String sql = "delete from rem_auto";
        db.updateSql(sql);
        db.close();
    }

    public void modifyUserState(String account, String state) {
        String sql = "update user_state set state=? where account=?";
        db.updateSql(sql, state, account);
        db.close();
    }

    public void modifyMemberType(String account, String f_acc, int type) {
        String sql = "update u_" + account + " set f_type=? where f_account=?";
        db.updateSql(sql, type, f_acc);
        db.close();
    }

    public String selectRem_auto(String columnName) {
        String sql = "select " + columnName + " from rem_auto";
        ResultSet rs = db.selectSql(sql);
        String column = null;
        try {
            while (rs.next()) {
                column = rs.getString(1);
            }
            return column;
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
        return null;
    }

    public ConcurrentHashMap<String, String> selectUser(int index1, int index2) {
        String sql = "select * from user";
        ResultSet rs = db.selectSql(sql);
        ConcurrentHashMap<String, String> user = new ConcurrentHashMap<>();
        try {
            while (rs.next()) {
                user.put(rs.getString(index1), rs.getString(index2));
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
        return user;
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

    public ConcurrentHashMap<String, String> selectUserState() {
        String sql = "select * from user_state";
        ResultSet rs = db.selectSql(sql);
        ConcurrentHashMap<String, String> userState = new ConcurrentHashMap<>();
        try {
            while (rs.next()) {
                userState.put(rs.getString(1), rs.getString(2));
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
        return userState;
    }

    public ConcurrentHashMap<String, Object> selectUserMember(int index1, int index2, String account) {
        String sql = "select * from u_" + account;
        ResultSet rs = db.selectSql(sql);
        ConcurrentHashMap<String, Object> userMember = new ConcurrentHashMap<>();
        try {
            while (rs.next()) {
                userMember.put(rs.getString(index1), rs.getObject(index2));
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
        return userMember;
    }

    public List<String> selectAccount() {
        String sql = "select account from user";
        ResultSet rs = db.selectSql(sql);
        List<String> accounts = new ArrayList<>();
        try {
            while (rs.next()) {
                accounts.add(rs.getString(1));
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
        return accounts;
    }
}
