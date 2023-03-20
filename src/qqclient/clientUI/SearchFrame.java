package qqclient.clientUI;

import qqcommon.dao.UserInfoManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ConcurrentHashMap;

public class SearchFrame extends JFrame {
    private final JTextField qqNumber_jtf;
    private final String account = LoginUI.account;
    public static String qqNumber; // 用于传递QQ号的参数
    public static boolean isAdd; // 是否添加此QQ号所对用户的的标志
    private final UserInfoManager uim = new UserInfoManager();
    private final ConcurrentHashMap<String, String> account_name = uim.selectUser(1, 3);

    public SearchFrame(int width, int height) {
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel jp = new JPanel();
        jp.setBorder(new EmptyBorder(5, 5, 5, 5));
        jp.setLayout(null);

        qqNumber_jtf = new JTextField();
        qqNumber_jtf.setBounds(180, 105, 145, 27);
        jp.add(qqNumber_jtf);
        qqNumber_jtf.setColumns(10);

        JLabel titleLabel = new JLabel("查找联系人");//标题
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setBounds(224, 10, 101, 33);
        jp.add(titleLabel);

        JLabel notifyLabel = new JLabel("请输入QQ号");
        notifyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        notifyLabel.setBounds(95, 109, 87, 21);
        jp.add(notifyLabel);

        JButton searchButton = new JButton("查找");
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 将鼠标设置为手掌型
        searchButton.setBorderPainted(false); // 取消按钮的边框
        searchButton.setFocusPainted(false); // 消除按钮的焦点，即点击按钮时不出现边框
        searchButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        searchButton.setForeground(Color.DARK_GRAY);
        searchButton.setBackground(new Color(30, 144, 255));
        searchButton.setBounds(332, 105, 70, 27);
        jp.add(searchButton);
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                qqNumber = qqNumber_jtf.getText().trim();
                if (qqNumber.equals("")) {
                    JOptionPane.showMessageDialog(null, "输入QQ号不能为空。", "请重新输入QQ号。", JOptionPane.ERROR_MESSAGE);
                } else if (!account_name.containsKey(qqNumber)) { // 查询失败
                    JOptionPane.showMessageDialog(null, "查无此人。", "请重新输入QQ号。", JOptionPane.ERROR_MESSAGE);
                } else { // 查询成功
                    String name = account_name.get(qqNumber);
                    if (uim.selectUserMember(1, 2, account).containsKey(qqNumber)) {
                        JOptionPane.showMessageDialog(null, name + "(" + qqNumber + ")已经是你的好友了，不能重复添加。");
                    } else {
                        // 判断是否添加
                        int res = JOptionPane.showConfirmDialog(null, "是否添加此人？", "查找到用户 "
                                + name + "(" + qqNumber + ")", JOptionPane.YES_NO_OPTION);
                        isAdd = res == JOptionPane.YES_OPTION;
                        if (isAdd) {
                            uim.addUserMember(account, qqNumber, account_name.get(qqNumber), 1); // 添加好友
                            uim.addUserMember(qqNumber,account,account_name.get(account),1);
                        }
                    }
                }
                qqNumber_jtf.setText("");
            }
        });

        MyPane panel = new MyPane();
        panel.file = "images/searchFrame.jpg";
        panel.setBounds(0, 0, width - 15, width - 37);
        jp.add(panel);
        setContentPane(jp);
    }
}
