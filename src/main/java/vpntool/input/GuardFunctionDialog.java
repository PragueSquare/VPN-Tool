package vpntool.input;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuardFunctionDialog extends JDialog {
    private JTextField guardFunctionInput;
    private PetriNet petriNet;

    public GuardFunctionDialog(int transitionIndex) {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        this.setTitle("变迁的守卫函数");
        this.setSize(600, 150);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new Label("Guard Function："));
        guardFunctionInput = new JTextField(20);//列数设置合适吗？
        inputPanel.add(guardFunctionInput);
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        buttonPanel.add(confirmButton);
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);

        /*利用匿名类实现监听机制*/
        /*守卫函数是可以为空的*/
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guardFunction = guardFunctionInput.getText();
                petriNet.getTransitions().get(transitionIndex).setGuardFunction(guardFunction);

                dispose();
            }
        });

        this.setVisible(true);
    }
}
