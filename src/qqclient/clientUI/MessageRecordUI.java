package qqclient.clientUI;

import qqcommon.dao.UserInfoManager;
import utils.Symbol;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageRecordUI extends JFrame {

    public static String account;
    private final UserInfoManager uim = new UserInfoManager();
    private final ConcurrentHashMap<String, String> account_name = uim.selectUser(1, 3);
    private static JTextArea showMesRecord_jta;

    public MessageRecordUI() {
        initialize();
    }

    public void initialize() {
        setTitle("和 " + account_name.get(account) + "(" + account + ") 的消息记录");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 设置标题图标
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon = toolkit.getImage("images/titleIcon.jpg");
        setIconImage(icon);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);

        showMesRecord_jta = new JTextArea();
        showMesRecord_jta.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);
        scrollPane.setViewportView(showMesRecord_jta);

        JPanel panel_1 = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel_1.setLayout(flowLayout);
        panel.add(panel_1, BorderLayout.SOUTH);

        JButton button = new JButton(" 清空记录 ");
        button.addActionListener(e -> {
            String filePath = "D:\\JavaQQData\\QQMesRecord\\";
            String fileName = LoginUI.account + "-" + account + "mesRecord.txt";
            clearInfoForFile(filePath + fileName);
        });
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        panel_1.add(button);

        setVisible(true);
    }

    public void clearInfoForFile(String fileName) {
        File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showMesRecord_jta.setText("");
    }

    public static void setMesRecord() throws IOException {
        String filePath = "D:\\JavaQQData\\QQMesRecord\\";
        String fileName = LoginUI.account + "-" + account + "mesRecord.txt";
        File file = new File(filePath + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            showMesRecord_jta.append(line + Symbol.LINE);
        }
        br.close();

    }
}
