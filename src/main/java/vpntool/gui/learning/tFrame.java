package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class tFrame extends JFrame {

    public tFrame() throws HeadlessException {
        setTitle("tFrame");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel jPanel = new JPanel();
        JButton jButton;
        for (int i = 0; i < 5; i++) {
//            JButton jButton = new JButton("" + i);
            jButton = new JButton("" + i);
            System.out.println("ha" + jButton.getText());
//            jButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    JOptionPane.showMessageDialog(null, "" + 1);
//                }
//            });
            jPanel.add(jButton);
//            jButton.setVisible(true);
        }

        add(jPanel);

        JButton debug = new JButton();

        setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new tFrame();
            }
        });
    }
}
