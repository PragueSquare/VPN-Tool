package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.models.*;
import vpntool.utils.parser.CommonStringMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class IMGeneration extends JDialog {
    private PetriNet petriNet;

    private JTable inputTable, outputTable;
    private DefaultTableModel inputTableModel, outputTableModel;

    public IMGeneration() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        this.setTitle("Incidence Matrix Generation");
        this.setSize(400, 600);

        inputTableModel = new DefaultTableModel();
        buildInputTableModel();
        inputTable = new JTable(inputTableModel);

        outputTableModel = new DefaultTableModel();
        buildOutputTableModel();
        outputTable = new JTable(outputTableModel);

        JScrollPane inputScrollPane = new JScrollPane(inputTable);
        JScrollPane outputScrollPane = new JScrollPane(outputTable);

        JSplitPane matrixSplitPane = new JSplitPane();
        matrixSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        matrixSplitPane.setDividerLocation(270);
        matrixSplitPane.setTopComponent(inputScrollPane);
        matrixSplitPane.setBottomComponent(outputScrollPane);

        this.add(matrixSplitPane);
        this.setVisible(true);
    }

    // TODO:  buildInputTableModel和buildOutputTableModel方法重复代码较多，可重构
    public void buildInputTableModel() {
        /*填充petriNet的inputMatrix*/
        ArrayList<String> constants = petriNet.getConstants();


        ArrayList<VirtualPlace> virtualPlaces = petriNet.getVirtualPlaces();
        int columnNumber = constants.size() + virtualPlaces.size();//输入矩阵总列数

        ArrayList<String> columnName = new ArrayList<>();// TODO: 可能会存到PetriNet中
//        columnName = (ArrayList<String>) constants.clone();//这里clone会不会有问题？先改成addAll
        columnName.addAll(constants);
        for (int i = constants.size(); i < columnNumber; i++) {
            columnName.add(i, virtualPlaces.get(i - constants.size()).getName());
        }

        ArrayList<Transition> transitions = petriNet.getTransitions();
        int rowNumber = transitions.size();

        ArrayList<String> rowName = new ArrayList<String>();// TODO: 可能会存到PetriNet中
        for (int i = 0; i < rowNumber; i++) {
            rowName.add(i, transitions.get(i).getName());
        }


        int[][] inputMatrix = new int[rowNumber][columnNumber];
        for (int i = 0; i < rowNumber; i++) {//inputMatrix每个位置初始化为-1
            for (int j = 0; j < columnNumber; j++) {
                inputMatrix[i][j] = -1;
            }
        }
        // TODO: 这个三重循环有点耗时吧
        for (int i = 0; i < rowNumber; i++) {//遍历变迁
            ArrayList<Arc> inputArcs = transitions.get(i).getInputArcs();
            for (int j = 0; j < inputArcs.size(); j++) {//遍历变迁所有输入弧
                Arc arc = inputArcs.get(j);
                Connectable prePlace = arc.getSource();
                for (int k = 0; k < columnNumber; k++) {//遍历列
                    if (prePlace.getName().equals(columnName.get(k))) {
                        inputMatrix[i][k] = arc.getId();


                        /*————debug————*/
//                        System.out.println("弧" + arc.getName() + "前集库所是" + prePlace.getName() + " 列名是" + columnName.get(k));
                        System.out.println("弧" + arc.getId() + "前集库所是" + prePlace.getName() + " 列名是" + columnName.get(k) + "；目标变迁是t" + (i + 1));

                    }
                }
            }
        }
        petriNet.setInputMatrix(inputMatrix);//存进去

        /*将inputMatrix中的值填入inputTableModel*/
        /*真正的输入矩阵存到网中之后再删去ε*/
        columnName.remove("ε");//ε
        inputTableModel.addColumn("Input Matrix");//建列
        for (int i = 0; i < columnNumber - 1; i++) {//ε
            inputTableModel.addColumn(columnName.get(i));
        }
        for (int i = 0; i < rowNumber; i++) {//建行
            String[] cellValues = new String[columnNumber + 1];
            cellValues[0] = rowName.get(i);
            inputTableModel.addRow(cellValues);
        }

        ArrayList<Arc> arcs = petriNet.getArcs();
        for (int i = 0; i < rowNumber; i++) {//填值
            for (int j = 0; j < columnNumber; j++) {//ε！小心这里反而不能改为-1。但也要针对处理
                if (inputMatrix[i][j] != -1) {

                    /*————debug————*/
//                    System.out.println("输入矩阵不为-1的情况");
                    /*————debug————*/

                    String cellValue = CommonStringMethods.arrayListToStringOfW(arcs.get(inputMatrix[i][j]));

                    if (j > constants.size() - 1) {
                        inputTableModel.setValueAt(cellValue, i, j);//受ε影响
                    } else {

                        /*————debug————*/
//                        System.out.println("ε之前的情况，设置" + j + "值为" + cellValue);
                        /*————debug————*/

                        inputTableModel.setValueAt(cellValue, i, j + 1);

                        System.out.println(inputTableModel.getValueAt(i, j + 1));
                    }

                    continue;
                }

                /*这是常量ε的位置。直接跳过即可*/
                if (j == constants.size() - 1) {
                    continue;
                }

                if (j > constants.size() - 1) {

                    /*————debug————*/
//                    System.out.println("ε之后-1的情况，此时j为" + j);
                    /*————debug————*/

//                    System.out.println(inputTableModel.getValueAt(i, j));
                    inputTableModel.setValueAt(0, i, j);//受ε影响

//                    System.out.println(inputTableModel.getValueAt(0, 1));
                } else {

                    /*————debug————*/
//                    System.out.println("ε之前-1的情况，此时j为" + j);
                    /*————debug————*/

//                    System.out.println(inputTableModel.getValueAt(i, j + 1));
                    inputTableModel.setValueAt(0, i, j + 1);

//                    System.out.println(inputTableModel.getValueAt(0, 1));
                }

            }
        }


    }

    public void buildOutputTableModel() {
        /*填充petriNet的outputMatrix*/

        /*————debug————*/
        System.out.println("进入buildOutputTableModel");
        /*————debug————*/

        ArrayList<String> constants = petriNet.getConstants();
        ArrayList<VirtualPlace> virtualPlaces = petriNet.getVirtualPlaces();
        int columnNumber = constants.size() + virtualPlaces.size();

        ArrayList<String> columnName;// TODO: 可能会存到PetriNet中
        columnName = (ArrayList<String>) constants.clone();
        for (int i = constants.size(); i < columnNumber; i++) {
            columnName.add(i, virtualPlaces.get(i - constants.size()).getName());
        }

        ArrayList<Transition> transitions = petriNet.getTransitions();
        int rowNumber = transitions.size();

        ArrayList<String> rowName = new ArrayList<String>();// TODO: 可能会存到PetriNet中
        for (int i = 0; i < rowNumber; i++) {
            rowName.add(i, transitions.get(i).getName());
        }


        int[][] outputMatrix = new int[rowNumber][columnNumber];
        for (int i = 0; i < rowNumber; i++) {//outputMatrix每个位置初始化为-1
            for (int j = 0; j < columnNumber; j++) {
                outputMatrix[i][j] = -1;
            }
        }
        // TODO: 这个三重循环有点耗时吧
        for (int i = 0; i < rowNumber; i++) {//遍历变迁
            ArrayList<Arc> outputArcs = transitions.get(i).getOutputArcs();
            for (int j = 0; j < outputArcs.size(); j++) {//遍历变迁所有输入弧
                Arc arc = outputArcs.get(j);
                Connectable postPlace = arc.getTarget();
                for (int k = 0; k < columnNumber; k++) {//遍历列
                    if (postPlace.getName() == columnName.get(k)) {
                        outputMatrix[i][k] = arc.getId();
                    }
                }
            }
        }
        petriNet.setOutputMatrix(outputMatrix);//存进去

        /*将outputMatrix中的值填入outputTableModel*/
        outputTableModel.addColumn("Output Matrix");//建列
        columnName.remove("ε");//ε
        for (int i = 0; i < columnNumber - 1; i++) {//ε
            outputTableModel.addColumn(columnName.get(i));
        }
        for (int i = 0; i < rowNumber; i++) {//建行
            String[] cellValues = new String[columnNumber + 1];
            cellValues[0] = rowName.get(i);
            outputTableModel.addRow(cellValues);
        }

        ArrayList<Arc> arcs = petriNet.getArcs();
        for (int i = 0; i < rowNumber; i++) {//填值
            for (int j = 0; j < columnNumber; j++) {//ε！小心这里反而不能改为-1。但也要相应处理
                if (outputMatrix[i][j] != -1) {
                    String cellValue = CommonStringMethods.arrayListToStringOfW(arcs.get(outputMatrix[i][j]));

                    /*————debug————*/
//                    System.out.println("j的值为" + j);
                    /*————debug————*/

                    if (j > constants.size() - 1) {
                        outputTableModel.setValueAt(cellValue, i, j);//受ε影响

                        /*————debug————*/
//                        System.out.println("处在ε之后，需要调整的是第" + j + "列" + columnName.get(j) + "的" + outputMatrix[i][j]);
                        /*————debug————*/

                    } else {
                        outputTableModel.setValueAt(cellValue, i, j + 1);

                        /*————debug————*/
//                        System.out.println("处在ε之前，不需要调整的是第" + j + "列" + columnName.get(j) + "的" + outputMatrix[i][j]);
                        /*————debug————*/
                    }

                    continue;
                }

                /*这是常量ε的位置。直接跳过即可*/
                if (j == constants.size() - 1) {
                    continue;
                }

                if (j > constants.size() - 1) {
                    outputTableModel.setValueAt(0, i, j);//受ε影响
                } else {
                    outputTableModel.setValueAt(0, i, j + 1);
                }
//                outputTableModel.setValueAt("0", i, j + 1);
            }

        }

    }

}
