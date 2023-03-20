package qqclient.clientUI;

import qqclient.service.UserClientService;
import qqcommon.dao.UserInfoManager;
import qqcommon.output.User;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class RegisterUI {
    private JTextField name_Jtf; // 账号文本框
    private JPasswordField password_Jpf; // 密码文本框
    private JTextField securityAnswer_jtf; // 密保问题文本框
    private JLabel alert; // 错误消息提示
    private int ID = (int) (900000 * Math.random() + 10000);
    private final UserInfoManager uim = new UserInfoManager(); // 管理数据库中用户信息
    private List<String> accounts = uim.selectAccount();
    private final UserClientService ucs = new UserClientService();

    public RegisterUI() {
        initialize();
    }

    public void initialize() {
        JFrame jf = new JFrame();
        // 窗体属性
        jf.setTitle("注册");
        jf.setSize(400, 380);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 流式布局
        FlowLayout fl = new FlowLayout();
        jf.setLayout(fl);
        // 组件
        JButton btn_reset = new JButton(" 重 置 ");
        JButton btn_register = new JButton("确认注册");
        JLabel nul = new JLabel(" ");
        LoginUI.setButtonStyle(btn_reset, btn_register);
        //图片
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon = toolkit.getImage("images/titleIcon.jpg");
        jf.setIconImage(icon);
        ImageIcon img = new ImageIcon("images/qqBackground.jpg");
        JLabel img_Jl = new JLabel(img);
        JLabel name_Jl = new JLabel("昵称：");
        JLabel password_Jl = new JLabel("密码：");
        JLabel securityQuestion_Jl = new JLabel("生日：");
        //输入框
        name_Jtf = new JTextField();
        password_Jpf = new JPasswordField();
        securityAnswer_jtf = new JTextField();
        //组件(输入框)属性
        Dimension dim = new Dimension(300, 30);
        name_Jtf.setPreferredSize(dim);
        password_Jpf.setPreferredSize(dim);
        securityAnswer_jtf.setPreferredSize(dim);
        // 错误消息提示标签
        alert = new JLabel();
        alert.setForeground(Color.RED);
        //添加组件
        jf.add(img_Jl);
        jf.add(name_Jl);
        jf.add(name_Jtf);
        jf.add(password_Jl);
        jf.add(password_Jpf);
        jf.add(securityQuestion_Jl);
        jf.add(securityAnswer_jtf);
        jf.add(btn_reset);
        jf.add(nul);
        jf.add(btn_register);
        jf.add(alert);

        jf.setVisible(true); // 可视化

        // 绑定按钮点击事件
        btn_reset.addActionListener(e -> {
            name_Jtf.setText("");
            password_Jpf.setText("");
            securityAnswer_jtf.setText("");
            alert.setText("");
        });

        btn_register.addActionListener(e -> {
            String username = name_Jtf.getText().trim();
            String password = new String(password_Jpf.getPassword());
            String securityAnswer = securityAnswer_jtf.getText().trim();
            User user = new User(username, password, securityAnswer);
            if (ucs.checkUser(user)) {
                alert.setText("");
                createAccount();
                JOptionPane.showMessageDialog(null, "注册成功,您的账号为" + ID);
            } else {
                if (username.equals("") || password.equals("") || securityAnswer.equals("")) {
                    alert.setText("温馨提示:昵称、密码或生日不能为空,请重新输入。");
                }
            }
        });
    }

    public void createAccount() {
        while (accounts.contains(String.valueOf(ID))) {
            ID = (int) (900000 * Math.random() + 100000);
        }
        String username = name_Jtf.getText().trim();
        String password = new String(password_Jpf.getPassword());
        String securityAnswer = securityAnswer_jtf.getText().trim();
        uim.addUser(String.valueOf(ID), password, username, securityAnswer);
        uim.addUserState(String.valueOf(ID), "[离线请留言]");
        uim.createUser(String.valueOf(ID));
        uim.addUserMember(String.valueOf(ID), String.valueOf(ID), username, 1);
        accounts = uim.selectAccount(); // 更新账号信息
    }
}
