package utils;

import java.awt.Point;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class ImageUtils {
    //图片自适应组件的函数
    public static ImageIcon setIcon(String fileName, boolean constrained) {
        return createAutoAdjustIcon(new ImageIcon(fileName).getImage(), constrained);
    }

    /**
     * 创建一个可以自适应组件大小的ImageIcon对象
     *
     * @param image       从<code> Image </code>对象来创建ImageIcon
     * @param constrained 是否等比例缩放 。当为<code> true </code>时，可通过
     *                    {@link javax.swing.JComponent#setAlignmentX(float)}和
     *                    {@link javax.swing.JComponent#setAlignmentY(float)}方法设置组件对齐方式。
     * &#064;date  2019-11-20
     */
    public static ImageIcon createAutoAdjustIcon(Image image, boolean constrained) {
        return new ImageIcon(image) {
            @Override
            public synchronized void paintIcon(java.awt.Component cmp, Graphics g, int x, int y) {
                //初始化参数
                Point startPoint = new Point(0, 0);//默认绘制起点
                Dimension cmpSize = cmp.getSize();//获取组件大小
                Dimension imgSize = new Dimension(getIconWidth(), getIconHeight());//获取图像大小

                //计算绘制起点和区域
                if (constrained) //等比例缩放
                {
                    //计算图像宽高比例
                    double ratio = 1.0 * imgSize.width / imgSize.height;
                    //计算等比例缩放后的区域大小
                    imgSize.width = (int) Math.min(cmpSize.width, ratio * cmpSize.height);
                    imgSize.height = (int) (imgSize.width / ratio);
                    //计算绘制起点
                    startPoint.x = (int) (cmp.getAlignmentX() * (cmpSize.width - imgSize.width));
                    startPoint.y = (int) (cmp.getAlignmentY() * (cmpSize.height - imgSize.height));
                } else imgSize = cmpSize;//完全填充

                //根据起点和区域大小进行绘制
                if (getImageObserver() == null) {
                    g.drawImage(getImage(), startPoint.x, startPoint.y,
                            imgSize.width, imgSize.height, cmp);
                } else {
                    g.drawImage(getImage(), startPoint.x, startPoint.y,
                            imgSize.width, imgSize.height, getImageObserver());
                }
            }
        };
    }
}
