package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.models.*;
import vpntool.models.analysisMethods.CTNode;
import vpntool.utils.tree.CommonJTreeMethods;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class CTShowPane extends JSplitPane implements TreeSelectionListener {
    /*数据*/
    private PetriNet petriNet;

    private Marking initialMarking;
    private ArrayList<CTNode> reachabilitySet;


//    private CTGenerationAlgo ctGenerationAlgo;
    private CTGenerationAlgo2 ctGenerationAlgo;

    /*展示*/
    //左侧
    private JScrollPane wholeTreeScrollPane;
    private JPanel wholeTreePanel;
    private JTree configurationTree;
    //右侧
    private JSplitPane detailShowPane;
    private JSplitPane nodeShowPane;

    public CTShowPane() {
        /*数据初始化*/
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        initialMarking = computeInitialMarking(petriNet);
        reachabilitySet = new ArrayList<CTNode>();

//        ctGenerationAlgo = new CTGenerationAlgo();
        ctGenerationAlgo = new CTGenerationAlgo2();

        /*界面初始化*/

        wholeTreePanel = new JPanel();
        wholeTreePanel.setBackground(Color.WHITE);
        wholeTreeScrollPane = new JScrollPane(wholeTreePanel);

        detailShowPane = new JSplitPane();
        detailShowPane.setBackground(Color.WHITE);
        detailShowPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        detailShowPane.setDividerLocation(100);
        nodeShowPane = new JSplitPane();
        nodeShowPane.setBackground(Color.WHITE);
        nodeShowPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        nodeShowPane.setDividerLocation(250);

        /*配置树根节点*/

        ArrayList<String> initialPlaceSet = new ArrayList<>();
        for (int i = 0; i < petriNet.getRealPlaces().size(); i++) {
            initialPlaceSet.add(petriNet.getRealPlaces().get(i).getName());//注意库所集中库所名是无序的
        }

        CTNode rootNode = new CTNode(0, "C0", null, initialMarking, petriNet.getMapBetweenVAndC(), initialPlaceSet, "");
        rootNode.setUserObject("C0");
        petriNet.setCtRootNode(rootNode);

        /*建树，生成可达集，同时生成左侧展示*/
        reachabilitySet = ctGenerationAlgo.createConfigurationTree(rootNode);

        /*界面设置*/
        //左侧
        configurationTree = new JTree(rootNode);
        configurationTree.addTreeSelectionListener(this);//利用自己向自己注册实现监听机制
        wholeTreePanel.add(configurationTree);
        CommonJTreeMethods.expandAllRows(configurationTree, 0, configurationTree.getRowCount());

        //右侧
        JTextArea nodeNumberArea = new JTextArea();
        JTextArea nodeContentArea = new JTextArea();
        JTextArea bindingsArea = new JTextArea();
        nodeNumberArea.setEditable(false);
        nodeContentArea.setEditable(false);
        bindingsArea.setEditable(false);
        nodeNumberArea.setBackground(Color.WHITE);
        nodeContentArea.setBackground(Color.WHITE);
        bindingsArea.setBackground(Color.WHITE);
        nodeNumberArea.setText("The total number of nodes : " + petriNet.getNodeNum());// TODO: 记录树的总结点数。而且应该是不同结点数？
        nodeContentArea.setText("Please click the configuration to get the node content");
        bindingsArea.setText("Please click the configuration to get the binding");

        nodeShowPane.setTopComponent(nodeContentArea);
        nodeShowPane.setBottomComponent(bindingsArea);
        detailShowPane.setTopComponent(nodeNumberArea);
        detailShowPane.setBottomComponent(nodeShowPane);

        //整体
        // TODO: 是不是左右要一致呢？比如都应该放在滚动面板中？
        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setDividerLocation(300);
        this.setLeftComponent(wholeTreeScrollPane);
        this.setRightComponent(detailShowPane);
    }

    public void valueChanged(TreeSelectionEvent e) {

    }

    /*注意只有实库所中才有token*/
    /*现在的处理方法是所有对虚库所的操作都要转移到其实化后的实库所中。这样就决定了标识中的元素数等于常量数。但这样如果不让虚库所占位，和矩阵行就对不上了，所以不对*/
    public Marking computeInitialMarking(PetriNet petriNet) {
        Marking initialMarking = new Marking(0);

        /*小心常量和库所的索引并不是一致的*/
        ArrayList<String> constants = petriNet.getConstants();
        ArrayList<RealPlace> realPlaces = petriNet.getRealPlaces();
        for (int i = 0; i < constants.size(); i++) {
            String constant = constants.get(i);

            boolean isPlace = false;//指示在初始时该常量是否为库所
            for (int j = 0; j < realPlaces.size(); j++) {
                RealPlace realPlace = realPlaces.get(j);
                String placeName = realPlace.getName();
                if (constant.equals(placeName)) {
                    initialMarking.getM().put(constant, realPlace.getTokens());
                    isPlace = true;
                    break;
                }
            }
            if (!isPlace) {//初始时不是库所的就存一个空的token列表
                initialMarking.getM().put(constant, new ArrayList<Token>());
            }
        }

        /*————bugFix————*/
        ArrayList<VirtualPlace> virtualPlaces = petriNet.getVirtualPlaces();
        for (int i = 0; i < virtualPlaces.size(); i++) {//虚库所存一个空的token列表，用来占位
            initialMarking.getM().put(virtualPlaces.get(i).getName(), new ArrayList<Token>());
        }
        /*————bugFix————*/


        return initialMarking;
    }
}
