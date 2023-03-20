package qqclient.clientUI;

import qqclient.service.UserClientService;
import qqcommon.dao.UserInfoManager;
import qqcommon.output.User;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;

public class ForgetPasswordUI {
    private JTextField account_Jtf; // 账号文本框
    private JTextField securityAnswer_jtf; // 密保问题文本框
    private JPasswordField password_Jpf; // 密码文本框
    private JPasswordField modifyPassword_Jpf; // 修改密码文本框
    private JLabel alert; // 错误消息提示
    private String account; // 账号
    private String securityAnswer; // 密保答案
    private String password; // 密码
    private String modifiedPassword; // 修改后密码
    private final UserInfoManager uim = new UserInfoManager(); // 管理数据库中用户信息
    private ConcurrentHashMap<String, String> account_password = uim.selectUser(1, 2); // 用户信息(账号-密码)
    private User user;
    private final UserClientService ucs = new UserClientService();


    public ForgetPasswordUI() {
        initialize();
    }

    public void initialize() {
        JFrame jf = new JFrame();
        // 窗体属性
        jf.setTitle("忘记密码");
        jf.setSize(400, 420);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 流式布局
        FlowLayout fl = new FlowLayout();
        jf.setLayout(fl);
        // 组件
        JButton btn_modify = new JButton(" 修改密码 ");
        JButton btn_findPassword = new JButton(" 找回密码 ");
        JLabel nul = new JLabel(" ");
        LoginUI.setButtonStyle(btn_modify, btn_findPassword);
        //图片
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon = toolkit.getImage("images/titleIcon.jpg");
        jf.setIconImage(icon);
        ImageIcon img = new ImageIcon("images/qqBackground.jpg");
        JLabel img_Jl = new JLabel(img);
        JLabel account_Jl = new JLabel("  QQ号码：");
        JLabel securityAnswer_Jl = new JLabel("您的生日：");
        JLabel password_Jl = new JLabel("原来密码：");
        JLabel modifyPassword_Jl = new JLabel("修改密码：");
        //输入框
        account_Jtf = new JTextField();
        securityAnswer_jtf = new JTextField();
        password_Jpf = new JPasswordField();
        modifyPassword_Jpf = new JPasswordField();
        //组件(输入框)属性
        Dimension dim = new Dimension(300, 30);
        account_Jtf.setPreferredSize(dim);
        securityAnswer_jtf.setPreferredSize(dim);
        password_Jpf.setPreferredSize(dim);
        modifyPassword_Jpf.setPreferredSize(dim);
        // 错误消息提示标签
        alert = new JLabel();
        alert.setForeground(Color.RED);
        //添加组件
        jf.add(img_Jl);
        jf.add(account_Jl);
        jf.add(account_Jtf);
        jf.add(securityAnswer_Jl);
        jf.add(securityAnswer_jtf);
        jf.add(password_Jl);
        jf.add(password_Jpf);
        jf.add(modifyPassword_Jl);
        jf.add(modifyPassword_Jpf);
        jf.add(btn_modify);
        jf.add(nul);
        jf.add(btn_findPassword);
        jf.add(alert);
        jf.setVisible(true); // 可视化

        // 绑定按钮点击事件
        btn_modify.addActionListener(e -> { // 修改密码
            account = account_Jtf.getText().trim();
            password = new String(password_Jpf.getPassword());
            modifiedPassword = new String(modifyPassword_Jpf.getPassword());
            user = new User(account, "null", password, modifiedPassword);
            if (ucs.checkUser(user)) {
                alert.setText("");
                JOptionPane.showMessageDialog(null, "修改密码成功！");
            } else if (account.equals("") || password.equals("") || modifiedPassword.equals("")) {
                alert.setText("温馨提示:账号、密码或修改密码不能为空,请重新输入。");
            } else {
                alert.setText("温馨提示:账号或密码错误,请重新输入。");
            }
        });

        btn_findPassword.addActionListener(e -> { // 找回密码
            account = account_Jtf.getText().trim();
            securityAnswer = securityAnswer_jtf.getText().trim();
            user = new User(account, securityAnswer, null, null);
            if (ucs.checkUser(user)) {
                alert.setText("");
                account_password = uim.selectUser(1, 2);
                String password = account_password.get(account);
                JOptionPane.showMessageDialog(null, "找回密码成功,您的密码为" + password);
            } else if (account.equals("") || securityAnswer.equals("")) {
                alert.setText("温馨提示:账号或生日（密保问题）不能为空,请重新输入。");
            } else {
                alert.setText("温馨提示:账号或生日（密保问题）错误,请重新输入。");
            }
        });
    }
}
