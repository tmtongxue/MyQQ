package qqclient.clientUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PictureFrame extends JFrame {
    private final JPanel contentPane;

    public PictureFrame(String file, int width, int height) { //添加图片的构造函数
        setBounds(800, 100, width, height);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        MyPane panel = new MyPane();
        panel.file = file;
        panel.setBounds(0, 0, width - 20, height - 30);
        contentPane.add(panel);
    }
}
