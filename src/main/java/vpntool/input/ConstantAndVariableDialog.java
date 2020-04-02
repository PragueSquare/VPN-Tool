package vpntool.input;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;
import vpntool.utils.parser.CommonStringMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ConstantAndVariableDialog extends JDialog {
    private JTextField constantInput, variableInput;
    private PetriNet petriNet;

    public ConstantAndVariableDialog() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        this.setTitle("常量和变量");
        this.setSize(300, 150);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        /*这里没有采用GridLayout*/
        JPanel constantPanel = new JPanel();
        constantPanel.add(new Label("Constants:"));
        constantInput = new JTextField(10);//columns应该怎么设置？？？
        constantPanel.add(constantInput);
        JPanel variablePanel = new JPanel();
        variablePanel.add(new Label("Variables:"));
        variableInput = new JTextField(10);
        variablePanel.add(variableInput);
        JPanel confirmPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        confirmPanel.add(confirmButton);
        this.add(constantPanel, BorderLayout.NORTH);
        this.add(variablePanel, BorderLayout.CENTER);
        this.add(confirmPanel, BorderLayout.SOUTH);

        /*利用匿名类实现监听机制*/
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String constantString = constantInput.getText();
                String variableString = variableInput.getText();
                if (constantString.equals("")) {//注意这里如果写constantString == null是不对的
                    JOptionPane.showMessageDialog(null, "常量不得为空", "提示", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    ArrayList<String> constantList = CommonStringMethods.stringToArrayList(constantString);
                    petriNet.setConstants(constantList);

                    /*用于处理普通token*/
                    petriNet.getConstants().add("ε");

                    // TODO: 如果没有变量还用解析吗？
                    ArrayList<String> variableList = CommonStringMethods.stringToArrayList(variableString);
                    petriNet.setVariables(variableList);

                    /*用于处理普通token*/
                    petriNet.getVariables().add("ε");

                    // TODO: 没有建立映射关系

                    dispose();

                }
            }
        });

        this.setVisible(true);
    }
}
