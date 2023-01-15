
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

//设置主页背景图片的JPnel类
public class drawPanel extends JPanel {
    ImageIcon icon;
    Image img;

    public drawPanel(Image image) {
        img = image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (this.getWidth() - img.getWidth(null)) / 2;
        int y = (this.getHeight() - img.getHeight(null)) / 2;
        g.drawImage(img,x,y,this);
    }
}

