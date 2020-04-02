package vpntool.gui.learning;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class WindowMenu extends JFrame {
    JMenuBar menuBar;
    JMenu menu, subMenu;
    JMenuItem item1, item2;

    public WindowMenu() {}

    public WindowMenu(String s, int x, int y, int w, int h) {
        init(s);
        setLocation(x, y);
        setSize(w, h);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    void init(String s) {
        setTitle(s);
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");

        subMenu = new JMenu("球员");
        item1 = new JMenuItem("广东");
        ImageIcon icon = new ImageIcon("D:/PN/Tool/IdeaWorkspace/vpntool/src/main/resources/images/learning/广东.png");
//        ImageIcon icon = new ImageIcon("images/广东.png");为啥提供的相对路径不对呢？
        item1.setIcon(icon);
        item2 = new JMenuItem("新疆", new ImageIcon("D:/PN/Tool/IdeaWorkspace/vpntool/src/main/resources/images/learning/新疆.png"));

        item1.setAccelerator(KeyStroke.getKeyStroke('A'));
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menu.add(item1);
        menu.addSeparator();
        menu.add(item2);
        menu.add(subMenu);

        subMenu.add(new JMenuItem("周琦", new ImageIcon("D:/PN/Tool/IdeaWorkspace/vpntool/src/main/resources/images/learning/周琦.png")));
        subMenu.add(new JMenuItem("可兰", new ImageIcon("D:/PN/Tool/IdeaWorkspace/vpntool/src/main/resources/images/learning/可兰.png")));
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WindowMenu windowMenu = new WindowMenu("带窗口的菜单", 20, 30, 400, 500);
            }
        });
    }

}
