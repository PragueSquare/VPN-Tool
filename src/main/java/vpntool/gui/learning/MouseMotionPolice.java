package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/*这个类的作者感觉换人了*/
class WindowMouseMotion {
    JFrame myframe;
    TextArea tf;
    MouseMotionPolice mouseMotionPolice;
    JButton exitButton;

    public WindowMouseMotion() {
//        Label lable = new Label("点击或拖动鼠标");//中文显示有问题
        Label lable = new Label("Press or drag the mouse");
        myframe = new JFrame("WindowMouseMotion");
        tf = new TextArea();
        exitButton = new JButton("退出");
        mouseMotionPolice = new MouseMotionPolice(tf);//利用构造方法把被监视对象传到监视器
//        tf.addMouseMotionListener(mouseMotionPolice);//注册监视器。怎么感觉和上面重复了？？？

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        myframe.add(lable, BorderLayout.NORTH);//北是上？
        myframe.add(tf, BorderLayout.CENTER);
        myframe.add(exitButton, BorderLayout.SOUTH);
        myframe.setSize(400, 300);
//        myframe.setBounds(200, 200, 400, 300);
        myframe.setVisible(true);
    }
}

public class MouseMotionPolice implements MouseMotionListener {
    int number = 1;
    TextArea tf;

    MouseMotionPolice(TextArea tf) {
        this.tf = tf;
    }

    public void mouseDragged(MouseEvent e) {
        String s = number++ + "鼠标拖动: x = " + e.getX() + " y = " + e.getY() + "\n";
        tf.append(s);
    }

    public void mouseMoved(MouseEvent e) {
        String s = number++ + "鼠标移动: x = " + e.getX() + " y = " + e.getY() + "\n";
        tf.append(s);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WindowMouseMotion();
            }
        });
    }
}
