package vpntool.input;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;
import vpntool.utils.parser.CommonStringMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ConstraintFunctionDialog extends JDialog {
    private JTextField variableInput, constantInput;
    private PetriNet petriNet;

    public ConstraintFunctionDialog() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
//        petriNet.setMapBetweenVAndC(new HashMap<String, ArrayList<String>>());//初始化映射关系。有必要吗？？？
//
        this.setTitle("约束函数");
        this.setSize(300, 150);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        /*这里变量和常量的输入顺序和C&V对话框中的输入顺序相反，是否要统一？*/
        JPanel variablePanel = new JPanel();
        variablePanel.add(new Label("变量："));
        variableInput = new JTextField(10);
        variablePanel.add(variableInput);
        JPanel constantPanel = new JPanel();
        constantPanel.add(new Label("常量："));
        constantInput = new JTextField(10);
        constantPanel.add(constantInput);
        JButton nextMapButton = new JButton("下组映射");
        JButton finishInputButton = new JButton("结束输入");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextMapButton);
        buttonPanel.add(finishInputButton);
        this.add(constantPanel, BorderLayout.NORTH);
        this.add(variablePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        /*利用匿名类实现监听机制*/
        nextMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String variable = variableInput.getText().trim();//字符串就是一个变量，无需复杂解析，简单trim一下
                String constantString = constantInput.getText();
                if (variable.equals("") || constantString.equals("")) {
                    Object[] objects = {"OK"};
                    JOptionPane.showOptionDialog(null, "变量和常量均不能为空", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[0]);
                } else {
                    ArrayList<String> constantList = CommonStringMethods.stringToArrayList(constantString);
                    petriNet.getMapBetweenVAndC().put(variable, constantList);

                    /*清空两个文本行*/
                    variableInput.setText("");
                    constantInput.setText("");

                    dispose();
                }
            }
        });
        finishInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String variable = variableInput.getText().trim();//字符串就是一个变量，无需复杂解析，简单trim一下
                String constantString = constantInput.getText();
                if (variable.equals("") || constantString.equals("")) {
                    Object[] objects = {"OK"};
                    JOptionPane.showOptionDialog(null, "变量和常量均不能为空", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[0]);
                } else {
                    ArrayList<String> constantList = CommonStringMethods.stringToArrayList(constantString);
                    petriNet.getMapBetweenVAndC().put(variable, constantList);

                    dispose();
                }
            }
        });

        this.setVisible(true);
    }
}
