package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;

public class t_FlowLayout {
    public static void addComponentsToPane(Container pane) {

        pane.setLayout(new FlowLayout());

        pane.add(new Button("Button 1"));
        pane.add(new Button("Button 2"));
        pane.add(new Button("Button 3"));
        pane.add(new Button("Long-Named Button 4"));
        pane.add(new Button("5"));
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("FlowLayout");
        //frame.setSize(900,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationRelativeTo(null);//居中
        //frame.setAlwaysOnTop(true);//置顶
        addComponentsToPane(frame.getContentPane());
        frame.pack();//AWT的Window类提供的方法
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
