package qqclient.clientUI;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqcommon.dao.UserInfoManager;
import utils.Symbol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


public class ChatUI implements ActionListener {
    private JFrame jf;
    private JTextArea showMessage_jta;
    private JTextArea sendMessage_jta;
    private String sender_acc;
    public static String getter_acc;
    private String content;
    private String sendTime;
    private final String sender;
    private final String getter;
    public static boolean isOn;
    private final UserInfoManager uim = new UserInfoManager();
    private final ConcurrentHashMap<String, String> account_name = uim.selectUser(1, 3);

    public ChatUI() {
        sender_acc = LoginUI.account;
        sender = account_name.get(sender_acc) + "(" + sender_acc + ")";
        getter = account_name.get(getter_acc) + "(" + getter_acc + ")";
        initialize();
    }

    public ChatUI(String content, String sender_acc, String getter_acc, String sendTime) {
        sender = account_name.get(getter_acc) + "(" + getter_acc + ")";
        getter = account_name.get(sender_acc) + "(" + sender_acc + ")";
        this.content = content;
        this.sender_acc = sender_acc;
        ChatUI.getter_acc = getter_acc;
        this.sendTime = sendTime;
        initialize();
    }

    public void initialize() {
        isOn = true;
        jf = new JFrame();
        jf.setTitle(getter);
        jf.setSize(700, 550);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FriendsListUI.isReceiveMes = false;
                isOn = false;
            }
        });

        // 设置标题图标
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon = toolkit.getImage("images/titleIcon.jpg");
        jf.setIconImage(icon);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        jf.getContentPane().add(panel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setBorder(null); // 删除缺省边界
        splitPane.setOneTouchExpandable(false);
        splitPane.setOpaque(false);
        splitPane.setResizeWeight(0.7D);
        splitPane.setDividerSize(2); // 设置分割条大小
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jf.getContentPane().add(splitPane, BorderLayout.CENTER);

        // 设置文本域
        showMessage_jta = new JTextArea("");
        showMessage_jta.setEditable(false);
        showMessage_jta.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        showMessage_jta.setLineWrap(true); // 激活自动换行功能
        showMessage_jta.setWrapStyleWord(true); // 激活断行不断字功能
        showMessage_jta.setTabSize(2);
        sendMessage_jta = new JTextArea("");
        sendMessage_jta.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        sendMessage_jta.setLineWrap(true);
        sendMessage_jta.setWrapStyleWord(true);
        sendMessage_jta.setTabSize(2);

        // 上方
        JScrollPane scrollPane = new JScrollPane();
        splitPane.setTopComponent(scrollPane);
        scrollPane.setViewportView(showMessage_jta);

        // 下方
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        splitPane.setBottomComponent(panel_1);

        JPanel panel_2 = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel_2.setLayout(flowLayout);
        panel_1.add(panel_2, BorderLayout.NORTH);

        JButton button = new JButton(" 文 件 ");
        JButton button_1 = new JButton(" 消息记录 ");
        JLabel nul = new JLabel(" ");
        setButtonStyle(nul, panel_2, button, button_1);

        JPanel panel_3 = new JPanel();
        FlowLayout flowLayout_1 = new FlowLayout();
        flowLayout_1.setAlignment(FlowLayout.RIGHT);
        panel_3.setLayout(flowLayout_1);
        panel_1.add(panel_3, BorderLayout.SOUTH);

        JScrollPane scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1, BorderLayout.CENTER);
        scrollPane_1.setViewportView(sendMessage_jta);

        JButton button_2 = new JButton(" 关 闭 ");
        JButton button_3 = new JButton(" 发 送 ");
        setButtonStyle(nul, panel_3, button_2, button_3);

        jf.setVisible(true);

        // 添加按钮监听对象
        button.addMouseListener(new ShowDialog(jf));
        button_1.addActionListener(this);
        button_2.addActionListener(this);
        button_3.addActionListener(this);
        button.addKeyListener(new KeyListenerHandler());
        button_1.addKeyListener(new KeyListenerHandler());
        button_2.addKeyListener(new KeyListenerHandler());
        button_3.addKeyListener(new KeyListenerHandler());
        sendMessage_jta.addKeyListener(new KeyListenerHandler());

        // 设置窗体风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                InstantiationException e) {
            e.printStackTrace();
        }
    }


    // 设置按钮样式
    private void setButtonStyle(JLabel nul, JPanel panel, JButton button_1, JButton button_2) {
        button_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button_1.setBackground(Color.WHITE);
        button_1.setBorderPainted(false);
        button_1.setFocusPainted(false);
        button_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button_2.setBackground(Color.WHITE);
        button_2.setBorderPainted(false);
        button_2.setFocusPainted(false);
        panel.add(button_1);
        panel.add(nul);
        panel.add(button_2);
    }

    // 发消息绑定键盘回车
    public class KeyListenerHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                e.consume();
                sendMessage_jta.append(Symbol.LINE);
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                e.consume();
                sendMessage();
            }
        }
    }

    public class ShowDialog extends MouseAdapter {
        JFrame frame;

        public ShowDialog(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(frame);
            File file = chooser.getSelectedFile();
            if (file != null) {
                FriendsListUI.isReceive = (int) uim.selectUserMember(1, 3, getter_acc).get(sender_acc) == 1;
                int res = JOptionPane.showConfirmDialog(null, "是否发送该文件给对方？", "提示", JOptionPane.YES_NO_OPTION);
                if (FriendsListUI.isReceive) {
                    if (res == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "发送成功,等待对方接收。");
                        if (uim.selectOnlineUser().containsKey(getter_acc) && !sender_acc.equals(getter_acc)) {
                            FileClientService fcs = new FileClientService();
                            fcs.sendFileToOne(file, sender_acc, getter_acc);
                        } else { // 离线文件

                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "发送失败。");
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        // 消息记录
        if (str.equals(" 消息记录 ")) {
            MessageRecordUI.account = getter_acc;
            new MessageRecordUI();
            try {
                MessageRecordUI.setMesRecord();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // 关闭窗口
        if (str.equals(" 关 闭 ")) {
            jf.setVisible(false);
            isOn = false;
        }
        // 发送消息
        if (str.equals(" 发 送 ")) {
            sendMessage();
        }
    }

    public void setSender_acc(String sender_acc) {
        this.sender_acc = sender_acc;
    }

    public void setGetter_acc(String getter_acc) {
        ChatUI.getter_acc = getter_acc;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void sendMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sendTime = sdf.format(new Date());
        content = sendMessage_jta.getText();
        if (content.equals("")) {
            JOptionPane.showMessageDialog(null, "发送内容不能为空，请重新输入。");
        } else {
            // 会话框显示时间、发送者
            showMessage_jta.append("我" + Symbol.BLANK + Symbol.BLANK);
            showMessage_jta.append(sendTime + Symbol.LINE);
            showMessage_jta.append(content + Symbol.LINE);
            showMessage_jta.append(Symbol.LINE);
            // 更新发送消息区域
            sendMessage_jta.setText("");
            // 聊天记录存入文件
            writeMesRecordToFile(sender);
            // 发送消息--服务端--接收方
            FriendsListUI.isReceive = (int) uim.selectUserMember(1, 3, getter_acc).get(sender_acc) == 1;
            if (FriendsListUI.isReceive) {
                if (uim.selectOnlineUser().containsKey(getter_acc) && !sender_acc.equals(getter_acc)) { // 在线发送
                    MessageClientService mcs = new MessageClientService();
                    mcs.sendMessageToOne(content, sender_acc, getter_acc, sendTime);
                } else { // 离线留言

                }
            }
        }
    }

    public void receiveMessage() {
        if (!content.equals("")) {
            showMessage_jta.append(getter + Symbol.BLANK + Symbol.BLANK);
            showMessage_jta.append(sendTime + Symbol.LINE);
            showMessage_jta.append(content + Symbol.LINE);
            showMessage_jta.append(Symbol.LINE);
            String temp = sender_acc;
            sender_acc = getter_acc;
            getter_acc = temp;
            writeMesRecordToFile(account_name.get(getter_acc) + "(" + getter_acc + ")");
        }
    }

    public void writeMesRecordToFile(String sender) {
        try {
            String filePath = "D:\\JavaQQData\\QQMesRecord\\";
            String fileName = sender_acc + "-" + getter_acc + "mesRecord.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath + fileName, true));
            bw.append(sender).append(Symbol.BLANK).append(sendTime).append(Symbol.LINE);
            bw.append(content).append(Symbol.LINE);
            bw.append(Symbol.LINE);
            bw.flush();
            if (!isOn) {
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
