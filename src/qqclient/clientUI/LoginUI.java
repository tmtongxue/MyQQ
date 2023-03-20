package qqclient.clientUI;

import qqclient.service.UserClientService;
import qqcommon.dao.UserInfoManager;
import qqcommon.output.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LoginUI implements ActionListener {
    private JFrame login_jf;
    private JPanel headImage_jp; // 头像框
    private JLabel headImage_jl; // 头像图片
    private JTextField account_Jtf; // 账号文本框
    private JPasswordField password_Jpf; // 密码文本框
    private JCheckBox remPassword_jc; // 记住密码复选框
    private JCheckBox autoLogin_jc; // 自动登录复选框
    public static String account; // 账号
    private JLabel alert; // 错误消息提示
    private final String imageFilePath = "images/";
    private final UserInfoManager uim = new UserInfoManager(); // 管理数据库中用户信息
    private final ConcurrentHashMap<String, String> account_headImage = uim.selectUser(1, 5); //用户信息(账号-头像)
    private final boolean remState = Boolean.parseBoolean(uim.selectRem_auto("rem")); // 记住密码复选框状态
    private final boolean autoState = Boolean.parseBoolean(uim.selectRem_auto("auto")); // 自动登录复选框状态
    private final String auto_account = uim.selectRem_auto("account"); // 自动填充的账号
    private final String auto_password = uim.selectRem_auto("password"); // 自动填充的密码
    private final UserClientService ucs = new UserClientService();

    // 构造方法
    public LoginUI() {
        initialize();
    }

    // 创建登陆界面方法
    public void initialize() {
        login_jf = new JFrame();

        // 设置大小，位置，标题
        login_jf.setSize(410, 380);
        login_jf.setTitle("QQ");
        login_jf.setResizable(false); // 窗体大小不能改变
        login_jf.setLocationRelativeTo(null);
        login_jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建边界布局对象
        login_jf.setLayout(new BorderLayout(25, 5));
        JPanel panelTop = new MyPane();
        JPanel panelCenter = new JPanel(new GridLayout(3, 1));
        headImage_jp = new JPanel(new GridLayout(1, 1));
        JPanel panelRight = new JPanel(new GridLayout(3, 1));
        JPanel panelFooter = new JPanel(new GridLayout(2, 1));
        JPanel panelFooter1 = new JPanel(new FlowLayout());
        JPanel panelFooter2 = new JPanel(new FlowLayout());
        panelFooter.add(panelFooter1);
        panelFooter.add(panelFooter2);
        login_jf.add("North", panelTop);
        login_jf.add("Center", panelCenter);
        login_jf.add("West", headImage_jp);
        login_jf.add("East", panelRight);
        login_jf.add("South", panelFooter);

        // 插入图片
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon = toolkit.getImage("images/titleIcon.jpg");
        login_jf.setIconImage(icon);
        JLabel top = new JLabel(new ImageIcon(imageFilePath + "qqBackground.jpg"));
        panelTop.add(top);
        if (remState) {
            headImage_jl = new JLabel(new ImageIcon(imageFilePath + account_headImage.get(auto_account)));
        } else {
            headImage_jl = new JLabel(new ImageIcon("images/qqLogo.jpg"));
        }
        headImage_jp.add(headImage_jl);

        // 创建注册账号和找回密码标签
        JLabel regName = new JLabel(" 注册账号 "); // 注册账号
        JLabel findPassword = new JLabel(" 忘记密码 "); // 忘记密码
        regName.setForeground(Color.BLUE);
        findPassword.setForeground(Color.BLUE);
        regName.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 将鼠标设置为手掌型
        findPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 将鼠标设置为手掌型

        // 创建帐号和密码输入框
        account_Jtf = new JTextField(18); // 用户名
        password_Jpf = new JPasswordField(18); // 密码
        account_Jtf.setFocusable(false);
        password_Jpf.setFocusable(false);

        // 复选框部分
        remPassword_jc = new JCheckBox("记住密码 ", remState);
        autoLogin_jc = new JCheckBox("自动登录", autoState);
        JLabel nul = new JLabel(" ");
        remPassword_jc.setFocusPainted(false); // 消除按钮的焦点，即点击按钮时不出现边框
        autoLogin_jc.setFocusPainted(false); // 消除按钮的焦点，即点击按钮时不出现边框
        JPanel checkPart = new JPanel(new FlowLayout());
        checkPart.add(remPassword_jc);
        checkPart.add(nul);
        checkPart.add(autoLogin_jc);

        // 错误消息提示标签
        alert = new JLabel();
        alert.setForeground(Color.RED);

        // 创建登陆，重置按钮
        JButton reset_btn = new JButton(" 重 置 ");
        JButton login_btn = new JButton(" 登 录 ");
        setButtonStyle(reset_btn, login_btn);

        // 添加文本输入框，按钮，事件等监听对象
        panelCenter.add(account_Jtf);
        panelCenter.add(password_Jpf);
        panelCenter.add(checkPart);
        panelRight.add(regName);
        panelRight.add(findPassword);
        panelFooter1.add(reset_btn);
        panelFooter1.add(nul);
        panelFooter1.add(login_btn);
        panelFooter2.add(alert);
        account_Jtf.setName("QQ号码");
        password_Jpf.setName("密码");
        account_Jtf.addFocusListener(new JTextFieldAdapter(account_Jtf, remPassword_jc, account_Jtf.getName())); // 账号框设置提示文字
        account_Jtf.addMouseListener(new MyMouseListener(account_Jtf, password_Jpf)); // 账号密码框绑定鼠标点击事件
        password_Jpf.addFocusListener(new JPasswordFieldAdapter(password_Jpf, remPassword_jc, password_Jpf.getName())); // 密码框设置提示文字
        regName.addMouseListener(new MyMouseListener(regName)); // 注册账号标签绑定鼠标点击事件
        findPassword.addMouseListener(new MyMouseListener(findPassword)); // 找回密码标签绑定鼠标点击事件
        reset_btn.addActionListener(this); // 重置按钮绑定点击事件
        login_btn.addActionListener(this); // 登录按钮绑定点击事件

        // 绑定键盘
        account_Jtf.addKeyListener(new KeyListenerHandler());
        password_Jpf.addKeyListener(new KeyListenerHandler());
        regName.addKeyListener(new KeyListenerHandler());
        findPassword.addKeyListener(new KeyListenerHandler());
        reset_btn.addKeyListener(new KeyListenerHandler());
        login_btn.addKeyListener(new KeyListenerHandler());
        remPassword_jc.addKeyListener(new KeyListenerHandler());
        autoLogin_jc.addKeyListener(new KeyListenerHandler());
        login_jf.addKeyListener(new KeyListenerHandler());

        login_jf.setVisible(true); // 可视化
        // 自动登录
        if (autoLogin_jc.isSelected()) {
            login();
        }
    }

    // 设置按钮样式
    static void setButtonStyle(JButton reset_btn, JButton login_btn) {
        reset_btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 将鼠标设置为手掌型
        reset_btn.setBorderPainted(false); // 取消按钮的边框
        reset_btn.setFocusPainted(false); // 消除按钮的焦点，即点击按钮时不出现边框
        reset_btn.setForeground(new Color(255, 255, 255));
        reset_btn.setBackground(new Color(100, 149, 238));
        login_btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        login_btn.setBorderPainted(false);
        login_btn.setFocusPainted(false);
        login_btn.setForeground(new Color(255, 255, 255));
        login_btn.setBackground(new Color(100, 149, 238));
    }

    // 绑定键盘
    public class KeyListenerHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                login();
            }
        }
    }

    // 文本框、标签点击事件（账号、密码、注册账号、找回密码）
    public static class MyMouseListener extends MouseAdapter {
        private JTextField jtf;
        private JPasswordField jpf;
        private JLabel jl;

        public MyMouseListener(JTextField jtf, JPasswordField jpf) {
            this.jtf = jtf;
            this.jpf = jpf;
        }

        public MyMouseListener(JLabel jl) {
            this.jl = jl;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (jtf != null) {
                jtf.setFocusable(true);
                jpf.setFocusable(true);
            }
            if (jl != null) {
                if (jl.getText().trim().equals("注册账号")) {
                    new RegisterUI();
                } else if (jl.getText().trim().equals("忘记密码")) {
                    new ForgetPasswordUI();
                }
            }
        }
    }

    /**
     * 实现文本框的焦点功能，当焦点不在文本框内时，显示默认提示信息（QQ号）
     */
    public class JTextFieldAdapter extends FocusAdapter {  // JTextField提示文字通用方法
        private final JTextField text;
        private final String reminder;

        //初始化
        public JTextFieldAdapter(JTextField text, JCheckBox rem_pw, String reminder) {
            this.text = text;
            this.reminder = reminder;
            // 记住密码复选框是否勾选
            if (rem_pw.isSelected()) {
                text.setText(auto_account);
                text.setForeground(Color.BLACK);
            } else {
                text.setText(reminder);
                text.setForeground(Color.GRAY);
            }
        }

        //焦点获得
        @Override
        public void focusGained(FocusEvent e) {
            String tempString = text.getText();
            if (tempString.equals(reminder)) {
                text.setText("");
                text.setForeground(Color.BLACK);
            }
        }

        //焦点失去
        @Override
        public void focusLost(FocusEvent e) {
            String tempString = text.getText();
            if (tempString.equals("")) {
                text.setForeground(Color.GRAY);
                text.setText(reminder);
            }
            updateHeadImage(account_headImage.getOrDefault(tempString, "qqLogo.jpg"));
        }
    }

    /**
     * 实现密码框的焦点功能，当焦点不在文本框内时，显示默认提示信息（密码）
     */
    public class JPasswordFieldAdapter extends FocusAdapter {  // JTextField提示文字通用方法
        private final JPasswordField text;
        private final String reminder;

        //初始化
        public JPasswordFieldAdapter(JPasswordField text, JCheckBox rem_pw, String reminder) {
            this.text = text;
            this.reminder = reminder;
            // 记住密码复选框是否勾选
            if (rem_pw.isSelected()) {
                text.setText(auto_password);
                text.setForeground(Color.BLACK);
            } else {
                text.setEchoChar((char) (0));
                text.setText(reminder);
                text.setForeground(Color.GRAY);
            }
        }

        //焦点获得
        @Override
        public void focusGained(FocusEvent e) {
            String tempString = new String(text.getPassword());
            if (tempString.equals(reminder)) {
                text.setText("");
                text.setEchoChar('*');
                text.setForeground(Color.BLACK);
            }
        }

        //焦点失去
        @Override
        public void focusLost(FocusEvent e) {
            String tempString = new String(text.getPassword());
            if (tempString.equals("")) {
                text.setEchoChar((char) (0));
                text.setForeground(Color.GRAY);
                text.setText(reminder);
            }
        }

    }

    // 按钮点击事件（重置与登录）
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        // 重置事件
        if (str.equals(" 重 置 ")) {
            alert.setText("");
            // 重置头像
            updateHeadImage("qqLogo.jpg");
            // 重置文本框，提示
            account_Jtf.setText("");
            password_Jpf.setText("");
            remPassword_jc = new JCheckBox("", false);
            autoLogin_jc = new JCheckBox("", false);
            account_Jtf.addFocusListener(new JTextFieldAdapter(account_Jtf, remPassword_jc, account_Jtf.getName()));
            password_Jpf.addFocusListener(new JPasswordFieldAdapter(password_Jpf, remPassword_jc, password_Jpf.getName()));
        }
        // 登录事件
        if (str.equals(" 登 录 ")) {
            login();
        }
    }

    public void login() {
        account = account_Jtf.getText().trim(); // 记录账号
        String password = new String(password_Jpf.getPassword()); // 记录密码
        ConcurrentHashMap<String, String> account_password = uim.selectUser(1, 2); // 获取注册用户信息（账号-密码）
        User user = new User(account, password);
        if (ucs.checkUser(user)) {
            System.out.println(account);
            alert.setText(""); // 提示清空
            logging();
            onSuccess();
            // 更新数据库信息
            uim.modifyUserState(account, "[在线]");
            uim.addOnlineUser(account, uim.selectUser(1, 2).get(account));
            // 如果登录时记住了密码, 则记录状态, 下次登录时自动填充账号和密码
            if (remPassword_jc.isSelected()) {
                uim.deleteRem_auto();
                uim.addRem_auto(account, password, "true", "false");
            }
            // 如果勾选记住密码且自动登录
            if (remPassword_jc.isSelected() && autoLogin_jc.isSelected()) {
                uim.deleteRem_auto();
                uim.addRem_auto(account, password, "true", "true");
            }
        } else {
            if (account_password.containsKey(account)) {
                if (password.equals(account_password.get(account))) {
                    alert.setText("温馨提示:您已在QQ登录了" + account + "，不能重复登录。");
                } else if (password.equals("密码") || password.equals("")) {
                    alert.setText("温馨提示:密码不能为空,请重新输入。");
                } else {
                    alert.setText("温馨提示:密码错误,请重新输入。");
                }
            } else {
                if (account.equals("QQ号码") || account.equals("") || password.equals("密码") || password.equals("")) {
                    alert.setText("温馨提示:账号或密码不能为空,请重新输入。");
                } else {
                    alert.setText("温馨提示:帐号或密码错误,请重新输入。");
                }
            }
        }
    }

    public void logging() {
        JOptionPane.showMessageDialog(null, "登录中...");
        try {
            // 自动登录时, 登录界面的停留时间(单位：秒)
            TimeUnit.SECONDS.sleep(1); // 停留1秒
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public void onSuccess() {
        login_jf.dispose();
        new FriendsListUI();
    }

    public void updateHeadImage(String imageName) {
        headImage_jp.remove(headImage_jl);
        headImage_jl = new JLabel(new ImageIcon(imageFilePath + imageName));
        headImage_jp.add(headImage_jl);
        login_jf.revalidate();
    }
}


