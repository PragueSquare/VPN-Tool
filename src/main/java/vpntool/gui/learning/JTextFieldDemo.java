package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*就像一个带界面的计算器*/
public class JTextFieldDemo implements ActionListener {

    JTextField leftJtf;
    JTextField rightJtf;
    JLabel resultJlb;

    JTextFieldDemo() {
        JFrame frame = new JFrame("JTextFieldDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new FlowLayout());

        leftJtf = new JTextField(5);
        rightJtf = new JTextField(5);

        leftJtf.setActionCommand("leftJtf");
        leftJtf.addActionListener(this);
        rightJtf.setActionCommand("rightJtf");
        rightJtf.addActionListener(this);

        JButton jBtn = new JButton("=");
        jBtn.addActionListener(this);

        resultJlb = new JLabel();

        frame.add(leftJtf);
        frame.add(new JLabel("+"));
        frame.add(rightJtf);
        frame.add(jBtn);
        frame.add(resultJlb);

        frame.setSize(275,100);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("=")) {
            resultJlb.setText(String.valueOf(Integer.parseInt(leftJtf.getText()) + Integer.parseInt(rightJtf.getText())));
        }else if(e.getActionCommand().equals("leftJtf")) {
            leftJtf.setText("");
        }else if(e.getActionCommand().equals("rightJtf")) {
            rightJtf.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JTextFieldDemo();
            }
        });
    }
}
