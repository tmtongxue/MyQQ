package qqcommon.output;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String account; // 用户账号
    private String password; // 用户密码
    private String modifiedPassword; // 修改后的密码
    private String userName; // 用户昵称
    private String securityAnswer; // 密保问题答案
    private String headImage; // 头像


    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(String userName, String password, String securityAnswer) {
        this.userName = userName;
        this.password = password;
        this.securityAnswer = securityAnswer;
    }

    public User(String account, String securityAnswer, String password, String modifiedPassword) {
        this.account = account;
        this.securityAnswer = securityAnswer;
        this.password = password;
        this.modifiedPassword = modifiedPassword;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getModifiedPassword() {
        return modifiedPassword;
    }

    public void setModifiedPassword(String modifiedPassword) {
        this.modifiedPassword = modifiedPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
}
