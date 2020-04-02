package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonDemoListener implements ActionListener {
    JTextField tf;

    public void actionPerformed(ActionEvent e) {
        String name = ((JButton)e.getSource()).getText();
        System.out.println("Button " + name + " is clicked");
    }

    ButtonDemoListener() {
        JFrame frame = new JFrame("ButtonDemo");
        frame.setSize(675,100);//怎么控制不住大小？因为pack函数，有冲突的。
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new FlowLayout());
        JButton btnUp = new JButton("Up");
        JButton btnDown = new JButton("Down");
        tf = new JTextField(20);

        frame.add(btnUp);
        frame.add(btnDown);
        frame.add(tf);

        /*监听器向自己（监听对象）注册*/
        btnUp.addActionListener(this);
        btnDown.addActionListener(this);

//        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ButtonDemoListener();
            }
        });
    }
}
