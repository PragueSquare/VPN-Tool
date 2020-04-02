package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class WindowMouse extends JFrame{
    JTextField text;
    JButton button;
    JTextArea textArea;
    MousePolice police;

    WindowMouse() {
        init();
        setBounds(100, 100, 420, 220);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void init() {
        setLayout(new FlowLayout());
        text = new JTextField(8);
        textArea = new JTextArea(10, 28);
        police = new MousePolice();
        police.setJTextArea(textArea);
        text.addMouseListener(police);
        button = new JButton("Button");
        button.addMouseListener(police);//多个被监视对象向一个监视器注册。话说是写多个监视器比较好还是一个比较好？
        addMouseListener(police);//整个窗口作为被监视对象向监视器注册
        add(button);
        add(text);
        add(new JScrollPane(textArea));
    }
}

public class MousePolice implements MouseListener {
    JTextArea area;

    public void setJTextArea(JTextArea area) {
        this.area = area;
    }

    public void mousePressed(MouseEvent e) {
        area.append("\nMouse was pressed, where the location is:(" + e.getX() + "," + e.getY() + ")");
    }

    public void mouseReleased(MouseEvent e) {
        area.append("\nMouse was released, where the location is:(" + e.getX() + "," + e.getY() + ")");
    }

    public void mouseEntered(MouseEvent e) {
        if(e.getSource() instanceof JButton) {
            area.append("\nMouse enters the button, where the location is:(" + e.getX() + "," + e.getY() + ")");
        }
        if(e.getSource() instanceof JTextField) {
            area.append("\nMouse enters the text filed, where the location is:(" + e.getX() + "," + e.getY() + ")");
        }
        if(e.getSource() instanceof JFrame) {
            area.append("\nMouse enters the frame, where the location is:(" + e.getX() + "," + e.getY() + ")");
        }
    }

    public void mouseExited(MouseEvent e) {
        area.append("\nMouse leaves the event source, where the location is:(" + e.getX() + "," + e.getY() + ")");
    }

    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() >= 2) {
            area.setText("\nMouse was clicked twice, where the location is:(" + e.getX() + "," + e.getY() + ")");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WindowMouse win = new WindowMouse();
//                System.out.println(11111);
                win.setTitle("To deal with mouse event");
                win.setBounds(100, 100, 460, 360);
//                System.out.println(22222);
            }
        });
    }
}
