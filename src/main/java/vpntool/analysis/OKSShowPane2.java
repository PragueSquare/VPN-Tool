package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.models.*;
import vpntool.models.analysisMethods.KSNode;
import vpntool.models.analysisMethods.kripkeStructure.APlTerm;
import vpntool.models.analysisMethods.kripkeStructure.APmTerm;
import vpntool.models.analysisMethods.kripkeStructure.APtTerm;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OKSShowPane2 extends JSplitPane implements TreeSelectionListener {
    /*数据*/
    private PetriNet petriNet;

    private Marking initialMarking;
    private ArrayList<KSNode> reachabilitySet;


//    private CTGenerationAlgo ctGenerationAlgo;
    private KSGenerationAlgo ksGenerationAlgo;

    /*展示*/
    //左侧
    private JScrollPane wholeTreeScrollPane;
    private JPanel wholeTreePanel;
    private JTree configurationTree;
    //右侧
    private JSplitPane detailShowPane;
    private JSplitPane nodeShowPane;

    public OKSShowPane2() {
        /*数据初始化*/
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        initialMarking = computeInitialMarking(petriNet);
        reachabilitySet = new ArrayList<KSNode>();

        ksGenerationAlgo = new KSGenerationAlgo();

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

        ArrayList<String> APpOfRootNode = new ArrayList<>();
        ArrayList<APlTerm> APlOfRootNode = new ArrayList<>();
        KSNode rootNode = new KSNode(0, "C0", null, initialMarking, petriNet.getMapBetweenVAndC(), initialPlaceSet, "", APpOfRootNode, APlOfRootNode);
        rootNode.setUserObject("C0");
        petriNet.setKsRootNode(rootNode);

        /*建树，生成可达集，同时生成左侧展示*/
        reachabilitySet = ksGenerationAlgo.createKripkeStructure(rootNode);

        /*生成smv文件*/
        createSMVFileForOKS(reachabilitySet);



//        /*————debug————*/
//        TreeNode[] preNodes = reachabilitySet.get(reachabilitySet.size() - 1).getPath();
//        System.out.println("利用JTree得到前面的状态数为：" + preNodes.length);
//        KSNode testJTree = (KSNode) preNodes[7];
//        System.out.println(((KSNode)(testJTree.getParent())).getChildren().size());
//        /*————debug————*/
//
//
//        /*界面设置*/
//        //左侧
//        configurationTree = new JTree(rootNode);
//        configurationTree.addTreeSelectionListener(this);//利用自己向自己注册实现监听机制
//        wholeTreePanel.add(configurationTree);
//        CommonJTreeMethods.expandAllRows(configurationTree, 0, configurationTree.getRowCount());
//
//        //右侧
//        JTextArea nodeNumberArea = new JTextArea();
//        JTextArea nodeContentArea = new JTextArea();
//        JTextArea bindingsArea = new JTextArea();
//        nodeNumberArea.setEditable(false);
//        nodeContentArea.setEditable(false);
//        bindingsArea.setEditable(false);
//        nodeNumberArea.setBackground(Color.WHITE);
//        nodeContentArea.setBackground(Color.WHITE);
//        bindingsArea.setBackground(Color.WHITE);
//        nodeNumberArea.setText("The total number of nodes : " + petriNet.getNodeNum());// TODO: 记录树的总结点数。而且应该是不同结点数？
//        nodeContentArea.setText("Please click the configuration to get the node content");
//        bindingsArea.setText("Please click the configuration to get the binding");
//
//        nodeShowPane.setTopComponent(nodeContentArea);
//        nodeShowPane.setBottomComponent(bindingsArea);
//        detailShowPane.setTopComponent(nodeNumberArea);
//        detailShowPane.setBottomComponent(nodeShowPane);
//
//        //整体
//        // TODO: 是不是左右要一致呢？比如都应该放在滚动面板中？
//        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
//        this.setDividerLocation(300);
//        this.setLeftComponent(wholeTreeScrollPane);
//        this.setRightComponent(detailShowPane);
//
//        /*生成smv文件*/
//        createSMVFile(reachabilitySet);

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

//    private void createSMVFile(ArrayList<KSNode> reachabilitySet) {
//        int reachabilitySetSize = reachabilitySet.size();
//
//        String res = "\nMODULE main\n\n";
//
//        /*VAR*/
//        String varPart = "VAR\n\nstate : {";
//        for (int i = 0; i < reachabilitySetSize - 1; i++) {
//            varPart += reachabilitySet.get(i).getUserObject() + ", ";
//        }
//        varPart += reachabilitySet.get(reachabilitySet.size() - 1).getUserObject() + "};\n\n";
//
//        /*ASSIGN*/
//        String assignPart = "ASSIGN\n\ninit(state) := C0;\nnext(state) :=\n\ncase\n";//这里初始状态硬编码为C0合适吗？
//        for (int i = 0; i < reachabilitySetSize; i++) {
//            KSNode node = reachabilitySet.get(i);
//            String oneAssignTerm = "state = " + node.getUserObject() + "          : {";//初步设置十个空格
//            ArrayList<KSNode> children = node.getChildren();
//            for (int j = 0; j < children.size() - 1; j++) {
//                oneAssignTerm += children.get(j).getUserObject() + ", ";
//            }
//            oneAssignTerm += children.get(children.size() - 1) + "};\n";
//
//            assignPart += oneAssignTerm;
//        }
//        assignPart += "esac;\n\n";
//
//        /*DEFINE*/
//        String definePart = "DEFINE\n\n";
//
//        /*APm*/
//        // TODO: 好像token不太好展示吧？其实就是命名的问题嘛。还有到底应该展示token还是标识呢？
//        ArrayList<APmTerm> APmList = new ArrayList<>();
//        for (int i = 0; i < reachabilitySetSize; i++) {
//            Marking marking = reachabilitySet.get(i).getMarking();
//            HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(marking);
//            for (String place : MAfterIntegration.keySet()) {
//                HashMap<Token, Integer> tokenWithNum = MAfterIntegration.get(place);
//                for (Token token : tokenWithNum.keySet()) {
//                    APmTerm aPmTerm = new APmTerm(place, token, tokenWithNum.get(token));
//                    if (!APmList.contains(aPmTerm)) {
//                        APmList.add(aPmTerm);
//                    }
//                }
//            }
//
//        }
//
//        // TODO: 对APmList排序？相同库所放一起，相同token放一起，然后按数量排序？
//
//        //这里把计算和展示一起做了合适吗？
//        String APmPart = "\n";
//        for (int i = 0; i < APmList.size(); i++) {
//            APmTerm oneAPmTerm = APmList.get(i);
//            String place = oneAPmTerm.getPlace();
//            Token token = oneAPmTerm.getToken();
//            ArrayList<String> cons = token.getConstants();
//            int num = oneAPmTerm.getNum();
//            String oneAPmTermStr = place + "_";
//            for (int j = 0; j < cons.size(); j++) {
//                oneAPmTermStr += cons.get(j) + "_";
//            }
//            oneAPmTermStr += num + "          := state in {";
//
//            ArrayList<String> states = new ArrayList<>();//存储所有标识中含有该条目的状态
//            for (int j = 0; j < reachabilitySetSize; j++) {
//                Marking marking = reachabilitySet.get(j).getMarking();
//                HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(marking);
//                if (isIncludedInAPm(MAfterIntegration, oneAPmTerm)) {
//                    states.add((String) reachabilitySet.get(j).getUserObject());
//                }
//            }
//
//            for (int j = 0; j < states.size() - 1; j++) {
//                oneAPmTermStr += states.get(j) + ", ";
//            }
//            oneAPmTermStr += states.get(states.size() - 1) + "};\n";
//
//            APmPart += oneAPmTermStr;
//
//        }
//
//
////        /*APp*/
////        //首先得到一个列表存储APp中所有元素
////        ArrayList<ArrayList<String>> APpList = new ArrayList<>();
////        for (int i = 0; i < reachabilitySetSize; i++) {
////            ArrayList<String> oneAPpTerm = reachabilitySet.get(i).getAPp();
////
////            boolean alreadyExisting = false;
////            for (int j = 0; j < APpList.size(); j++) {
////                ArrayList<String> APp_j = reachabilitySet.get(j).getAPp();
////                if (!oneAPpTerm.containsAll(APp_j) || !APp_j.containsAll(oneAPpTerm)) {
////                    //这样是为了利用短路提高效率
////                } else {
////                    alreadyExisting = true;
////                    break;
////                }
////            }
////            if (!alreadyExisting) {
////                APpList.add(oneAPpTerm);
////            }
////        }
////
////        //这里把计算和展示一起做了合适吗？
////        String APpPart = "";
////        for (int i = 0; i < APpList.size(); i++) {
////            String oneAPpTermStr = "";
////            ArrayList<String> oneAPpTerm = APpList.get(i);
////            for (int j = 0; j < oneAPpTerm.size() - 1; j++) {
////                oneAPpTermStr += oneAPpTerm.get(j) + "_";
////            }
////            if (oneAPpTerm.size() != 0) {
////                oneAPpTermStr += oneAPpTerm.get(oneAPpTerm.size() - 1) + "          := state in {";//初步设置十个空格
////            } else {
////                oneAPpTermStr += "APpIsNull          := state in {";// TODO: APp为空时还用展示吗？
////            }
////
////
////            ArrayList<String> states = new ArrayList<>();//存储所有APp(x)=oneAPpTerm的状态
////            for (int j = 0; j < reachabilitySetSize; j++) {
////                ArrayList<String> APpTermToCompareWith = reachabilitySet.get(j).getAPp();
////                if (!oneAPpTerm.containsAll(APpTermToCompareWith) || !APpTermToCompareWith.containsAll(oneAPpTerm)) {
////                    //这样是为了利用短路提高效率
////                } else {
////                    states.add((String) reachabilitySet.get(j).getUserObject());
////                }
////            }
////
////            for (int j = 0; j < states.size() - 1; j++) {
////                oneAPpTermStr += states.get(j) + ", ";
////            }
////            oneAPpTermStr += states.get(states.size() - 1) + "};\n";
////
////            APpPart += oneAPpTermStr;
////        }
//
//        /*APp*/
//
//        //首先得到一个列表存储APp中所有元素，每个新增库所是一个元素
//        ArrayList<String> APpList = new ArrayList<>();
//        for (int i = 0; i < reachabilitySetSize; i++) {
//            ArrayList<String> oneAPpTerm = reachabilitySet.get(i).getAPp();
//
//            for (int j = 0; j < oneAPpTerm.size(); j++) {
//                String oneNewPlace = oneAPpTerm.get(j);
//                if (!APpList.contains(oneNewPlace)) {
//                    APpList.add(oneNewPlace);
//                }
//            }
//
//        }
//
//        //这里把计算和展示一起做了合适吗？主要是想循环一次完成以提高效率
//        String APpPart = "\n";
//        for (int i = 0; i < APpList.size(); i++) {
//            String oneNewPlaceStr = "";
//
//            String oneNewPlace = APpList.get(i);
//            oneNewPlaceStr += oneNewPlace + "_add          := state in {";
//
//
//            ArrayList<String> states = new ArrayList<>();//存储所有新增该库所的状态。按说不就只有一个？
//            for (int j = 0; j < reachabilitySetSize; j++) {
//                ArrayList<String> APpTermToCompareWith = reachabilitySet.get(j).getAPp();
//                if (APpTermToCompareWith.contains(oneNewPlace)) {
//                    states.add((String) reachabilitySet.get(j).getUserObject());
//                }
//            }
//
//            for (int j = 0; j < states.size() - 1; j++) {
//                oneNewPlaceStr += states.get(j) + ", ";
//            }
//            oneNewPlaceStr += states.get(states.size() - 1) + "};\n";
//
//            APpPart += oneNewPlaceStr;
//        }
//
//        /*APc（γ）*/
//
//        //首先得到一个哈希表存储APc中所有元素
//        // TODO: 这里其实是有问题的，当γ中存在一个常量映射为两个变量的情况则因为键冲突无法加入哈希表
//        HashMap<String, String> APcList = new HashMap<>();//变量命名为list合适吗？？？
//        for (int i = 0; i < reachabilitySetSize; i++) {
//            HashMap<String, ArrayList<String>> oneAPcTerm = reachabilitySet.get(i).getConstraintFunction();
//
//            for (String var : oneAPcTerm.keySet()) {
//                ArrayList<String> cons = oneAPcTerm.get(var);
//                for (int j = 0; j < cons.size(); j++) {
//                    /*应该不会加入重复元素吧？*/
//                    APcList.put(var, cons.get(j));
//                }
//            }
//
//        }
//
//
//        //这里把计算和展示一起做了合适吗？
//        String APcPart = "\n";
//        for (String var : APcList.keySet()) {
//            String con = APcList.get(var);
//            String oneAPcTermStr = var + "_" + con + "          := state in {";
//
//            ArrayList<String> states = new ArrayList<>();//存储所有γ中含有该映射的状态。必须存是因为受限于展示？
//            for (int i = 0; i < reachabilitySetSize; i++) {
//                HashMap<String, ArrayList<String>> APcTermToCompareWith = reachabilitySet.get(i).getConstraintFunction();
//                if (isIncludedInAPc(APcTermToCompareWith, var, con)) {
//                    states.add((String) reachabilitySet.get(i).getUserObject());
//                }
//            }
//
//            for (int i = 0; i < states.size() - 1; i++) {
//                oneAPcTermStr += states.get(i) + ", ";
//            }
//            oneAPcTermStr += states.get(states.size() - 1) + "};\n";
//
//            APcPart += oneAPcTermStr;
//        }
//
//
////        for (int i = 0; i < APcList.size(); i++) {
////            String oneAPcTermStr = "";
////            HashMap<String, ArrayList<String>> oneAPcTerm = APcList.get(i);
////
////            // TODO: 用γi来命名合适吗？若不这样，物理含义也很难在一个命名中表示
////
////            oneAPcTermStr += "γ" + i + "          := state in {";//初步设置十个空格;
////
////
////
////            ArrayList<String> states = new ArrayList<>();//存储所有APc(x)=oneAPpTerm的状态
////            for (int j = 0; j < reachabilitySetSize; j++) {
////                HashMap<String, ArrayList<String>> APcTermToCompareWith = reachabilitySet.get(j).getConstraintFunction();
////                if (judgeEqualityOfAPc(oneAPcTerm, APcTermToCompareWith)) {
////                    states.add((String) reachabilitySet.get(j).getUserObject());
////                }
////            }
////
////            for (int j = 0; j < states.size() - 1; j++) {
////                oneAPcTermStr += states.get(j) + ", ";
////            }
////            oneAPcTermStr += states.get(states.size() - 1) + "};\n";
////
////            APcPart += oneAPcTermStr;
////        }
//
//        /*APl*/
//
//        //首先得到一个列表存储APl中所有元素，每个新增库所是一个元素
//        ArrayList<APlTerm> APlList = new ArrayList<>();
//        for (int i = 0; i < reachabilitySetSize; i++) {
//            ArrayList<APlTerm> oneAPlTerm = reachabilitySet.get(i).getAPl();
//
//            for (int j = 0; j < oneAPlTerm.size(); j++) {
//                APlTerm oneGammaEvo = oneAPlTerm.get(j);
//                if (!APlList.contains(oneGammaEvo)) {
//                    APlList.add(oneGammaEvo);
//                }
//            }
//
//        }
//
//        //这里把计算和展示一起做了合适吗？主要是想循环一次完成以提高效率
//        String APlPart = "\n";
//        for (int i = 0; i < APlList.size(); i++) {
//            String oneGammaEvoStr = "";
//
//            APlTerm oneGammaEvo = APlList.get(i);
//            if (oneGammaEvo.getOper().equals("+")) {
//                oneGammaEvoStr += oneGammaEvo.getCon() + "_" + oneGammaEvo.getVar() + "_add          := state in {";
//            } else {
//                oneGammaEvoStr += oneGammaEvo.getCon() + "_" + oneGammaEvo.getVar() + "_delete          := state in {";
//            }
//
//
//            ArrayList<String> states = new ArrayList<>();//存储所有新增该库所的状态。按说不就只有一个？
//            for (int j = 0; j < reachabilitySetSize; j++) {
//                ArrayList<APlTerm> APlTermToCompareWith = reachabilitySet.get(j).getAPl();
//                if (APlTermToCompareWith.contains(oneGammaEvo)) {
//                    states.add((String) reachabilitySet.get(j).getUserObject());
//                }
//            }
//
//            for (int j = 0; j < states.size() - 1; j++) {
//                oneGammaEvoStr += states.get(j) + ", ";
//            }
//            oneGammaEvoStr += states.get(states.size() - 1) + "};\n";
//
//            APlPart += oneGammaEvoStr;
//        }
//
//        /*APt*/
//
////        /*β*/
////        // TODO: 这里用哈希表存是有问题的，键相同的映射无法加入。所有还是要写个Binding类
////        HashMap<String, String> bindingList = new HashMap<>();
////        for (int i = 0; i < reachabilitySetSize; i++) {
////            ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(i).getAPt();
////
////            for (int j = 0; j < APtOfOneState.size(); j++) {
////                APtTerm oneAPtTerm = APtOfOneState.get(j);
////                HashMap<String, String> correspondingBinding = oneAPtTerm.getCorrespondingBinding();
////                for (String var : correspondingBinding.keySet()) {
////                    if (!var.equals("ε")) {//过滤掉普通token的绑定
////                        bindingList.put(var, correspondingBinding.get(var));
////                    }
////                }
////            }
////        }
////
////        //这里把计算和展示一起做了合适吗？
////        String bindingPart = "\n";
////        for (String var : bindingList.keySet()) {
////            String con = bindingList.get(var);
////            String oneBindingStr = var + "_" + con + "          := state in {";
////
////            ArrayList<String> states = new ArrayList<>();//存储所有绑定中含有该映射的状态
////            for (int i = 0; i < reachabilitySetSize; i++) {
////                ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(i).getAPt();
////                if (isIncludedInAPt(APtOfOneState, var, con)) {
////                    states.add((String) reachabilitySet.get(i).getUserObject());
////                }
////            }
////
////            for (int i = 0; i < states.size() - 1; i++) {
////                oneBindingStr += states.get(i) + ", ";
////            }
////            oneBindingStr += states.get(states.size() - 1) + "};\n";
////
////            bindingPart += oneBindingStr;
////        }
//
//        /*β*/
//        // TODO: 这里用哈希表存是有问题的，键相同的映射无法加入。所以还是要写个Mapping类
//        ArrayList<Mapping> bindingList = new ArrayList<>();
//        for (int i = 0; i < reachabilitySetSize; i++) {
//            ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(i).getAPt();
//
//            for (int j = 0; j < APtOfOneState.size(); j++) {
//                APtTerm oneAPtTerm = APtOfOneState.get(j);
//                HashMap<String, String> correspondingBinding = oneAPtTerm.getCorrespondingBinding();
//                for (String var : correspondingBinding.keySet()) {
//
//                    Mapping oneMapping = new Mapping(var, correspondingBinding.get(var));
//
//                    if (!var.equals("ε") && !bindingList.contains(oneMapping)) {//注意过滤掉普通token的绑定
//                        bindingList.add(oneMapping);
//                    }
//                }
//            }
//        }
//
//        //这里把计算和展示一起做了合适吗？
//        String bindingPart = "\n";
//        for (int i = 0; i < bindingList.size(); i++) {
//            Mapping oneMapping = bindingList.get(i);
//            String var = oneMapping.getVar();
//            String con = oneMapping.getCon();
//            String oneMappingStr = var + "_" + con + "          := state in {";
//
//            ArrayList<String> states = new ArrayList<>();//存储所有绑定中含有该映射的状态
//            for (int j = 0; j < reachabilitySetSize; j++) {
//                ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(j).getAPt();
//                if (isIncludedInAPt(APtOfOneState, var, con)) {
//                    states.add((String) reachabilitySet.get(j).getUserObject());
//                }
//            }
//
//            for (int j = 0; j < states.size() - 1; j++) {
//                oneMappingStr += states.get(j) + ", ";
//            }
//            oneMappingStr += states.get(states.size() - 1) + "};\n";
//
//            bindingPart += oneMappingStr;
//
//        }
//
//
//        definePart += APmPart;
//
//        definePart += APpPart;
//        definePart += APcPart;
//        definePart += APlPart;
//
//        definePart += bindingPart;
//
//        res += varPart;
//        res += assignPart;
//        res += definePart;
//
//        byte b[] = res.getBytes();
//
//        try {
//            FileOutputStream fos = new FileOutputStream(new File("d:", "2.smv"));
//            fos.write(b);
//        } catch (IOException e) {
//            System.out.println("fail to write the file");
//        }
//
//        System.out.println("完成写入");
//
//    }

    private boolean judgeEqualityOfAPc(HashMap<String, ArrayList<String>> constraintFunction, HashMap<String, ArrayList<String>> constraintFunctionToCompareWith) {

        /*一个为空，一个不为空则一定不相等*/
//        if (((constraintFunction.size() == 0)&&(constraintFunctionToCompareWith.size() != 0)) || ((constraintFunction.size() != 0)&&(constraintFunctionToCompareWith.size() == 0))) {
//            return false;
//        }

        if ((isConstraintFuncIndeedNull(constraintFunction)&&!isConstraintFuncIndeedNull(constraintFunctionToCompareWith)) || (!isConstraintFuncIndeedNull(constraintFunction)&&isConstraintFuncIndeedNull(constraintFunctionToCompareWith))) {

//            /*————debug————*/
//            System.out.println("伽马不相等是因为1");
//            /*————debug————*/

            return false;
        }

        if (isConstraintFuncIndeedNull(constraintFunction)&&isConstraintFuncIndeedNull(constraintFunctionToCompareWith)) {
            return true;
        }

        for (String var : constraintFunction.keySet()) {
            if (!constraintFunctionToCompareWith.containsKey(var)) {

//                /*————debug————*/
//                System.out.println("伽马不相等是因为2");
//                /*————debug————*/

                return false;//有一个不包含则直接返回

            } else {

                ArrayList<String> cons1 = constraintFunction.get(var);
                ArrayList<String> cons2 = constraintFunctionToCompareWith.get(var);

                if (!cons1.containsAll(cons2) || !cons2.containsAll(cons1)) {

//                    /*————debug————*/
//                    System.out.println("伽马不相等是因为3");
//                    /*————debug————*/

                    return false;
                }

            }
        }

        return true;
    }

    private boolean isConstraintFuncIndeedNull(HashMap<String, ArrayList<String>> constraintFunction) {
        if (constraintFunction.size() == 0) {
            return true;
        }

        for (String var : constraintFunction.keySet()) {
            if (constraintFunction.get(var).size() != 0) {
                return false;
            }
        }

        return true;
    }

    private boolean isIncludedInAPc(HashMap<String, ArrayList<String>> APcTermToCompareWith, String var, String con) {

        if (isConstraintFuncIndeedNull(APcTermToCompareWith)) {
            return false;
        }

        /*这样能否提高效率？*/
        if (!APcTermToCompareWith.containsKey(var)) {
            return false;
        }

        for (String varToCompareWith : APcTermToCompareWith.keySet()) {
            if (varToCompareWith.equals(var)) {
                ArrayList<String> consToCompareWith = APcTermToCompareWith.get(var);
                if (consToCompareWith.contains(con)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isIncludedInAPt(ArrayList<APtTerm> APtOfOneState, String var, String con) {

        if (APtOfOneState.size() == 0) {
            return false;
        }

        for (int i = 0; i < APtOfOneState.size(); i++) {
            APtTerm oneAPtTerm = APtOfOneState.get(i);
            HashMap<String, String> correspondingBinding = oneAPtTerm.getCorrespondingBinding();

            if (!correspondingBinding.containsKey(var)) {
                continue;
            }

            for (String varToCompareWith : correspondingBinding.keySet()) {
                if (varToCompareWith.equals(var)) {
                    String conToCompareWith = correspondingBinding.get(varToCompareWith);

                    if (conToCompareWith != null) {//注意这里要加是否为空的判断
                        if (con.equals(conToCompareWith)) {
                            return true;
                        }
                    }

                }
            }

        }

        return false;
    }


    private boolean isIncludedInAPm(HashMap<String, HashMap<Token, Integer>> MAfterIntegration, APmTerm oneAPmTerm) {
        String place = oneAPmTerm.getPlace();

        if (!MAfterIntegration.containsKey(place)) {
            return false;
        }

        Token token = oneAPmTerm.getToken();
        HashMap<Token, Integer> tokenWithNum = MAfterIntegration.get(place);

        if (!tokenWithNum.containsKey(token)) {
            return false;
        }

        int num = oneAPmTerm.getNum();

        if (tokenWithNum.get(token) != num) {
            return false;
        }

        return true;

    }

    public HashMap<String, HashMap<Token, Integer>> integrateTokens(Marking curMarking) {
        HashMap<String, HashMap<Token, Integer>> tokensAfterIntegration = new HashMap<String, HashMap<Token, Integer>>();
        HashMap<String, ArrayList<Token>> tokensBeforeIntegration = curMarking.getM();

        for (String column : tokensBeforeIntegration.keySet()) {//两个哈希表的键是一致的
            ArrayList<Token> allTokens = tokensBeforeIntegration.get(column);
            int tokenNum = allTokens.size();

            int[] isClassified = new int[tokenNum];//各位置自动初始化为0。0表示未分类，1表示已分类

            HashMap<Token, Integer> oneKindOfToken = new HashMap<Token, Integer>();

            for (int i = 0; i < tokenNum; i++) {
                if (isClassified[i] == 1) {//已分类则跳过
                    continue;
                }

                /*下面都是第一次访问到某种类型token的时候要做的事*/
                Token token = allTokens.get(i);
                int theTokenNum = 1;//记录此种类型token的个数
                oneKindOfToken.put(token, 1);//在返回的token列表中加入这种token
                for (int j = i + 1; j < tokenNum; j++) {//遍历token列表中此token后面的token
                    if (isClassified[j] == 0) {
                        Token tokenToCompareWith = allTokens.get(j);
                        if (token.equals(tokenToCompareWith)) {
                            oneKindOfToken.replace(token, ++theTokenNum);
                            isClassified[j] = 1;
                        }
                    }
                }

            }

            tokensAfterIntegration.put(column, oneKindOfToken);
        }

        /*————debug————*/
//        System.out.println("tokensBeforeIntegration.size()" + tokensBeforeIntegration.size());
//        System.out.println("tokensAfterIntegration.size()" + tokensAfterIntegration.size());
        /*————debug————*/

        return tokensAfterIntegration;
    }

    private void createSMVFileForOKS(ArrayList<KSNode> reachabilitySet) {
        int reachabilitySetSize = reachabilitySet.size();


        ArrayList<KSNode> nodesToBeDeleted = new ArrayList<>();
        boolean[] fakeDeleted = new boolean[reachabilitySetSize];
        while (sameNodesExist(reachabilitySet, fakeDeleted)) {
            for (int i = 0; i < reachabilitySetSize; i++) {

                if (fakeDeleted[i] == true) {
                    continue;
                }

                KSNode ksNode = reachabilitySet.get(i);

                /*C11的后集*/
//            System.out.println(reachabilitySet.get(11).getUserObject());
//            System.out.println("C11的后集大小为" + reachabilitySet.get(11).getChildren().size());
//            for (int k = 0; k < reachabilitySet.get(11).getChildren().size(); k++) {
//                System.out.print(reachabilitySet.get(11).getChildren().get(k).getUserObject() + " ");
//            }
//            System.out.println();

//            boolean stepping = false;

                for (int j = i + 1; j < reachabilitySetSize; j++) {

                    if (fakeDeleted[j] == true) {
                        continue;
                    }

                    KSNode ksNodeToCompareWith = reachabilitySet.get(j);

                    /*从前向后合并*/
                    if (nodeConditionalEquals(ksNode, ksNodeToCompareWith) && (isTheOnlyPostNode(ksNode, ksNodeToCompareWith) || !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet))) {
                        /*注意是前面向后面合并*/

                        /*————debug————*/
                        if (ksNode.getUserObject().equals("C6") && ksNodeToCompareWith.getUserObject().equals("C7")) {
                            System.out.println("isTheOnlyPostNode(ksNode, ksNodeToCompareWith) is " + isTheOnlyPostNode(ksNode, ksNodeToCompareWith));
                            System.out.println("!haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet) is " + !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet));
                        }
                        /*————debug————*/

                        // TODO: 这样暴力添加会不会重复？所以在添加前要判断是否重复
                        // TODO: C0会不会有问题？
                        /*比如KSNode和ksNodeToCompareWith有相同的后集结点就会重复添加*/
                        /*前集状态*/
                        ArrayList<KSNode> preNodesOfksNode = ksNode.getPreNodes();
                        ArrayList<KSNode> preNodesOfksNodeTCW = ksNodeToCompareWith.getPreNodes();
                        for (int k = 0; k < preNodesOfksNode.size(); k++) {
                            KSNode preNodeOfksNode = preNodesOfksNode.get(k);
                            if (!preNodesOfksNodeTCW.contains(preNodeOfksNode)) {
                                preNodesOfksNodeTCW.add(preNodeOfksNode);//将被合并结点的前集结点加入合并结点的前集
                            }
                            ArrayList<KSNode> postNodes = preNodeOfksNode.getChildren();//这里的postNodes是ksNode前集结点的后集
                            if (!postNodes.contains(ksNodeToCompareWith)) {
                                postNodes.add(ksNodeToCompareWith);//将合并结点加入被合并结点的所有前集结点的后集
                            }

                            /*删除被合并结点*/
                            postNodes.remove(ksNode);
                        }


                        /*后集状态*/
                        ArrayList<KSNode> postNodesOfksNode = ksNode.getChildren();
                        ArrayList<KSNode> postNodesOfksNodeTCW = ksNodeToCompareWith.getChildren();
                        for (int k = 0; k < postNodesOfksNode.size(); k++) {
                            KSNode postNodeOfksNode = postNodesOfksNode.get(k);

                            if (!postNodeOfksNode.equals(ksNodeToCompareWith)) {//注意要过滤掉ksNodeToCompareWith！
                                if (!postNodesOfksNodeTCW.contains(postNodeOfksNode)) {
                                    postNodesOfksNodeTCW.add(postNodeOfksNode);//将被合并结点的后集结点加入合并结点的后集
                                }
                                ArrayList<KSNode> preNodes = postNodeOfksNode.getPreNodes();//这里的preNodes是ksNode后集结点的前集
                                if (!preNodes.contains(ksNodeToCompareWith)) {
                                    preNodes.add(ksNodeToCompareWith);//将合并结点加入被合并结点的所有后集结点的前集
                                }

                                /*删除被合并结点*/
                                preNodes.remove(ksNode);
                            }

                        }

                        /*从可达集中删去ksNode*/
//                    reachabilitySet.remove(ksNode);
                        nodesToBeDeleted.add(ksNode);
                        fakeDeleted[i] = true;

                        /*————debug————*/
                        System.out.println(ksNode.getUserObject() + "到" + ksNodeToCompareWith.getUserObject() + "的合并");
                        if (ksNodeToCompareWith.getUserObject().equals("C9")) {
                            for (int k = 0; k < ksNodeToCompareWith.getChildren().size(); k++) {
                                System.out.print(ksNodeToCompareWith.getChildren().get(k).getUserObject() + " ");
                            }
                            System.out.println();
                        }
                        /*————debug————*/

                        /*找到一组相等的就退出*/
//                    stepping = true;
                        break;
                    }

                    /*从后向前合并*/
                    if (nodeConditionalEquals(ksNode, ksNodeToCompareWith) && isTheOnlyPostNode(ksNodeToCompareWith, ksNode)) {
                        /*注意是后面向前面合并*/

                        /*————debug————*/
//                        if (ksNode.getUserObject().equals("C6") && ksNodeToCompareWith.getUserObject().equals("C7")) {
//                            System.out.println("isTheOnlyPostNode(ksNode, ksNodeToCompareWith) is " + isTheOnlyPostNode(ksNode, ksNodeToCompareWith));
//                            System.out.println("!haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet) is " + !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet));
//                        }
                        /*————debug————*/

                        // TODO: 这样暴力添加会不会重复？所以在添加前要判断是否重复
                        // TODO: C0会不会有问题？
                        /*比如KSNode和ksNodeToCompareWith有相同的后集结点就会重复添加*/
                        /*前集状态*/
                        ArrayList<KSNode> preNodesOfksNode = ksNode.getPreNodes();
                        ArrayList<KSNode> preNodesOfksNodeTCW = ksNodeToCompareWith.getPreNodes();
                        for (int k = 0; k < preNodesOfksNodeTCW.size(); k++) {
                            KSNode preNodeOfksNodeTCW = preNodesOfksNodeTCW.get(k);
                            if (!preNodesOfksNode.contains(preNodeOfksNodeTCW)) {
                                preNodesOfksNode.add(preNodeOfksNodeTCW);//将被合并结点的前集结点加入合并结点的前集
                            }
                            ArrayList<KSNode> postNodes = preNodeOfksNodeTCW.getChildren();//这里的postNodes是ksNodeToCompareWith前集结点的后集
                            if (!postNodes.contains(ksNode)) {
                                postNodes.add(ksNode);//将合并结点加入被合并结点的所有前集结点的后集
                            }

                            /*删除被合并结点*/
                            postNodes.remove(ksNodeToCompareWith);
                        }


                        /*后集状态*/
                        ArrayList<KSNode> postNodesOfksNode = ksNode.getChildren();
                        ArrayList<KSNode> postNodesOfksNodeTCW = ksNodeToCompareWith.getChildren();
                        for (int k = 0; k < postNodesOfksNodeTCW.size(); k++) {
                            KSNode postNodeOfksNodeTCW = postNodesOfksNodeTCW.get(k);

                            if (!postNodeOfksNodeTCW.equals(ksNode)) {//注意要过滤掉ksNode！
                                if (!postNodesOfksNode.contains(postNodeOfksNodeTCW)) {
                                    postNodesOfksNode.add(postNodeOfksNodeTCW);//将被合并结点的后集结点加入合并结点的后集
                                }
                                ArrayList<KSNode> preNodes = postNodeOfksNodeTCW.getPreNodes();//这里的preNodes是ksNodeToCompareWith后集结点的前集
                                if (!preNodes.contains(ksNode)) {
                                    preNodes.add(ksNode);//将合并结点加入被合并结点的所有后集结点的前集
                                }

                                /*删除被合并结点*/
                                preNodes.remove(ksNodeToCompareWith);
                            }

                        }

                        /*从可达集中删去ksNodeToCompareWith*/
//                    reachabilitySet.remove(ksNode);
                        nodesToBeDeleted.add(ksNodeToCompareWith);
                        fakeDeleted[j] = true;

                        /*————debug————*/
                        System.out.println(ksNodeToCompareWith.getUserObject() + "到" + ksNode.getUserObject() + "的合并");
//                        if (ksNodeToCompareWith.getUserObject().equals("C9")) {
//                            for (int k = 0; k < ksNodeToCompareWith.getChildren().size(); k++) {
//                                System.out.print(ksNodeToCompareWith.getChildren().get(k).getUserObject() + " ");
//                            }
//                            System.out.println();
//                        }
                        /*————debug————*/

                        /*找到一组相等的就退出*/
                        break;
                    }

                }

            }

            /*————debug————*/
            System.out.println("结束一次遍历");
            /*————debug————*/

        }


        /*————debug————*/
        System.out.println("C4的后集大小为" + reachabilitySet.get(4).getChildren().size());
        System.out.println("C4的后集为" + reachabilitySet.get(4).getChildren().get(0).getUserObject());
        /*————debug————*/

        /*注意这里要更新可达集*/
        for (int i = 0; i < nodesToBeDeleted.size(); i++) {
            reachabilitySet.remove(nodesToBeDeleted.get(i));
        }
        reachabilitySetSize = reachabilitySet.size();

        /*结点（状态）改名*/
        // TODO: 为什么改了这里展示结果也会变？？？因为展示的地方依赖于结点命名。调试的时候可以不改结点名
        for (int i = 0; i < reachabilitySetSize; i++) {
            System.out.println(reachabilitySet.get(i).getUserObject() + "改为了" + "S" + i);
            reachabilitySet.get(i).setUserObject("S" + i);
        }

        /*据此来决定展示哪些维度*/
        boolean[] propositionType = petriNet.getPropositionType();

        String res = "\nMODULE main\n\n";

        /*VAR*/
        String varPart = "VAR\n\nstate : {";
        for (int i = 0; i < reachabilitySetSize - 1; i++) {
            varPart += reachabilitySet.get(i).getUserObject() + ", ";
        }
        varPart += reachabilitySet.get(reachabilitySet.size() - 1).getUserObject() + "};\n\n";

        /*ASSIGN*/
        String assignPart = "ASSIGN\n\ninit(state) := S0;\nnext(state) :=\n\ncase\n";//这里初始状态硬编码为S0合适吗？
        for (int i = 0; i < reachabilitySetSize; i++) {
            KSNode node = reachabilitySet.get(i);

//            /*————debug————*/
//            if (node.getUserObject().equals("C13")) {
//                System.out.println("此时C13后集大小为到" + node.getChildren().size());
//            }
//            /*————debug————*/

            String oneAssignTerm = "state = " + node.getUserObject() + "          : {";//初步设置十个空格
            ArrayList<KSNode> children = node.getChildren();

            // TODO: 排序为了文件中的展示
            ArrayList<KSNode> childrenToSort = new ArrayList<>();
            childrenToSort.addAll(children);//这样能否避免对node后集的影响？
            for (int j = 0; j < childrenToSort.size(); j++) {
                KSNode nodeToSort = childrenToSort.get(j);
            }


            for (int j = 0; j < children.size() - 1; j++) {
                if (children.get(j).getUserObject().toString().charAt(0) == 'S') {//过滤掉Cn的情况。
                    oneAssignTerm += children.get(j).getUserObject() + ", ";
                }
//                if (children.get(j).getUserObject().toString().charAt(1) != '-') {//只过滤掉C-1的情况。这样考虑全面吗？
//                    oneAssignTerm += children.get(j).getUserObject() + ", ";
//                }
//                oneAssignTerm += children.get(j).getUserObject() + ", ";
            }
            if (children.get(children.size() - 1).getUserObject().toString().charAt(0) == 'S') {//过滤掉Cn的情况。
                oneAssignTerm += children.get(children.size() - 1) + "};\n";
            } else {
                oneAssignTerm = oneAssignTerm.substring(0, oneAssignTerm.length() - 2);//去掉","
                oneAssignTerm += "};\n";
            }
//            oneAssignTerm += children.get(children.size() - 1) + "};\n";

            assignPart += oneAssignTerm;
        }
        assignPart += "esac;\n\n";

        /*DEFINE*/
        String definePart = "DEFINE\n\n";

        /*APm*/
        // TODO: 好像token不太好展示吧？其实就是命名的问题嘛。还有到底应该展示token还是标识呢？
        String APmPart = "\n";
        if (propositionType[0] == true) {
            ArrayList<APmTerm> APmList = new ArrayList<>();
            for (int i = 0; i < reachabilitySetSize; i++) {
                Marking marking = reachabilitySet.get(i).getMarking();
                HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(marking);
                for (String place : MAfterIntegration.keySet()) {
                    HashMap<Token, Integer> tokenWithNum = MAfterIntegration.get(place);
                    for (Token token : tokenWithNum.keySet()) {
                        APmTerm aPmTerm = new APmTerm(place, token, tokenWithNum.get(token));
                        if (!APmList.contains(aPmTerm)) {
                            APmList.add(aPmTerm);
                        }
                    }
                }

            }

            // TODO: 对APmList排序？相同库所放一起，相同token放一起，然后按数量排序？

            //这里把计算和展示一起做了合适吗？
            for (int i = 0; i < APmList.size(); i++) {
                APmTerm oneAPmTerm = APmList.get(i);
                String place = oneAPmTerm.getPlace();
                Token token = oneAPmTerm.getToken();
                ArrayList<String> cons = token.getConstants();
                int num = oneAPmTerm.getNum();
                String oneAPmTermStr = place + "_";
                for (int j = 0; j < cons.size(); j++) {
                    oneAPmTermStr += cons.get(j) + "_";
                }
                oneAPmTermStr += num + "          := state in {";

                ArrayList<String> states = new ArrayList<>();//存储所有标识中含有该条目的状态
                for (int j = 0; j < reachabilitySetSize; j++) {
                    Marking marking = reachabilitySet.get(j).getMarking();
                    HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(marking);
                    if (isIncludedInAPm(MAfterIntegration, oneAPmTerm)) {
                        states.add((String) reachabilitySet.get(j).getUserObject());
                    }
                }

                for (int j = 0; j < states.size() - 1; j++) {
                    oneAPmTermStr += states.get(j) + ", ";
                }
                oneAPmTermStr += states.get(states.size() - 1) + "};\n";

                APmPart += oneAPmTermStr;

            }
        }




        /*APp*/
        String APpPart = "\n";
        if (propositionType[1] == true) {
            //首先得到一个列表存储APp中所有元素，每个新增库所是一个元素
            ArrayList<String> APpList = new ArrayList<>();
            for (int i = 0; i < reachabilitySetSize; i++) {
                ArrayList<String> oneAPpTerm = reachabilitySet.get(i).getAPp();

                for (int j = 0; j < oneAPpTerm.size(); j++) {
                    String oneNewPlace = oneAPpTerm.get(j);
                    if (!APpList.contains(oneNewPlace)) {
                        APpList.add(oneNewPlace);
                    }
                }

            }

            //这里把计算和展示一起做了合适吗？主要是想循环一次完成以提高效率
            for (int i = 0; i < APpList.size(); i++) {
                String oneNewPlaceStr = "";

                String oneNewPlace = APpList.get(i);
                oneNewPlaceStr += oneNewPlace + "_add          := state in {";


                ArrayList<String> states = new ArrayList<>();//存储所有新增该库所的状态。按说不就只有一个？
                for (int j = 0; j < reachabilitySetSize; j++) {
                    ArrayList<String> APpTermToCompareWith = reachabilitySet.get(j).getAPp();
                    if (APpTermToCompareWith.contains(oneNewPlace)) {
                        states.add((String) reachabilitySet.get(j).getUserObject());
                    }
                }

                for (int j = 0; j < states.size() - 1; j++) {
                    oneNewPlaceStr += states.get(j) + ", ";
                }
                oneNewPlaceStr += states.get(states.size() - 1) + "};\n";

                APpPart += oneNewPlaceStr;
            }
        }



        /*APc（γ）*/
        String APcPart = "\n";
        if (propositionType[2] == true) {
            //首先得到一个列表存储APc中所有元素
            ArrayList<Mapping> APcList = new ArrayList<>();
            for (int i = 0; i < reachabilitySetSize; i++) {
                HashMap<String, ArrayList<String>> oneAPcTerm = reachabilitySet.get(i).getConstraintFunction();

                for (String var : oneAPcTerm.keySet()) {
                    ArrayList<String> cons = oneAPcTerm.get(var);
                    for (int j = 0; j < cons.size(); j++) {
                        String con = cons.get(j);
                        Mapping oneMapping = new Mapping(var, con);
                        if (!APcList.contains(oneMapping)) {
                            APcList.add(oneMapping);
                        }

                    }
                }

            }


            //这里把计算和展示一起做了合适吗？
            for (int i = 0; i < APcList.size(); i++) {
                Mapping oneMapping = APcList.get(i);
                String var = oneMapping.getVar();
                String con = oneMapping.getCon();
                String oneAPcTermStr = var + "_" + con + "          := state in {";

                ArrayList<String> states = new ArrayList<>();//存储所有γ中含有该映射的状态。必须存是因为受限于展示？
                for (int j = 0; j < reachabilitySetSize; j++) {
                    HashMap<String, ArrayList<String>> APcTermToCompareWith = reachabilitySet.get(j).getConstraintFunction();
                    if (isIncludedInAPc(APcTermToCompareWith, var, con)) {
                        states.add((String) reachabilitySet.get(j).getUserObject());
                    }
                }

                for (int j = 0; j < states.size() - 1; j++) {
                    oneAPcTermStr += states.get(j) + ", ";
                }
                oneAPcTermStr += states.get(states.size() - 1) + "};\n";

                APcPart += oneAPcTermStr;
            }

        }



        /*APl*/

        String APlPart = "\n";
        if (propositionType[3] == true) {
            //首先得到一个列表存储APl中所有元素，每个新增库所是一个元素
            ArrayList<APlTerm> APlList = new ArrayList<>();
            for (int i = 0; i < reachabilitySetSize; i++) {
                ArrayList<APlTerm> oneAPlTerm = reachabilitySet.get(i).getAPl();

                for (int j = 0; j < oneAPlTerm.size(); j++) {
                    APlTerm oneGammaEvo = oneAPlTerm.get(j);
                    if (!APlList.contains(oneGammaEvo)) {
                        APlList.add(oneGammaEvo);
                    }
                }

            }

            //这里把计算和展示一起做了合适吗？主要是想循环一次完成以提高效率
            for (int i = 0; i < APlList.size(); i++) {
                String oneGammaEvoStr = "";

                APlTerm oneGammaEvo = APlList.get(i);
                if (oneGammaEvo.getOper().equals("+")) {
                    oneGammaEvoStr += oneGammaEvo.getCon() + "_" + oneGammaEvo.getVar() + "_add          := state in {";
                } else {
                    oneGammaEvoStr += oneGammaEvo.getCon() + "_" + oneGammaEvo.getVar() + "_delete          := state in {";
                }


                ArrayList<String> states = new ArrayList<>();//存储所有新增该库所的状态。按说不就只有一个？
                for (int j = 0; j < reachabilitySetSize; j++) {
                    ArrayList<APlTerm> APlTermToCompareWith = reachabilitySet.get(j).getAPl();
                    if (APlTermToCompareWith.contains(oneGammaEvo)) {
                        states.add((String) reachabilitySet.get(j).getUserObject());
                    }
                }

                for (int j = 0; j < states.size() - 1; j++) {
                    oneGammaEvoStr += states.get(j) + ", ";
                }
                oneGammaEvoStr += states.get(states.size() - 1) + "};\n";

                APlPart += oneGammaEvoStr;
            }
        }


        /*APt*/
        String bindingPart = "\n";
        String transitionPart = "\n";
        if (propositionType[4] == true) {
            /*β*/
            // TODO: 这里用哈希表存是有问题的，键相同的映射无法加入。所以还是要写个Mapping类
            ArrayList<Mapping> bindingList = new ArrayList<>();
            for (int i = 0; i < reachabilitySetSize; i++) {
                ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(i).getAPt();

                for (int j = 0; j < APtOfOneState.size(); j++) {
                    APtTerm oneAPtTerm = APtOfOneState.get(j);
                    HashMap<String, String> correspondingBinding = oneAPtTerm.getCorrespondingBinding();
                    for (String var : correspondingBinding.keySet()) {

                        Mapping oneMapping = new Mapping(var, correspondingBinding.get(var));

                        if (!var.equals("ε") && !bindingList.contains(oneMapping)) {//注意过滤掉普通token的绑定
                            bindingList.add(oneMapping);
                        }
                    }
                }
            }

            //这里把计算和展示一起做了合适吗？
            for (int i = 0; i < bindingList.size(); i++) {
                Mapping oneMapping = bindingList.get(i);
                String var = oneMapping.getVar();
                String con = oneMapping.getCon();
                String oneMappingStr = var + "_" + con + "          := state in {";

                ArrayList<String> states = new ArrayList<>();//存储所有绑定中含有该映射的状态
                for (int j = 0; j < reachabilitySetSize; j++) {
                    ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(j).getAPt();
                    if (isIncludedInAPt(APtOfOneState, var, con)) {
                        states.add((String) reachabilitySet.get(j).getUserObject());
                    }
                }

                for (int j = 0; j < states.size() - 1; j++) {
                    oneMappingStr += states.get(j) + ", ";
                }
                oneMappingStr += states.get(states.size() - 1) + "};\n";

                bindingPart += oneMappingStr;

            }

            /*t*/
            ArrayList<String> transitionList = new ArrayList<>();
            for (int i = 0; i < reachabilitySetSize; i++) {
                ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(i).getAPt();

                for (int j = 0; j < APtOfOneState.size(); j++) {
                    APtTerm oneAPtTerm = APtOfOneState.get(j);
                    String enabledTransition = oneAPtTerm.getEnabledTransition();
                    if (!transitionList.contains(enabledTransition)) {
                        transitionList.add(enabledTransition);
                    }
                }
            }

            //这里把计算和展示一起做了合适吗？
            for (int i = 0; i < transitionList.size(); i++) {
                String enabledTransition = transitionList.get(i);
                String oneTransitionStr = enabledTransition + "          := state in {";

                ArrayList<String> states = new ArrayList<>();//存储所有绑定中含有该映射的状态
                for (int j = 0; j < reachabilitySetSize; j++) {
                    ArrayList<APtTerm> APtOfOneState = reachabilitySet.get(j).getAPt();
                    for (int k = 0; k < APtOfOneState.size(); k++) {
                        APtTerm oneAptTerm = APtOfOneState.get(k);
                        if (oneAptTerm.getEnabledTransition().equals(enabledTransition)) {
                            states.add((String) reachabilitySet.get(j).getUserObject());
                            break;//因为一个使能变迁可能对应两个不同的绑定，防止重复添加要break掉
                        }
                    }
                }

                for (int j = 0; j < states.size() - 1; j++) {
                    oneTransitionStr += states.get(j) + ", ";
                }
                oneTransitionStr += states.get(states.size() - 1) + "};\n";

                transitionPart += oneTransitionStr;

            }
        }







        definePart += APmPart;

        definePart += APpPart;
        definePart += APcPart;
        definePart += APlPart;

        definePart += bindingPart;
        definePart += transitionPart;

        res += varPart;
        res += assignPart;
        res += definePart;

        byte b[] = res.getBytes();

        try {
            FileOutputStream fos = new FileOutputStream(new File("d:", "3.smv"));
            fos.write(b);
        } catch (IOException e) {
            System.out.println("fail to write the file");
        }

        System.out.println("针对OKS完成写入");

    }

    public boolean nodeConditionalEquals(KSNode node, KSNode nodeToCompareWith) {

        /*据此来决定比较哪些维度*/
        boolean[] propositionType = petriNet.getPropositionType();

//        /*————debug————*/
//        if (nodeToCompareWith.getUserObject().equals("C4") && node.getParentNode().getUserObject().equals("C2")) {
//            System.out.println("进入C6/C7与C4的比较");
//        }
//        /*————debug————*/

        /*比配置*/

        /*比库所集*/
        /*注意库所集中的常量顺序是无序的*/
//        ArrayList<String> placeSet = node.getPlaceSet();
//        ArrayList<String> placeSetToCompareWith = nodeToCompareWith.getPlaceSet();
//
//        for (int i = 0; i < placeSet.size(); i++) {
//            if (!placeSetToCompareWith.contains(placeSet.get(i))) {
//
//                /*————debug————*/
//                System.out.println("不相等是因为1");
//                System.out.println("具体来说，placeSet.get(i)为" + placeSet.get(i));
//                for (int j = 0; j < placeSetToCompareWith.size(); j++) {
//                    System.out.print(" " + placeSetToCompareWith.get(j));
//                }
//                System.out.println("与之比较的结点是" + nodeToCompareWith.getUserObject());
//                /*————debug————*/
//
//                return false;//有一个不相等则直接返回
//            }
//        }
//        for (int i = 0; i < placeSetToCompareWith.size(); i++) {
//            if (!placeSet.contains(placeSetToCompareWith.get(i))) {
//
//                /*————debug————*/
//                System.out.println("不相等是因为2");
//                /*————debug————*/
//
//                return false;//有一个不相等则直接返回
//            }
//        }

        /*比γ函数*/
        if (propositionType[2] == true) {
            HashMap<String, ArrayList<String>> constraintFunction = node.getConstraintFunction();
            HashMap<String, ArrayList<String>> constraintFunctionToCompareWith = nodeToCompareWith.getConstraintFunction();


            /*当结点γ为空时不进入下面的循环*/
            if ((constraintFunction.size() == 0)&&(constraintFunctionToCompareWith.size() != 0)) {
                return false;
            }


            for (String var : constraintFunction.keySet()) {
                if (!constraintFunctionToCompareWith.containsKey(var)) {

                    /*————debug————*/
                    System.out.println("不相等是因为3");
                    System.out.println("针对变量" + var + "详细地说");
                    for (String var2 : constraintFunctionToCompareWith.keySet()) {
                        System.out.print(var2 + " ");
                    }
                    System.out.println();
                    /*————debug————*/

                    return false;//有一个不包含则直接返回
                } else {
                    ArrayList<String> cons1 = constraintFunction.get(var);
                    ArrayList<String> cons2 = constraintFunctionToCompareWith.get(var);

                    for (int i = 0; i < cons1.size(); i++) {
                        if (!cons2.contains(cons1.get(i))) {

                            /*————debug————*/
                            System.out.println("不相等是因为4");
                            /*————debug————*/

                            return false;//有一个不相等则直接返回
                        }
                    }
                    for (int i = 0; i < cons2.size(); i++) {
                        if (!cons1.contains(cons2.get(i))) {

                            /*————debug————*/
                            System.out.println("不相等是因为5");
                            /*————debug————*/

                            return false;//有一个不相等则直接返回
                        }
                    }
                }
            }
        }


        /*比标识*/
        if (propositionType[0] == true) {
            HashMap<String, HashMap<Token, Integer>> integratedM = integrateTokens(node.getMarking());
            HashMap<String, HashMap<Token, Integer>> integratedMToCompareWith = integrateTokens(nodeToCompareWith.getMarking());

            for (String column : integratedM.keySet()) {//遍历行中所有位置
//            if (!integratedMToCompareWith.containsKey(column)) {//好像没必要判断
//                return false;
//            }

                HashMap<Token, Integer> tokens1 = integratedM.get(column);
                HashMap<Token, Integer> tokens2 = integratedMToCompareWith.get(column);

                for (Token token : tokens1.keySet()) {
                    if (tokens2.containsKey(token)) {
                        int token1Num = tokens1.get(token);//重写的token方法能够作用到这里的get方法吗？应该可以吧
                        int token2Num = tokens2.get(token);
                        if (token1Num != token2Num) {

                            /*————debug————*/
                            System.out.println("不相等是因为6");
                            /*————debug————*/

                            return false;//只要有一种token不匹配则直接返回假
                        }
                    } else {

                        /*————debug————*/
                        System.out.println("不相等是因为7");
                        /*————debug————*/

                        return false;//如果标识中没有此类token则直接返回假
                    }
                }

                /*————debug————*/
                if (((tokens1.size() == 0)&&(tokens2.size() != 0))||((tokens1.size() != 0)&&(tokens2.size() == 0))) {

                    /*————debug————*/
                    System.out.println("不相等是因为8");
                    /*————debug————*/

                    return false;
                }
                /*————debug————*/

            }
        }


        /*比标签*/

        /*APp*/
        if (propositionType[1] == true) {
            ArrayList<String> APp = node.getAPp();
            ArrayList<String> APpToCompareWith = nodeToCompareWith.getAPp();
            if (!APpToCompareWith.containsAll(APp) || !APp.containsAll(APpToCompareWith)) {//用containsAll效率低吗？
                return false;
            }
        }


        /*APl*/
        if (propositionType[3] == true) {
            ArrayList<APlTerm> APl = node.getAPl();
            ArrayList<APlTerm> APlToCompareWith = nodeToCompareWith.getAPl();
            if (!APlToCompareWith.containsAll(APl) || !APl.containsAll(APlToCompareWith)) {//用containsAll效率低吗？
                return false;
            }
        }


        /*APt*/
        if (propositionType[4] == true) {
            ArrayList<APtTerm> APt = node.getAPt();
            ArrayList<APtTerm> APtToCompareWith = nodeToCompareWith.getAPt();
            if (!APtToCompareWith.containsAll(APt) || !APt.containsAll(APtToCompareWith)) {//用containsAll效率低吗？
                return false;
            }
        }


        /*————debug————*/
        System.out.println("发现相同结点" + node.getUserObject() + "和" + nodeToCompareWith.getUserObject());
        /*————debug————*/

        return true;

    }

    /*后者是前者的唯一后集*/
    private boolean isTheOnlyPostNode(KSNode ksNode, KSNode ksNodeToCompareWith) {
        ArrayList<KSNode> postNodes = ksNode.getChildren();



        if (!(postNodes.size() == 1) || !postNodes.contains(ksNodeToCompareWith)) {

            /*————debug————*/
            System.out.print(ksNode.getUserObject() + "和" + ksNodeToCompareWith.getUserObject() + "不满足单后集，后集为");
            for (int i = 0; i < postNodes.size(); i++) {
                System.out.print(postNodes.get(i).getUserObject() + " ");
            }
            System.out.println();
            /*————debug————*/

            /*针对C12后集为C-1，C11的情况*/
            /*真实过程是C12合并到了C4，C4再合并到C11*/
            // TODO: 但是其他情况呢？
            if (postNodes.size() == 2 && postNodes.get(0).getUserObject().equals("C-1") && postNodes.contains(ksNodeToCompareWith)) {
                return true;
            }

            return false;
        } else {
            return true;
        }
    }

    /*看前者是否在后者的路径上*/
    /*注意不能是路径上的上一个*/
    private boolean haveCasualRelation(KSNode ksNode, KSNode ksNodeToCompareWith, ArrayList<KSNode> reachabilitySet) {

        /*直接排除孩子的情况*/
        /*按说后面用路径也能排除啊？？？*/
        if (ksNode.getChildren().contains(ksNodeToCompareWith)) {
            return true;
        }

        int index = -1;
        for (int i = 0; i < reachabilitySet.size(); i++) {
            if (ksNodeToCompareWith.equals(reachabilitySet.get(i))) {
                index = i;
                break;
            }
        }

        TreeNode[] parentNodes = reachabilitySet.get(index).getPath();
        for (int i = 0; i < parentNodes.length - 1; i++) {//不用跟自己比
            if (ksNode.equals(parentNodes[i])) {

                /*————debug————*/
                System.out.println(ksNode.getUserObject() + "和" + ksNodeToCompareWith.getUserObject() + "有因果关系");
                /*————debug————*/

                return true;
            }
        }

        /*出现两个及以上直系父结点只可能是因为合并*/
        // TODO: 这样有漏洞吗？只针对C0加个判断？按说一定不为空吧？
        if (ksNodeToCompareWith.getParentNode() != null) {
            ArrayList<KSNode> siblings = ksNodeToCompareWith.getParentNode().getChildren();
            for (int i = 0; i < siblings.size(); i++) {
                KSNode oneSibling = siblings.get(i);

                /*避免兄弟不在可达集中的情况*/
                if (oneSibling.getUserObject() == "C-1") {
                    continue;
                }

                int index2 = -1;
                for (int j = 0; j < reachabilitySet.size(); j++) {
                    if (oneSibling.equals(reachabilitySet.get(j))) {
                        index2 = j;
                        break;
                    }
                }

                /*————debug————*/
                System.out.println("此时sibling为" + oneSibling.getUserObject());
                /*————debug————*/

                /*要避免兄弟不在可达集中的情况*/

                TreeNode[] parentNodes2 = reachabilitySet.get(index2).getPath();
                for (int j = 0; j < parentNodes2.length - 1; j++) {//不用跟自己比
                    if (ksNode.equals(parentNodes)) {

                        /*————debug————*/
                        System.out.println(ksNode.getUserObject() + "和" + ksNodeToCompareWith.getUserObject() + "有因果关系");
                        /*————debug————*/

                        return true;
                    }
                }
            }
        }


        return false;
    }

    private boolean sameNodesExist(ArrayList<KSNode> reachabilitySet, boolean[] fakeDeleted) {
        int reachabilitySetSize = reachabilitySet.size();

        for (int i = 0; i < reachabilitySetSize; i++) {

            if (fakeDeleted[i] == true) {
                continue;
            }

            KSNode ksNode = reachabilitySet.get(i);


            for (int j = i + 1; j < reachabilitySetSize; j++) {

                if (fakeDeleted[j] == true) {
                    continue;
                }

                KSNode ksNodeToCompareWith = reachabilitySet.get(j);

                /*————debug————*/
                if (ksNode.getUserObject().equals("C11") && ksNodeToCompareWith.getUserObject().equals("C12")) {
                    System.out.println("isTheOnlyPostNode(ksNode, ksNodeToCompareWith) is " + isTheOnlyPostNode(ksNode, ksNodeToCompareWith));
                    System.out.println("!haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet) is " + !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet));
                    System.out.println("进行C11和C12的比较");
                }
                /*————debug————*/

                /*从前向后比*/
                if (nodeConditionalEquals(ksNode, ksNodeToCompareWith) && (isTheOnlyPostNode(ksNode, ksNodeToCompareWith) || !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet))) {

                    /*————debug————*/
                    if (ksNode.getUserObject().equals("C6") && ksNodeToCompareWith.getUserObject().equals("C7")) {
                        System.out.println("isTheOnlyPostNode(ksNode, ksNodeToCompareWith) is " + isTheOnlyPostNode(ksNode, ksNodeToCompareWith));
                        System.out.println("!haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet) is " + !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet));
                    }
                    /*————debug————*/

                    /*找到一个就返回真*/
                    return true;
                }

                /*从后向前比*/
                if (nodeConditionalEquals(ksNode, ksNodeToCompareWith) && isTheOnlyPostNode(ksNodeToCompareWith, ksNode)) {

                    /*————debug————*/
                    if (ksNode.getUserObject().equals("C11") && ksNodeToCompareWith.getUserObject().equals("C12")) {
                        System.out.println("isTheOnlyPostNode(ksNode, ksNodeToCompareWith) is " + isTheOnlyPostNode(ksNode, ksNodeToCompareWith));
                        System.out.println("!haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet) is " + !haveCasualRelation(ksNode, ksNodeToCompareWith, reachabilitySet));
                        System.out.println("找到反向可发生");
                    }
                    /*————debug————*/

                    /*找到一个就返回真*/
                    return true;
                }

            }

        }

        return false;
    }

}
