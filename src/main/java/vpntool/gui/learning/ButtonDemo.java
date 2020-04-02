package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonListener implements ActionListener {
    /*ActionListener中就只有一个actionPerformed方法*/
    public void actionPerformed(ActionEvent e) {
        String name = ((JButton)e.getSource()).getText();
        System.out.println("Button " + name + " is clicked");
//        if (e.getActionCommand().equals("Up")) {
//            System.out.println("Button Up is clicked");
//        } else {
//            System.out.println("Button Down is clicked");
//        }
    }
}

public class ButtonDemo {
    private ButtonListener buttonListener;
    ButtonDemo() {
        buttonListener = new ButtonListener();
        JFrame frame = new JFrame("ButtonDemo");
        frame.setSize(275,100);//怎么控制不住大小？因为pack函数，有冲突的。
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new FlowLayout());
        JButton btnUp = new JButton("Up");
        JButton btnDown = new JButton("Down");
        frame.add(btnUp);
        frame.add(btnDown);

        /*监听器向监听对象注册*/
        btnUp.addActionListener(buttonListener);
        btnDown.addActionListener(buttonListener);

//        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ButtonDemo();
            }
        });
    }
}
