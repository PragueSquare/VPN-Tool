package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//class Listener implements MouseListener {//是动作监听器，不是鼠标监听器
//    private TriangleArea1 ta1;
//
//    public void mouseClicked(MouseEvent e) {
//        ta1 = new TriangleArea1();
//        int answer = Integer.parseInt(ta1.getJtf1().getText()) + Integer.parseInt(ta1.getJtf2().getText()) + Integer.parseInt(ta1.getJtf3().getText());
//        ta1.getJta().setText("" + answer);
//    }
//
//    public void mouseExited(MouseEvent e) {
//
//    }
//
//    public void mouseReleased(MouseEvent e) {
//
//    }
//
//    public void mousePressed(MouseEvent e) {
//
//    }
//
//    public void mouseEntered(MouseEvent e) {
//
//    }
//}

//class Listener implements ActionListener {//改成自己做监听器
//    private TriangleArea1 ta1;
//
//    public void actionPerformed(ActionEvent e) {
//        ta1 = new TriangleArea1();
//        int answer = Integer.parseInt(ta1.getJtf1().getText()) + Integer.parseInt(ta1.getJtf2().getText()) + Integer.parseInt(ta1.getJtf3().getText());
//        ta1.getJta().setText("" + answer);
//    }
//
//}

public class TriangleArea1 extends JFrame implements ActionListener{
//    private JFrame frame;
    private JPanel panel1;
//    private JPanel panel2;//panel2一开始不显示出文本框，计算出结果后才有
    private JTextField jtf1;
    private JTextField jtf2;
    private JTextField jtf3;
    private JTextArea jta;
    private JButton btn;

//    private Listener listener;

    public TriangleArea1() {
//        frame = new JFrame("使用MVC结构");//显示问题是因为定义了frame？
//        frame.setLayout(new BorderLayout());

        panel1 = new JPanel();
        jtf1 = new JTextField(5);
        jtf2 = new JTextField(5);
        jtf3 = new JTextField(5);
        btn = new JButton("计算面积");
        panel1.add(new JLabel("边1"));
        panel1.add(jtf1);
        panel1.add(new JLabel("边2"));
        panel1.add(jtf2);
        panel1.add(new JLabel("边3"));
        panel1.add(jtf3);
        panel1.add(btn);
//        frame.add(panel1);
//        this.add(panel1);

//        panel2 = new JPanel();
        jta = new JTextArea();
//        panel2.add(jta);
//        frame.add(panel2);
//        this.add(panel2);
//        this.add(jta);

//        listener = new Listener();
//        btn.addActionListener(listener);
        btn.addActionListener(this);

        add(panel1, BorderLayout.NORTH);
        add(jta, BorderLayout.CENTER);

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);


    }

    public void actionPerformed(ActionEvent e) {
        int answer = Integer.parseInt(this.getJtf1().getText()) + Integer.parseInt(this.getJtf2().getText()) + Integer.parseInt(this.getJtf3().getText());
        this.getJta().setText("" + answer);
    }

    public JTextField getJtf1() {
        return jtf1;
    }

    public JTextField getJtf2() {
        return jtf2;
    }

    public JTextField getJtf3() {
        return jtf3;
    }

    public JTextArea getJta() {
        return jta;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TriangleArea1 t = new TriangleArea1();
                t.setTitle("使用MVC模式");
                t.setBounds(100, 100, 400, 300);
            }
        });
    }
}
