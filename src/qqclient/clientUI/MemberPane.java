package qqclient.clientUI;

import qqclient.service.ClientConnectServiceThread;
import qqcommon.dao.UserInfoManager;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MemberPane extends JPanel {
    public String account;
    public String friendName, label;
    public boolean isMember = true; // 是否为成员
    public boolean isBlacklist; // 是否黑名单
    public static int height = 70;
    public static int width = 301;
    public JLabel name_Jl; // 昵称
    public JLabel str_Jl; // 状态/消息标签
    public JButton headImage_jbt; // 头像按钮
    public String headImageFile; // 头像文件
    public PictureFrame pictureFrame;
    private final UserInfoManager uim = new UserInfoManager();

    public MemberPane(int x, int y) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { // 鼠标移进模板区，改变背景颜色；
                setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) { // 鼠标移出模板区，改变背景颜色；
                setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) { // 鼠标点击
                if (e.getClickCount() == 2) {
                    if (!isBlacklist ) {
                        ChatUI.getter_acc = account;
                        ClientConnectServiceThread.chatUI = new ChatUI();
                        FriendsListUI.isReceiveMes = true;
                    } else {
                        MessageRecordUI.account = account;
                        new MessageRecordUI();
                        try {
                            MessageRecordUI.setMesRecord();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        setBorder(null);
        this.setBounds(x, y, width, height); // 插入合适的位置
        setBackground(Color.WHITE);
        setForeground(Color.DARK_GRAY);
        setLayout(null);

        //头像
        headImage_jbt = new JButton();
        headImage_jbt.setBounds(15, 15, 50, 49);
        add(headImage_jbt);
        headImage_jbt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //测试头像弹窗
                if (pictureFrame != null) {
                    pictureFrame.setVisible(true);
                }
            }
        });

        // 鼠标右击菜单
        JPopupMenu popupMenu = new JPopupMenu();
        addPopup(this, popupMenu);

        // 删除好友
        JMenuItem delete_item = new JMenuItem("删除");
        delete_item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (account.equals(LoginUI.account)) {
                    JOptionPane.showMessageDialog(null, "你不要自己了？", "混蛋", JOptionPane.ERROR_MESSAGE);
                } else {
                    int res = JOptionPane.showConfirmDialog(null, "是否删除此人？", "提示", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION ) {
                        isMember = false;
                    }
                }
            }
        });
        delete_item.setBackground(Color.WHITE);
        popupMenu.add(delete_item);
        // 移动至黑名单
        JMenuItem moveToBlack_item = new JMenuItem("移动到黑名单");
        moveToBlack_item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isBlacklist) {
                    JOptionPane.showMessageDialog(null, "已经在黑名单中", "移动失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    int res = JOptionPane.showConfirmDialog(null, "是否移动至黑名单？", "提示", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION ) {
                        isBlacklist = true;
                        uim.modifyMemberType(LoginUI.account, account, 2);
                    }
                }
                if (account != null && account.equals(LoginUI.account)) {
                    JOptionPane.showMessageDialog(null, "想成为小黑子吗？", "鸡你太美", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        moveToBlack_item.setBackground(Color.WHITE);
        popupMenu.add(moveToBlack_item);
        // 添加至好友列表
        JMenuItem moveToFriend_item = new JMenuItem("移动到我的好友");
        moveToFriend_item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isBlacklist) {
                    JOptionPane.showMessageDialog(null, "已经在好友中", "移动失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    int res = JOptionPane.showConfirmDialog(null, "是否移动至我的好友？", "提示", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION ) {
                        isBlacklist = false;
                        uim.modifyMemberType(LoginUI.account, account, 1);
                    }
                }
            }
        });
        moveToFriend_item.setBackground(Color.WHITE);
        popupMenu.add(moveToFriend_item);
        // 发送消息
        JMenuItem sendMes_item = new JMenuItem("发送消息");
        sendMes_item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isBlacklist) {
                    ChatUI.getter_acc = account;
                    ClientConnectServiceThread.chatUI = new ChatUI();
                    FriendsListUI.isReceiveMes = true;
                } else {
                    MessageRecordUI.account = account;
                    new MessageRecordUI();
                    try {
                        MessageRecordUI.setMesRecord();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        sendMes_item.setBackground(Color.WHITE);
        popupMenu.add(sendMes_item);

        // 昵称
        name_Jl = new JLabel();
        name_Jl.setForeground(Color.RED);
        name_Jl.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        name_Jl.setBounds(75, 15, 84, 30);
        add(name_Jl);

        // 标签
        str_Jl = new JLabel();
        str_Jl.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
        str_Jl.setBounds(75, 37, 192, 20);
        add(str_Jl);
    }

    public void setData(String name, String str) { // 设置昵称和标签
        this.friendName = name;
        this.label = str;
        name_Jl.setText(name);
        name_Jl.repaint();
        str_Jl.setText(str);
        str_Jl.repaint();
    }

    public void setHeadImage(String file) { // 设置头像
        this.headImageFile = file;
        headImage_jbt.setIcon(ImageUtils.setIcon(file, true));
    }

    public void setPane(String name, String str, String headImageFile) {
        setData(name, str);
        setHeadImage(headImageFile);
    }

    public void setPictureFrame(String title, String pictureFile, int width, int height) {
        pictureFrame = new PictureFrame(pictureFile, width, height);
        pictureFrame.setTitle(title);
    }

    private static void addPopup(Component component, JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
}
