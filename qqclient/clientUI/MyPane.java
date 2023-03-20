package qqclient.clientUI;

import utils.ImageUtils;

import java.awt.*;
import javax.swing.*;

public class MyPane extends JPanel {
    public String file;

    public MyPane() {
    }

    @Override
    public void paintComponent(Graphics g) {
        int x = 0, y = 0;
        ImageIcon icon = ImageUtils.setIcon(file, true);
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放
    }
}
