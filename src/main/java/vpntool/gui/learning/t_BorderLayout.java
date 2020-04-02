package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;

public class t_BorderLayout {
    public static void addComponentsToPane(Container pane) {
//        pane.setLayout(new BorderLayout(30,40));

        JButton button = new JButton("Button 1(PAGE_START)");
        pane.add(button, BorderLayout.PAGE_START);
        button.setBackground(Color.RED);

        button = new JButton("Button 2(CENTER)");
        button.setPreferredSize(new Dimension(500,250));
        pane.add(button, BorderLayout.CENTER);
        button.setBackground(Color.BLUE);

        button = new JButton("Button 3(LINE_START)");
        pane.add(button, BorderLayout.LINE_START);
//        button.setOpaque(false);

        button = new JButton("Button 4(LINE_END)");
        pane.add(button, BorderLayout.LINE_END);

        button = new JButton("Long-Named Button 5(PAGE_END)");
        pane.add(button, BorderLayout.PAGE_END);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("BorderLayout");
        //frame.setSize(900,500);
//        frame.setBounds(800, 0, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationRelativeTo(null);//居中
        //frame.setAlwaysOnTop(true);//置顶
        addComponentsToPane(frame.getContentPane());
        frame.pack();
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
