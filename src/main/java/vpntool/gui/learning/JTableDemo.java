package vpntool.gui.learning;

import javax.swing.*;
import java.awt.*;

public class JTableDemo {
    public static void createAndShowGUI() {
        JFrame f = new JFrame("CBA");
        f.setSize(400, 300);
        f.setLocation(200, 200);
        f.setLayout(new BorderLayout());

        String[] columnNames = new String[] {"id", "name", "team", "role"};
        String[][] players = new String[][] {{"1", "Yi", "Guangdong", "PF"}, {"2", "Zhou", "Xinjiang", "C"}};
        JTable jtb = new JTable(players, columnNames);

        JScrollPane sp = new JScrollPane(jtb);

        jtb.getColumnModel().getColumn(0).setPreferredWidth(10);

        f.add(sp, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
