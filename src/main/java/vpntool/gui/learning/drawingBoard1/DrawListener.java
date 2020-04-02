package vpntool.gui.learning.drawingBoard1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawListener implements ActionListener, MouseListener, MouseMotionListener {
    private int x1, x2, y1, y2;
    private Graphics2D g;
    private JFrame frame;

    /*No fault？*/
    Color color;
    String name;

    public DrawListener(JFrame frame) {
        this.frame = frame;
    }

    public void setG(Graphics g) {
        this.g = (Graphics2D) g;
        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿？
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("点击的按钮是：" + e.getActionCommand());
        if (e.getActionCommand().equals("")) {
            JButton button = (JButton) e.getSource();
            color = button.getBackground();
        } else {
            name = e.getActionCommand();
        }
    }

    public void mouseDragged(MouseEvent e) {
        System.out.println("拖动");
        x2 = e.getX();
        y2 = e.getY();
        switch (name) {
            case "铅笔":
                g.setStroke(new BasicStroke(2));
                g.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
                break;

            case "刷子":
                g.setStroke(new BasicStroke(10));
                g.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
                break;

            case "橡皮":
                color = frame.getContentPane().getBackground();
                g.setColor(color);
                g.setStroke(new BasicStroke(50));
                g.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
                break;
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("点击");
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("按下");
        x1 = e.getX();
        y1 = e.getY();
        g.setColor(color);
    }

    /*这样看起来是通过press和release一笔画出来一个图形？*/
    public void mouseReleased(MouseEvent e) {
        System.out.println("释放");
        x2 = e.getX();
        y2 = e.getY();

        g.setStroke(new BasicStroke(1));
        switch(name) {
            case "直线":
                g.drawLine(x1, y1, x2, y2);
                break;

            case "矩形":
                g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                break;

            case "圆":
                g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                break;

            case "文字":
                g.drawString("Let's dance", x1, y1);
                break;
        }
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("进入");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("离开");
    }
}
