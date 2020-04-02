package vpntool.input;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LinkFunctionDialog extends JDialog {
    private JTextField booleanExpressionInput;
    private JTextField variableInput;
    private JTextField operationInput;
    private PetriNet petriNet;

    private ArrayList<String[]> linkFunction;

    public LinkFunctionDialog(int transitionIndex) {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
        linkFunction = new ArrayList<>();//这一步必要吗？

        this.setTitle("变迁的连接函数");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // TODO: 为啥布局还是控不住？
        JPanel inputPanel = new JPanel();
        JPanel boolExprPanel = new JPanel();
        booleanExpressionInput = new JTextField(20);//列数设置合适吗？
        boolExprPanel.add(new Label("Bool Expression : "));
        boolExprPanel.add(booleanExpressionInput);
        JPanel varPanel = new JPanel();
        variableInput = new JTextField(10);//列数设置合适吗？
        varPanel.add(new Label("Variable : "));
        varPanel.add(variableInput);
        JPanel operPanel = new JPanel();
        operationInput = new JTextField(10);//列数设置合适吗？
        operPanel.add(new Label("Operation : "));
        operPanel.add(operationInput);
        inputPanel.add(boolExprPanel, BorderLayout.NORTH);
        inputPanel.add(varPanel, BorderLayout.CENTER);
        inputPanel.add(operPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton nextConstraintButton = new JButton("下个约束");
        JButton finishInputButton = new JButton("结束输入");
        buttonPanel.add(nextConstraintButton);
        buttonPanel.add(finishInputButton);

        this.add(inputPanel, BorderLayout.CENTER);//NORTH还是CENTER好？NORTH面积太小了？CENTER也小
        this.add(buttonPanel, BorderLayout.SOUTH);

        /*利用匿名类实现监听机制*/
        nextConstraintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String booleanExpression = booleanExpressionInput.getText();
                String variable = variableInput.getText();
                String operation = operationInput.getText();
                if (booleanExpression.trim().equals("")) {//要想继续输入则布尔表达式必不为空
                    Object[] objects = {"OK"};
                    JOptionPane.showOptionDialog(null, "请完成布尔表达式的输入", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[0]);
                } else if ((variable.trim().equals("")&&(!operation.trim().equals("")))||(!variable.trim().equals("")&&(operation.trim().equals("")))) {//变量和操作要么同时为空，要么同时不空
                    Object[] objects = {"OK"};
                    JOptionPane.showOptionDialog(null, "请完成变量和对应操作的输入", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[0]);
                } else {
                    String[] link = {booleanExpression, variable, operation};
                    linkFunction.add(link);

                    /*————debug————*/
                    System.out.println("连接函数加入一条映射");
                    /*————debug————*/

                    /*清空三个文本行*/
                    booleanExpressionInput.setText("");
                    variableInput.setText("");
                    operationInput.setText("");

                }

            }
        });

        finishInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String booleanExpression = booleanExpressionInput.getText();
                String variable = variableInput.getText();
                String operation = operationInput.getText();
                if ((booleanExpression.trim().equals(""))&&(!variable.trim().equals("")||!operation.trim().equals(""))) {//当布尔表达式为空时，后续操作都必须为空
                    JOptionPane.showMessageDialog(null, "请完成布尔表达式的输入", "提示", JOptionPane.WARNING_MESSAGE, null);
                } else if ((variable.trim().equals("")&&(!operation.trim().equals("")))||(!variable.trim().equals("")&&(operation.trim().equals("")))) {//变量和操作要么同时为空，要么同时不空
                    JOptionPane.showMessageDialog(null, "请完成变量和对应操作的输入", "提示", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    /*当前有输入时，先把当前约束存进去*/
                    if (!booleanExpression.equals("")){//注意此时可能有空操作
                        String[] link = {booleanExpression, variable, operation};
                        linkFunction.add(link);

                        /*————debug————*/
                        System.out.println("连接函数加入一条映射");
                        /*————debug————*/

                    }

                    //注意这里
                    petriNet.getTransitions().get(transitionIndex).setLinkFunction(linkFunction);

//                    drawPanel.repaint();//不用重画吧？
                    dispose();
                }
            }
        });

        this.setVisible(true);
    }
}
