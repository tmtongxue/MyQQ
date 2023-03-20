package qqclient.clientUI;

import qqcommon.dao.UserInfoManager;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FriendsListUI {
    private JLabel friend_jl, blackList_jl;
    private JPanel panel_1, panel_2; // 消息和联系人面板
    private final MemberPane[] friends = new MemberPane[100]; // 好友
    private final MemberPane[] blacklist = new MemberPane[100]; // 黑名单
    private static final String account = LoginUI.account;
    private String tempAcc;
    private final String imageFilePath = "images/";
    // 该账号的基本信息
    private final String name;
    private final String imageName;
    // 好友的基本信息
    public static int memberType;
    private String memberName, memberState, memberHead;
    private int friendAmount = 0, blackListAmount = 0; // 好友、黑名单成员数量
    private final int labelHeight = 28, labelWidth = 301;
    private boolean friendState = false, blacklistState = false;
    public static boolean isReceive = true, isReceiveMes, isReceiveFile;
    private final List<String> memberAccounts = new ArrayList<>();
    private static final UserInfoManager uim = new UserInfoManager();
    private final ConcurrentHashMap<String, String> account_head = uim.selectUser(1, 5);
    private final ConcurrentHashMap<String, Object> member_acc_type = uim.selectUserMember(1, 3, account);
    private final ConcurrentHashMap<String, Object> member_acc_name = uim.selectUserMember(1, 2, account);


    public FriendsListUI() {
        this.name = (String) member_acc_name.get(account);
        this.imageName = account_head.get(account);
        initialize();
        loadMember();
        blacklist[1].setPictureFrame("warning", imageFilePath + "warning.jpg", 400, 350); //设置头像弹窗
    }

    public void initialize() {
        JFrame jf = new JFrame();
        jf.getContentPane().setBackground(Color.WHITE);
        jf.setBounds(1000, 0, 320, 715);
        jf.getContentPane().setLayout(null);
        jf.setBackground(Color.WHITE);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 更新数据库
                uim.deleteOnlineUser(LoginUI.account); // 移除在线用户
                uim.modifyUserState(LoginUI.account, "[离线请留言]");
            }
        });

        // 设置标题图片
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon = toolkit.getImage(imageFilePath + "titleIcon.jpg");
        jf.setIconImage(icon);

        /*顶部窗格**/
        // 顶部背景
        MyPane topPanel = new MyPane();
        topPanel.file = imageFilePath + "QQ背景.jpg";
        topPanel.setBounds(-2, 0, 308, 115);
        jf.getContentPane().add(topPanel);
        topPanel.setLayout(null);

        // 设置头像
        JButton headImage_jbt = new JButton();
        headImage_jbt.setBounds(18, 30, 57, 60);
        headImage_jbt.setIcon(ImageUtils.setIcon(imageFilePath + imageName, true));
        topPanel.add(headImage_jbt);
        //添加鼠标事件
        headImage_jbt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        // 昵称
        JLabel name_jl = new JLabel(name);
        name_jl.setForeground(Color.RED);
        name_jl.setFont(new Font("华文楷体", Font.BOLD, 19));
        name_jl.setBounds(92, 30, 50, 22);
        topPanel.add(name_jl);

        //签名版
        JLabel label = new JLabel("我和我的牛马兄弟雄心壮志");
        label.setForeground(Color.DARK_GRAY);
        label.setBounds(88, 64, 208, 15);
        topPanel.add(label);

        //等级
        JButton btn = new JButton("Lv 49");
        btn.setFont(new Font("SimSun", Font.PLAIN, 12));
        btn.setBounds(140, 40, 32, 15);
        btn.setIcon(ImageUtils.setIcon(imageFilePath + "Lv.jpg", true));
        topPanel.add(btn);

        //搜索面板
        // 搜索框
        JTextField search_jtf = new JTextField();
        search_jtf.setBounds(28, 113, 278, 28);
        jf.getContentPane().add(search_jtf);
        search_jtf.setColumns(10);
        JButton search_btn = new JButton();
        search_btn.setBounds(-2, 113, 44 / 10 * 9 - 3, 37 / 10 * 9);
        search_btn.setIcon(ImageUtils.setIcon(imageFilePath + "search.jpg", true));
        jf.getContentPane().add(search_btn);
        /*顶部窗格完成**/

        /*中部窗格设计**/
        // 分类栏
        JTabbedPane tablePane = new JTabbedPane(JTabbedPane.TOP);
        jf.getContentPane().add(tablePane);
        tablePane.setBounds(0, 140, 306, 500);

        //消息面板
        panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        tablePane.addTab("消息",  panel_1);
        panel_1.setLayout(null);

        //联系人面板
        panel_2 = new JPanel();
        panel_2.setBackground(Color.WHITE);
        JScrollPane jsp = new JScrollPane(panel_2);
        tablePane.addTab("联系人", jsp);
        panel_2.setLayout(null);

        //我的好友栏
        friend_jl = new JLabel(" >我的好友");
        friend_jl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = friend_jl.getText();
                if (text.equals(" >我的好友")) {
                    friend_jl.setText(" ∨我的好友");
                    if (SearchFrame.isAdd) {
                        String qqNumber = SearchFrame.qqNumber;
                        String name = uim.selectUser(1, 3).get(qqNumber);
                        String state = uim.selectUserState().get(qqNumber);
                        String headImage = uim.selectUser(1, 5).get(qqNumber);
                        addMember(1, name, state, imageFilePath + headImage);
                        friends[friendAmount].account = qqNumber;
                        labelUpdate(1);
                        SearchFrame.isAdd = false;
                    } else {
                        labelUpdate(1);
                    }
                } else {
                    friend_jl.setText(" >我的好友");
                    labelUpdate(1);
                }
            }
        });
        friend_jl.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        friend_jl.setBackground(Color.LIGHT_GRAY);
        friend_jl.setBounds(0, 0, labelWidth, labelHeight);
        panel_2.add(friend_jl);

        //黑名单栏
        blackList_jl = new JLabel(" >黑名单");
        blackList_jl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = blackList_jl.getText();
                if (text.equals(" >黑名单")) {
                    blackList_jl.setText(" ∨黑名单");
                    labelUpdate(2);
                } else {
                    blackList_jl.setText(" >黑名单");
                    labelUpdate(2);
                }
            }
        });
        blackList_jl.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        blackList_jl.setBackground(Color.LIGHT_GRAY);
        blackList_jl.setBounds(0, labelHeight, labelWidth, labelHeight);
        panel_2.add(blackList_jl);
        /*中部窗格完成**/

        /*底部窗格设计**/
        // 底部容器
        MyPane bottomPane = new MyPane();
        bottomPane.file = imageFilePath + "其他软件栏.jpg";
        bottomPane.setBackground(UIManager.getColor("TextPane.background"));
        bottomPane.setBounds(0, 638, 306, 43);
        jf.getContentPane().add(bottomPane);
        bottomPane.setLayout(null);

        //添加联系人
        JButton add_jbt = new JButton();
        add_jbt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SearchFrame searchframe = new SearchFrame(500, 400);
                searchframe.setVisible(true);
            }
        });
        add_jbt.setBounds(36, 1, 32, 42);
        add_jbt.setIcon(ImageUtils.setIcon(imageFilePath + "添加联系人.jpg", true));
        bottomPane.add(add_jbt);

        jf.setVisible(true);
    }

    //更新好友栏的函数
    public void labelUpdate(int choose) { // 1是好友栏，2是黑名单
        if (choose == 1) { // 好友
            if (friendState) { // 可见
                for (int i = 1; i <= friendAmount; i++) { // 好友设置为不可见
                    friends[i].setVisible(false);
                }
                // 复原黑名单栏的位置
                blackList_jl.setBounds(0, labelHeight, labelWidth, labelHeight);
                if (blacklistState) { // 复原黑名单栏的成员
                    for (int i = 1; i <= blackListAmount; i++) {
                        blacklist[i].setBounds(0, labelHeight * 2 + MemberPane.height * (i - 1), MemberPane.width, MemberPane.height);
                        blacklist[i].setVisible(true);
                    }
                }
            } else { // 不可见
                for (int i = 2; i <= friendAmount; i++) { // 遍历，对好友列表更新
                    memberName = friends[i].friendName;
                    memberState = friends[i].label;
                    memberHead = friends[i].headImageFile;
                    String memberAcc = friends[i].account;
                    String currentState = updateState(memberAcc);
                    if (!memberState.equals(currentState)) { // 更新状态
                        updateMember(i);
                    }
                    if (friends[i].isBlacklist) { // 是黑名单成员
                        tempAcc = friends[i].account;
                        deleteMember(1, i);
                        addMember(2, memberName, "[离线请留言]", memberHead); // 添加到黑名单
                        blacklist[blackListAmount].account = tempAcc;
                    }
                    if (friends[i] != null && !friends[i].isMember) { // 不是好友
                        uim.deleteUserMember(account, memberAcc);
                        uim.deleteUserMember(memberAcc, account);
                        deleteMember(1, i); // 删除好友
                    }
                }
                for (int i = 1; i <= friendAmount; i++) { // 展开好友列表
                    friends[i].setBounds(0, labelHeight + MemberPane.height * (i - 1), MemberPane.width, MemberPane.height);
                    friends[i].setVisible(true);
                }
                // 重新放黑名单的位置
                blackList_jl.setBounds(0, labelHeight + MemberPane.height * friendAmount, labelWidth, labelHeight);
                if (blacklistState) { // 重新放黑名单栏的成员
                    for (int i = 1; i <= blackListAmount; i++) {
                        blacklist[i].setBounds(0, labelHeight * 2 + MemberPane.height * (i - 1 + friendAmount), MemberPane.width, MemberPane.height);
                    }
                }
            }
            friendState = !friendState;
        } else if (choose == 2) { // 黑名单
            if (blacklistState) {
                for (int i = 1; i <= blackListAmount; i++) { // 黑名单成员设置为不可见
                    blacklist[i].setVisible(false);
                }
            } else { // 展开黑名单
                int addition = 0;
                if (friendState) {
                    addition = friendAmount;
                }
                for (int i = 1; i <= blackListAmount; i++) {
                    memberName = blacklist[i].friendName;
                    memberState = blacklist[i].label;
                    memberHead = blacklist[i].headImageFile;
                    String memberAcc = blacklist[i].account;
                    if (!blacklist[i].isBlacklist) { // 不是黑名单
                        tempAcc = blacklist[i].account;
                        deleteMember(2, i);
                        addMember(1, memberName, memberState, memberHead); // 添加到好友
                        friends[friendAmount].account = tempAcc;
                    }
                    if (blacklist[i] != null && !blacklist[i].isMember) { // 不是好友
                        uim.deleteUserMember(account, memberAcc);
                        uim.deleteUserMember(memberAcc, account);
                        deleteMember(2, i); // 删除好友
                    }
                }
                for (int i = 1; i <= blackListAmount; i++) { // 重放黑名单成员位置
                    if (blacklist[i] != null) {
                        blacklist[i].setBounds(0, labelHeight * 2 + MemberPane.height * (i - 1 + addition), MemberPane.width, MemberPane.height);
                        blacklist[i].setVisible(true);
                    }
                }
            }
            blacklistState = !blacklistState;
        }
    }

    public void addMember(int type, String name, String state, String headImageFile) {
        if (type == 1) {  // 好友
            friendAmount++;
            friends[friendAmount] = new MemberPane(0, 0);
            friends[friendAmount].setPane(name, state, headImageFile);
            friends[friendAmount].isBlacklist = false;
            friends[friendAmount].setVisible(false);
            panel_2.add(friends[friendAmount]);
        }
        if (type == 2) { // 黑名单
            blackListAmount++;
            blacklist[blackListAmount] = new MemberPane(0, 0);
            blacklist[blackListAmount].setPane(name, state, headImageFile);
            blacklist[blackListAmount].setVisible(false);
            blacklist[blackListAmount].isBlacklist = true;
            panel_2.add(blacklist[blackListAmount]);
        }
    }

    public void deleteMember(int type, int num) { // 删除列表成员
        // 如果好友
        if (type == 1) {
            // 好友前移，补充删掉的好友
            if (friendAmount - num >= 0) System.arraycopy(friends, num + 1, friends, num, friendAmount - num);
            friends[friendAmount--] = null;

        }
        // 如果黑名单
        if (type == 2) {
            if (blackListAmount - num >= 0) System.arraycopy(blacklist, num + 1, blacklist, num, blackListAmount - num);
            blacklist[blackListAmount--] = null;
        }
    }

    public void loadMember() {
        // 加载第一个好友（自己）
        addMember(1, (String) member_acc_name.get(account), "[在线]", imageFilePath + account_head.get(account));
        friends[friendAmount].account = account;
        // 加载剩余好友
        memberAccounts.addAll(member_acc_name.keySet());
        for (String acc : memberAccounts) {
            if (!acc.equals(account)) {
                memberType = (int) member_acc_type.get(acc);
                memberName = (String) member_acc_name.get(acc);
                memberState = uim.selectUserState().get(acc);
                memberHead = account_head.get(acc);
                if (memberType == 1) {
                    addMember(memberType, memberName, memberState, imageFilePath + memberHead);
                } else {
                    addMember(memberType, memberName, "[离线请留言]", imageFilePath + memberHead);
                }
                if (memberType == 1) {
                    friends[friendAmount].account = acc;
                } else {
                    blacklist[blackListAmount].account = acc;
                }
            }
        }
    }

    public void updateMember(int index) {
        String acc = friends[index].account;
        memberType = (int) member_acc_type.get(acc);
        memberName = (String) member_acc_name.get(acc);
        memberState = uim.selectUserState().get(acc);
        memberHead = account_head.get(acc);
        tempAcc = friends[index].account;
        panel_2.remove(friends[index]);
        friends[index].setPane(memberName, memberState, imageFilePath + memberHead);
        friends[index].account = tempAcc;
        panel_2.add(friends[index]);
    }

    public String updateState(String account) {
        if (account != null && uim.selectUserState().get(account).equals("[在线]")) {
            return "[在线]";
        }
        return "[离线请留言]";
    }

    public static void messageAlert(String senderAcc) {
        int res = JOptionPane.showConfirmDialog(null, "是否查看消息？",
                uim.selectUser(1, 3).get(senderAcc) + "(" + senderAcc + ")给您发来一条新消息", JOptionPane.YES_NO_OPTION);
        isReceiveMes = res == JOptionPane.YES_OPTION;
    }

    public static void fileAlert(String senderAcc) {
        int res = JOptionPane.showConfirmDialog(null, "是否接收？",
                uim.selectUser(1, 3).get(senderAcc) + "(" + senderAcc + ")给您发来一个文件", JOptionPane.YES_NO_OPTION);
        isReceiveFile = res == JOptionPane.YES_OPTION;
    }
}

