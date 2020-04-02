package vpntool.input;

import vpntool.gui.DrawPanel;
import vpntool.gui.PaneManager;
import vpntool.models.*;
import vpntool.utils.parser.CommonStringMethods;
import vpntool.views.ArcView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ArcLabelFunctionDialog extends JDialog {
    private PetriNet petriNet;

    private JTextField tupleText;
    private JTextField numberText;
    private StringBuilder tupleString;

//    private ArrayList<ArrayList<String>> tuplesInArcLabel;//本来想解析到每个元组的参数级别，但因为内部类的限制没有实现
    private ArrayList<String[]> tuplesInArcLabel;//每个元组是一个字符串数组，第一个元素是内容，第二个元素是数量

    public ArcLabelFunctionDialog(Connectable source, Connectable target, int sourceX, int sourceY, int targetX, int targetY, boolean isReal, DrawPanel drawPanel, Graphics2D graphics2D) {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        tupleString = new StringBuilder();//必须初始化

        int arcIndex = drawPanel.getArcIndex();
        String arcName = "f" + (arcIndex + 1);

        tuplesInArcLabel = new ArrayList<String[]>();//这里初始化必要吗？

        this.setTitle("请选择弧权值中的参数");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(new BorderLayout());//一定要显式声明边界布局
        JPanel showPanel = new JPanel();
        tupleText = new JTextField(10);
        tupleText.setEditable(false);//只能选择，不能输入
        showPanel.add(tupleText);
        JPanel choosePanel = new JPanel();
//        ArrayList<String> parameters = petriNet.getConstants();//暂时注释掉

        ArrayList<String> constants = new ArrayList<>();
        constants.addAll(petriNet.getConstants());
        constants.remove(constants.size() - 1);//不要展示普通token的常量
        int consCount = constants.size();

        int varsCount = petriNet.getVariables().size();
        ArrayList<String> parameters = new ArrayList<>(consCount + varsCount);
        for (int i = 0; i < consCount; i++) {
            parameters.add(i, constants.get(i));
        }
        for (int i = consCount; i < consCount + varsCount; i++) {
            parameters.add(i, petriNet.getVariables().get(i - consCount));
        }

        /*debug*/
        System.out.println("--------------" + parameters.size() + "--------------");
        System.out.println("--------------" + constants.size() + "--------------");

//        parameters.addAll(petriNet.getVariables());//为什么addAll这种做法会出问题？？？

        /*debug*/
        System.out.println("--------------" + petriNet.getVariables().size() + "--------------");

        for (int i = 0; i < parameters.size(); i++) {

            /*debug*/
            System.out.println("--------------第" + i + "个--------------");

            String parameter = parameters.get(i);
            JButton parameterButton = new JButton(parameter);
            parameterButton.addActionListener(new ActionListener() {//不用考虑final的问题了？
                @Override
                public void actionPerformed(ActionEvent e) {
                    tupleString = CommonStringMethods.appendParameters(tupleString, parameter);
                    tupleText.setText(tupleString.toString());

//                    tuplesInArcLabel.get(paraIndex).add(parameter);//存入列表，然后传给弧
                }
            });
            choosePanel.add(parameterButton);
        }
        // TODO: 对普通token的处理
        parameterPanel.add(showPanel, BorderLayout.NORTH);
        parameterPanel.add(choosePanel, BorderLayout.CENTER);

        JPanel numberPanel = new JPanel();
        numberPanel.add(new JLabel("请输入该类元组的数量"), BorderLayout.NORTH);
        numberText = new JTextField(10);
        numberPanel.add(numberText, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton nextTupleButton = new JButton("下个元组");
        JButton finishInputButton = new JButton("结束输入");
        nextTupleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tuple = tupleText.getText();//tupleString.toString()可以吗？
                String number = numberText.getText();
                if (tuple.equals("") || number.equals("")) {//元组和相应数量都不能为空
                    Object[] objects = {"OK"};
                    JOptionPane.showOptionDialog(null, "请先完成本元组的输入", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[0]);
                } else {
                    // TODO: 2019/12/23
//                    String formalTuple = "(" + tuple + "){" + number + "}";//后面还要解析，过程反复
                    String[] formalTuple = {tuple, number};
                    tuplesInArcLabel.add(formalTuple);

                    /*清空两个文本行*/
                    tupleText.setText("");
                    numberText.setText("");

                    /*清空stringBuilder*/
                    tupleString.setLength(0);
                }
            }
        });

        finishInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tuple = tupleText.getText();
                String number = numberText.getText();
                if ((!tuple.equals("")&&number.equals(""))||(tuple.equals("")&&!number.equals(""))) {//元组和相应数量可以都为空，但不能一个为空
                    JOptionPane.showMessageDialog(null, "请完成元组和对应数量的输入", "提示", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    /*当前有输入时，先把当前元组存进去*/
                    if (!tuple.equals("")&&!number.equals("")){
                        String[] formalTuple = {tuple, number};
                        tuplesInArcLabel.add(formalTuple);
                    }
                    //注意这里
                    Arc arc = new Arc(arcIndex, arcName, source, target, isReal, tuplesInArcLabel);
                    petriNet.getArcs().add(arcIndex, arc);

                    /*————debug————*/
                    System.out.println("新画的弧中，弧ID是" + arcIndex + "。源是" + source.getName() + "，目是" + target.getName());
                    System.out.println("弧上的元组是：");
                    for (int i = 0; i < tuplesInArcLabel.size(); i++) {
                        System.out.print(tuplesInArcLabel.get(i)[0] + ",");
                    }
                    System.out.println();
                    /*————debug————*/

                    ArcView arcView = new ArcView(arcIndex, arcName, sourceX, sourceY, targetX, targetY, isReal);
                    petriNet.getArcViews().add(arcIndex, arcView);

                    /*画弧标签*/
                    String arcLabel = CommonStringMethods.arrayListToStringOfW(arc);

                    arcView.shapeDrawing(graphics2D, arcLabel);

                    source.getOutputArcs().add(arc);
                    target.getInputArcs().add(arc);

                    drawPanel.repaint();//有必要吗？
                    dispose();
                }
            }
        });
        buttonPanel.add(nextTupleButton);
        buttonPanel.add(finishInputButton);

        this.add(numberPanel, BorderLayout.NORTH);
        this.add(parameterPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(true);

    }
}
