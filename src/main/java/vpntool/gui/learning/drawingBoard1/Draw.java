package vpntool.gui.learning.drawingBoard1;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame{
    public static void main(String[] args) {
        Draw draw = new Draw();
        draw.onDraw();
    }

    public void onDraw() {
        JFrame frame = new JFrame();
        frame.setTitle("画板");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);//setLocationRelativeTo方法的作用？

        FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
        frame.setLayout(f1);

        Graphics g = frame.getGraphics();
        DrawListener dl = new DrawListener(frame);
        dl.setG(g);

        /*对窗体添加鼠标事件监听方法*/
        frame.addMouseListener(dl);
        frame.addMouseMotionListener(dl);

        String[] typeArray = {"直线", "矩形", "圆", "文字", "铅笔", "刷子", "橡皮", "喷枪", "长方体"};
        for (int i = 0; i < typeArray.length; i++) {
            JButton button = new JButton(typeArray[i]);
            button.setPreferredSize(new Dimension(80, 30));
            frame.add(button);
            button.addActionListener(dl);//添加动作监听方法
        }

        Color[] colorArray = {Color.RED, Color.GREEN, Color.blue};//大小写都行啊。。
        for (int i = 0; i < colorArray.length; i++) {
            JButton button = new JButton();
            button.setBackground(colorArray[i]);
            button.setPreferredSize(new Dimension(30, 30));
            frame.add(button);
            button.addActionListener(dl);//添加动作监听方法
        }

        frame.setVisible(true);

//        /*一开始在这里，后来移到上面去了*/
//        Graphics g = frame.getGraphics();//一开始这里报错，于是把Language Level由5改成8就好了
//        DrawListener dl = new DrawListener(frame);
//        dl.setG(g);
    }

}
