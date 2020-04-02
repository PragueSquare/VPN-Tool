package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;
import java.util.Scanner;


public class WindowInput extends JFrame implements ActionListener {

    private JTextArea showResult;
    private JButton openInput;


    public WindowInput() {
//        setVisible(true);//把setVisible放前面也没毛病
        openInput = new JButton("弹出输入对话框");
        showResult = new JTextArea();
        openInput.addActionListener(this);
        add(openInput, BorderLayout.NORTH);
        add(new JScrollPane(showResult), BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        String str = JOptionPane.showInputDialog(this, "输入数字，用空格分隔", "输入对话框", JOptionPane.PLAIN_MESSAGE);
        if (str != null) {
            Scanner scanner = new Scanner(str);
            double sum = 0;
            int k = 0;
            while (scanner.hasNext()) {
                try {
                    double number = scanner.nextDouble();
                    if (k == 0) {
                        showResult.append("" + number);
                    } else {
                        showResult.append("+" + number);
                    }
                    sum += number;
                    k++;
                } catch (InputMismatchException exp) {
                    String t = scanner.next();
                }
            }
            showResult.append("=" + sum + "\n");
        }
    }

    /*快捷输入psvm*/
    public static void main(String[] args) {
        WindowInput win = new WindowInput();
        win.setTitle("带输入对话框的窗口");
        win.setBounds(80, 90, 300, 300);
    }
}
