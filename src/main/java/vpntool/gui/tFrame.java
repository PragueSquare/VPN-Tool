package vpntool.gui;

import javax.swing.*;
import java.awt.*;

public class tFrame extends JFrame {

    public tFrame() {
        JPanel outerPanel = new JPanel();
        this.add(outerPanel);
        outerPanel.setLayout(new BorderLayout());

//        /*为什么边界布局直接用于JFrame可以，用到JPanel就不行了呢？应该是因为JFrame默认布局就是边界布局，而JPanel不是，需要手动设置*/
//        this.add(new JLabel("up"), BorderLayout.NORTH);
//        this.add(new JLabel("middle"), BorderLayout.CENTER);
//        this.add(new JLabel("down"), BorderLayout.SOUTH);

        JPanel upPanel = new JPanel();
//        upPanel.setSize(200,200);
        JPanel middlePanel = new JPanel();
        JPanel downPanel = new JPanel();

        upPanel.setPreferredSize(new Dimension(800, 200));
        middlePanel.setPreferredSize(new Dimension(800, 400));
        downPanel.setPreferredSize(new Dimension(800, 200));

//        JPanel innerPanel = new JPanel();
//        outerPanel.add(innerPanel);

        outerPanel.add(upPanel, BorderLayout.NORTH);
        outerPanel.add(middlePanel, BorderLayout.CENTER);
        outerPanel.add(downPanel, BorderLayout.SOUTH);

//        innerPanel.setLayout(new BorderLayout());
//
//
//        innerPanel.add(upPanel, BorderLayout.NORTH);
//        innerPanel.add(middlePanel, BorderLayout.CENTER);
//        innerPanel.add(downPanel, BorderLayout.SOUTH);



        upPanel.add(new JLabel("up"));
        middlePanel.add(new JLabel("middle"));
        downPanel.add(new JLabel("down"));

        this.setVisible(true);
        this.setSize(800, 800);
    }

    public static void main(String[] args) {
        new tFrame();
    }

}
