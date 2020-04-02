package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class ReaderListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        String fileName = e.getActionCommand();
        System.out.println(fileName + "内容如下:");
        try {
            File file = new File(fileName);
            FileReader inOne = new FileReader(file);
            BufferedReader inTwo = new BufferedReader(inOne);
            String s = null;
            while ((s = inTwo.readLine()) != null) {
                System.out.println(s);
            }
            inOne.close();
            inTwo.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

public class WindowActionEvent extends JFrame {
    JTextField text;
    ReaderListener listener;

    public WindowActionEvent() throws HeadlessException {
        init();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void init() {
        setLayout(new FlowLayout());
        text = new JTextField(10);
        listener = new ReaderListener();
        text.addActionListener(listener);
        add(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WindowActionEvent win = new WindowActionEvent();
                win.setBounds(100, 100, 310, 260);
                win.setTitle("To deal with ActionEvent");
            }
        });
    }
}
