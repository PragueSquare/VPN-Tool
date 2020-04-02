package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowMessage extends JFrame implements ActionListener {

    private JTextField inputEng;
//    JTextField inputEng;
    JTextArea show;
    String regex = "[a-zA-Z]+";//正则表达式？

    public WindowMessage() {
        inputEng = new JTextField(22);
        /*对于注册监视器的文本框，在文本框获得焦点后，当用户对文本框进行操作，如按下回车键，Java运行环境就会自动用ActionEvent类
        创建一个对象，触发ActionEvent事件。对文本框进行相应的操作就会导致相应的事件发生，并通知监视器，监视器就会做出相应的处理*/
        inputEng.addActionListener(this);//为什么没有实现监听机制呢？？？因为没有按下回车。。其实实现了
        show = new JTextArea();
        add(inputEng, BorderLayout.NORTH);
        add(show, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println(3333333);
        if (e.getSource() == inputEng) {
            String str = inputEng.getText();
//            System.out.println(222222);
            if (str.matches(regex)) {
                show.append(str + ",");
//                System.out.println(1111111);
            } else {
                /*消息对话框一定依赖于JOptionPane？*/
                JOptionPane.showMessageDialog(this, "您输入了非法字符", "消息对话框", JOptionPane.WARNING_MESSAGE);
                inputEng.setText(null);
            }
        }
    }

    /*快捷输入psvm*/
    public static void main(String[] args) {
        WindowMessage win = new WindowMessage();
        win.setTitle("带消息对话框的窗口");
        win.setBounds(80, 90, 300, 300);
    }
}
