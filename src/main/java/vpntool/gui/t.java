package vpntool.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class t extends JFrame{

    public t() {
        this.setTitle("C&V");
        this.setSize(300, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
//        this.setLayout(new FlowLayout());
        JPanel jPanel1 = new JPanel();
        jPanel1.add(new Label("常量："));
        JTextField constantInput = new JTextField(10);
        jPanel1.add(constantInput);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(new Label("变量："));
        JTextField variableInput = new JTextField(10);
        jPanel2.add(variableInput);
        this.add(jPanel1, BorderLayout.NORTH);
        this.add(jPanel2, BorderLayout.CENTER);
        JPanel jPanel3 = new JPanel();
        JButton jButton = new JButton("确认");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(variableInput.getText() == null){
                    System.out.println("null");
                }
                else if (variableInput.getText().equals("")){
                    System.out.println("括号");
                }
                else
                {
                    System.out.println(variableInput.getText());
                }
            }
        });
        jPanel3.add(jButton);
//        this.add(new JButton("确认"), BorderLayout.SOUTH);//这样按钮占的位置太大
        this.add(jPanel3, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            int j = 0;
//            while (j++ < 5) {
//                i++;
//                System.out.println("inner" + i);
//            }
//            System.out.println(i);
//        }

//        data d = new data();
//        data dd = d;
//        dd.setS(1000);
//        dd.setStr("hahaha");
//        System.out.println(d.s + d.str);//说明传的就是指针（引用）

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        System.out.println(screenSize.width + " " + screenSize.height);
//
//        JFrame frame = new JFrame();
//        System.out.println(frame.getForeground());
//        System.out.println(frame.getBackground());
//        Object[] a = {null, 3, "ha"};
//        String[] s = {"ha", "haha"};
        new t();
    }
}

class data {
    int s;
    String str;
    public data() {
        s = 10;
        str = "ha";
    }

    public void setS(int s) {
        this.s = s;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
