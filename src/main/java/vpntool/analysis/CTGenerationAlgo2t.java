package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.gui.tVariableGroup;
import vpntool.models.*;
import vpntool.models.analysisMethods.CTNode;
import vpntool.utils.math.Permutation;
import vpntool.utils.parser.CommonStringMethods;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class CTGenerationAlgo2t {
    private PetriNet petriNet;

    private int duplicateNodeNumber = 0;//重复节点数。其实可以不设置此变量，直接用可达集的大小即可。

    public CTGenerationAlgo2t() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
    }

    public ArrayList<CTNode> createConfigurationTree(CTNode rootNode) {

        ArrayList<CTNode> reachabilitySet = new ArrayList<CTNode>();//可达集，要返回
        int nodeId = 1;//结点id

        ArrayList<CTNode> allNodeSet = new ArrayList<>();//中间过程的结点集
        allNodeSet.add(rootNode);
        rootNode.setLable("New");//将根节点标之以新

//        int duplicateNodeNumber = 0;//重复节点数

        /*利用两个列表来实现其实可以免去标签的操作？*/
        while (!allNodeSet.isEmpty()) {



            CTNode node = allNodeSet.remove(0);

            /*————debug————*/
            System.out.println(node.getUserObject() + "进入所有节点的while循环");
            /*————debug————*/

            /*如果是旧结点则跳过*/
//            if (node.getLable().equals("Old")) {
//
//                /*————debug————*/
//                System.out.println(node.getUserObject() + "标签是Old");
//                /*————debug————*/
//
//                continue;
//            }

            if (isDuplicatedNode(node, reachabilitySet)) {

                /*————debug————*/
                System.out.println(node.getUserObject() + "标签不是Old，但其实是重复结点");
                /*————debug————*/

                node.setLable("Old");//将标签置为旧
                duplicateNodeNumber++;

                addChildren(node, rootNode);//展示

                continue;
            }


            node.setLable("Old");//去新

            if (isTerminal(node)) {

                /*————debug————*/
                System.out.println(node.getUserObject() + "是终端结点");
                /*————debug————*/

                /*————debug————*/
//                if (isDuplicatedNode(node, reachabilitySet)) {
//                    duplicateNodeNumber++;
//                }
                /*————debug————*/

                reachabilitySet.add(node);

                /*————debug————*/
                System.out.println("此时可达集大小为" + reachabilitySet.size() + "，新加入的结点是" + node.getUserObject());
                /*————debug————*/

                // TODO: 死锁相关处理 
                
                addChildren(node, rootNode);
                
                continue;
            }

            for (int i = 0; i < petriNet.getTransitions().size(); i++) {
                if (!isTransitionEnabled(i, node)) {

                    /*————debug————*/
                    if (i == 2 && node.getUserObject().equals("C5")) {
                        System.out.println("结点C5条件下t3不可发生");
                    }
                    /*————debug————*/

                    continue;
                }

                /*此时在node条件下i一定是使能的*/
                ArrayList<CTNode> childrenNode  = enableTransition(i, node);//计算此变迁下的所有子节点（后续状态）

                /*————debug————*/
                if (node.getUserObject().equals("C2")) {
                    System.out.println("C2有" + childrenNode.size() + "个子结点，在变迁t" + (i + 1) + "时");
                    System.out.println("此时可达集大小为" + reachabilitySet.size() + "。具体展示如下");
                    for (int j = 0; j < reachabilitySet.size(); j++) {
                        System.out.println(reachabilitySet.get(j));
                    }
                    System.out.println();
                }

                if (node.getUserObject().equals("C0")) {
                    System.out.println("计算出C0结点在变迁t" + (i + 1) + "下的子结点后，可达集的大小为" + reachabilitySet.size());
                }

                System.out.println("计算出树中每个结点在指定变迁下的子结点后，可达集的大小为" + reachabilitySet.size());

                /*————debug————*/

                for (int j = 0; j < childrenNode.size(); j++) {
                    CTNode childNode = childrenNode.get(j);

                    /*————debug————*/
                    /*构造一个顺序表，临时存储可达集和其中元素的所有子集*/
                    /*并没用*/
//                    ArrayList<CTNode> toFindDuplication = new ArrayList<>();
//                    toFindDuplication.addAll(reachabilitySet);
//                    for (int k = 0; k < reachabilitySet.size(); k++) {
//                        toFindDuplication.addAll(reachabilitySet.get(k).getChildren());
//                    }
                    /*————debug————*/

                    /*找旧*/
                    /*这里找旧是多余的！*/
//                    if (isDuplicatedNode(childNode, toFindDuplication)) {
//                    if (isDuplicatedNode(childNode, reachabilitySet)) {// TODO: 与可达集中的结点比没问题吗？按说应该和路径上的结点比。好像就该和可达集中结点比？
//
//                        /*————debug————*/
//                        System.out.println("找到重复结点，其双亲节点为" + childNode.getParentNode().getUserObject());//这里调试有问题。null
//                        System.out.println(childNode.getParentNode() + "的子节点有" + childrenNode.size() + "个");
//                        System.out.println("此时可达集中结点数为" + reachabilitySet.size());
//                        for (int k = 0; k < reachabilitySet.size(); k++) {
//                            System.out.println("此时可达集中结点为：");
//                            System.out.print(reachabilitySet.get(k).getUserObject() + " ");
//                            System.out.println();
//                        }
//                        /*————debug————*/
//
//                        childNode.setLable("Old");//将标签置为旧
//                        duplicateNodeNumber++;
//                    }

                    /*找无界量*/
                    // TODO: 对无界量的处理

                    /*自维护的子节点集为空的时候要做的操作。也就是只做一次的操作*/
                    if (node.getChildren().size() == 0) {

                        /*————debug————*/
                        System.out.println(node.getUserObject() + "自维护的孩子列表为空");
                        /*————debug————*/

                        reachabilitySet.add(node);//将node加入可达集

                        /*————debug————*/
                        if (node.getUserObject().equals("C0")) {
                            System.out.println("计算出C0结点在变迁t" + (i + 1) + "下的子结点后，进入第" + (j + 1) + "个子结点，此时可达集的大小为" + reachabilitySet.size());
                        }
                        System.out.println("此时可达集大小为" + reachabilitySet.size());
                        /*————debug————*/

                        node.getChildren().add(childNode);//将新结点加入node的孩子集（自维护）

                        addChildren(node, rootNode);//展示。要放在node.add(childNode)之前


                    }

                    /*addChildren之后，使子结点展示时处于双亲结点最靠后的位置*/
                    node.add(childNode);//将新结点加入node的孩子集（内置）

                    childNode.setUserObject("C" + nodeId++);

                    childNode.setTransitionId(i);//设置产生新结点的变迁

                    /*————debug————*/
                    System.out.println("原节点的孩子数为" + node.getChildren().size());
                    System.out.println("产生新结点的变迁为" + "t" + (i + 1));
                    /*————debug————*/

                    allNodeSet.add(childNode);//将新结点加入allNodeSet

                }
                
            }

        }

        // TODO: 这样求出不相同的结点数对吗？
//        petriNet.setNodeNum(nodeId - duplicateNodeNumber);
        petriNet.setNodeNum(reachabilitySet.size());

        return reachabilitySet;
    }




    public boolean isTerminal(CTNode node) {
        Marking curMarking = node.getMarking();//变迁能否发生要根据此时的标识来判断。也就是说此时M已知，后面计算的是可转移到的标识情况
        HashMap<String, String> bindingOfCurNode = node.getBindingMap();//当前结点的绑定
        HashMap<String, ArrayList<String>> constraintFunction = node.getConstraintFunction();//本结点的γ函数

        /*整合标识*/
        HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(curMarking);



        /*只要有一个使能变迁即可返回假（不是端点）*/
        ArrayList<Transition> transitions = petriNet.getTransitions();

        for (int i = 0; i < transitions.size(); i++) {

            Transition transition = transitions.get(i);

            /*————debug————*/
            System.out.println("进入变迁" + (transition.getId() + 1) + "能否发生的判断");
            /*————debug————*/

            /*如果前集库所中有虚库所，且虚库所未实化，则该变迁一定不可发生？*/
            /*————debug————*/
            boolean areAllInstantiated = true;//标记所有前集虚库所是否都已实化
            ArrayList<Arc> inputArcs = transition.getInputArcs();
            for (int j = 0; j < inputArcs.size(); j++) {
                if (inputArcs.get(j).getSource() instanceof VirtualPlace) {
                    VirtualPlace prePlace = (VirtualPlace) inputArcs.get(j).getSource();

//                    HashMap<String, ArrayList<String>> constraintFunction = node.getParentNode().getConstraintFunction();//父节点的γ函数


                    if (node.getUserObject().equals("C1")) {
                        System.out.println("C1的γ函数大小为" + constraintFunction.size());
                    }

                    boolean isInstantiated = false;//标记某一个前集虚库所是否实化
                    for (String var : constraintFunction.keySet()) {
                        if (var.equals(prePlace.getName()) && constraintFunction.get(var).size() != 0 && constraintFunction.get(var) != null) {// TODO: 这里的处理严谨吗？


                            isInstantiated = true;
                            break;
                        }
                    }

                    /*只要有一个前集库所未实化则变迁不可发生*/
                    if (!isInstantiated) {

                        System.out.println("只要有一个前集库所未实化则变迁不可发生，该前集库所是" + prePlace.getName());
//                        System.out.println("其双亲节点为" + node.getParentNode().getUserObject());//这句话经常会导致空指针问题


                        areAllInstantiated = false;
                        break;
                    }

                }
            }

            /*————debug————*/
            System.out.println("变迁" + (transition.getId() + 1) + "结束对前集虚库所的判断");
            /*————debug————*/

            if (!areAllInstantiated) {
                continue;
            }
            /*————debug————*/

            /*下面做的事是生成绑定，要提取出来作为独立的函数*/
//            ArrayList<Arc> inputArcs = transition.getInputArcs();
//            HashMap<Place, ArrayList<HashMap<String, String>>> bindingOfAllPlaces = new HashMap<>();//所有前集库所维护一个哈希表
            ArrayList<ArrayList<HashMap<String, String>>> bindingOfAllPlaces = new ArrayList<>();//所有前集库所维护一个顺序表

            boolean hasToken = true;//标记前集库所中是否还有token

            for (int j = 0; j < inputArcs.size(); j++) {//一个输入弧对应一个前集库所
                Arc inputArc = inputArcs.get(j);

                ArrayList<HashMap<String, String>> bindingOfOnePlace = new ArrayList<>();//为每个前集库所维护一个绑定集的顺序表

                /*这样没有空指针隐患吧？*/
                Place prePlace = null;
                HashMap<Token, Integer> tokensAfterIntegration = null;//一个库所中的token情况


                if (inputArc.getSource() instanceof RealPlace) {//前集库所为实库所的情况
                    prePlace = (RealPlace) inputArc.getSource();
                    tokensAfterIntegration = MAfterIntegration.get(prePlace.getName());
                } else if (inputArc.getSource() instanceof VirtualPlace) {//前集库所为虚库所的情况
                    prePlace = (VirtualPlace) inputArc.getSource();

                    String realPlaceAfterBinding = null;
                    if (bindingOfCurNode.containsKey(prePlace.getName())) {//安全起见加一下判断。这里利用绑定而不是γ对不对？
                        realPlaceAfterBinding = bindingOfCurNode.get(prePlace.getName());
                    }

                    tokensAfterIntegration = MAfterIntegration.get(realPlaceAfterBinding);
                }

                /*若token为空，即库所中没token了，则变迁不可发生。只要前集库所中有一个没有token，则变迁不可发生*/
                /*输入弧上的标签不可以为空吧？*/
                if (tokensAfterIntegration.size() == 0) {
                    hasToken = false;
                    break;
                }

                // TODO: 目前只能处理弧标签有一个元组的情况
                ArrayList<String[]> tuplesInArcLabel = inputArc.getTuplesInArcLabel();
                for (int k = 0; k < tuplesInArcLabel.size(); k++) {
                    String tuple = tuplesInArcLabel.get(k)[0];
                    String[] variablesInTuple = CommonStringMethods.stringToStringArray(tuple);//此时变量是有序的

//                        ArrayList<HashMap<String, String>> bindingOfAllTokens = new ArrayList<>();//AllTokens不就是OnePlace吗。。

                    for (Token token : tokensAfterIntegration.keySet()) {
                        ArrayList<String> constants = token.getConstants();//此时常量是有序的

                        HashMap<String, String> bindingOfOneToken = new HashMap<>();
                        for (int l = 0; l < variablesInTuple.length; l++) {//这里variablesInTuple.length应该等于constants.size()
                            bindingOfOneToken.put(variablesInTuple[l], constants.get(l));
                        }

                        bindingOfOnePlace.add(bindingOfOneToken);
                    }

                }

//                bindingOfAllPlaces.put(prePlace, bindingOfOnePlace);
                bindingOfAllPlaces.add(bindingOfOnePlace);


            }

            if (hasToken == false) {
                continue;
            }

            /*————debug————*/
            System.out.println("所有库所的绑定数为" + bindingOfAllPlaces.size());
            for (int j = 0; j < bindingOfAllPlaces.size(); j++) {
                System.out.println("以下为进入组合函数前的绑定");
                ArrayList<HashMap<String, String>> oneBinding = bindingOfAllPlaces.get(j);
                for (int k = 0; k < oneBinding.size(); k++) {
                    HashMap<String, String> oneTerm = oneBinding.get(k);
                    for (String var : oneTerm.keySet()) {
                        System.out.println(var + "->" + oneTerm.get(var));
                    }
                }
            }
            /*————debug————*/

            ArrayList<ArrayList<HashMap<String, String>>> bindingCombination = tVariableGroup.combination(bindingOfAllPlaces);//内层顺序表中的元素去重后得到一个绑定

            /*————debug————*/
            System.out.println("未处理的绑定数为" + bindingCombination.size());
            for (int j = 0; j < bindingCombination.size(); j++) {
                System.out.println("以下为一组未处理的绑定");
                ArrayList<HashMap<String, String>> oneBinding = bindingCombination.get(j);
                for (int k = 0; k < oneBinding.size(); k++) {
                    HashMap<String, String> oneTerm = oneBinding.get(k);
                    for (String var : oneTerm.keySet()) {
                        System.out.println(var + "->" + oneTerm.get(var));
                    }
                }
            }
            /*————debug————*/

            ArrayList<HashMap<String, String>> allBindingMap = new ArrayList<>();//最终结果

            for (int j = 0; j < bindingCombination.size(); j++) {
                HashMap<String, String> resBinding = new HashMap<>();

                /*正常情况下每个映射都要添进去，如果映射冲突则不添加。这里的冲突是不同库所中token的冲突*/
                /*HashMap是不会添加重复键值的*/
                ArrayList<HashMap<String, String>> oneBindingToDeal = bindingCombination.get(j);
                for (int k = 0; k < oneBindingToDeal.size(); k++) {
                    HashMap<String, String> bindingOfOnePlace = oneBindingToDeal.get(k);

                    /*————debug————*/
                    System.out.println("一个库所中的绑定中的映射数为：" + bindingOfOnePlace.size());
                    for (String var : bindingOfOnePlace.keySet()) {
                        System.out.println(var + "->" + bindingOfOnePlace.get(var));
                    }
                    /*————debug————*/

                    for (String varOfOnePlace : bindingOfOnePlace.keySet()) {

                        /*————debug————*/
                        System.out.println("进入变量" + varOfOnePlace + "的判断");
                        /*————debug————*/

                        boolean isLegal = true;

                        for (int l = k + 1; l < oneBindingToDeal.size(); l++) {
                            HashMap<String, String> bindingOfAnotherPlace = oneBindingToDeal.get(l);
                            if (bindingOfAnotherPlace.containsKey(varOfOnePlace)) {
                                if (bindingOfOnePlace.get(varOfOnePlace) == bindingOfAnotherPlace.get(varOfOnePlace)) {
                                    isLegal = false;
                                }
                            }
                        }

                        if (isLegal) {
                            resBinding.put(varOfOnePlace, bindingOfOnePlace.get(varOfOnePlace));
                        }

                    }

                }

                allBindingMap.add(resBinding);
            }

            /*————debug————*/
            System.out.println("处理后的绑定数为" + allBindingMap.size());
            /*————debug————*/

            for (int j = 0; j < allBindingMap.size(); j++) {
                HashMap<String, String> bindingMap = allBindingMap.get(j);

                //根据绑定修改输入矩阵。不是绑定是γ
                int[][] curInputMatrix = recalcMatrix(petriNet.getInputMatrix(), transition, constraintFunction);


                /*先看守卫函数*/
                // TODO:
                if (!judgeGuardFunc(transition.getGuardFunction(), bindingMap)) {

                    /*————debug————*/
                    System.out.println("守卫函数判断不通过");
                    /*————debug————*/

                    continue;
                }



                /*再看当前标识是否满足输入矩阵*/

                /*————debug————*/
                System.out.println("变迁" + (transition.getId() + 1) + "能否发生到最后一步判断");
                /*————debug————*/

//                /*整合标识*/
//                HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(curMarking);

                /*输入矩阵行*/
                int[] row = curInputMatrix[i];
                HashMap<String, HashMap<Token, Integer>> inputMatrixRow = calcRowAfterBinding(row, bindingMap);

                /*比较*/
                if (MarkingSatisfyRow(MAfterIntegration, inputMatrixRow)) {
                    /*只要有一个绑定使变迁使能，即可返回假（非端点）*/

                    /*————debug————*/
                    System.out.println("变迁" + (transition.getId() + 1) + "能发生");
                    /*————debug————*/

                    return false;
                }

                /*————debug————*/
                System.out.println("变迁" + (transition.getId() + 1) + "不能发生");
                /*————debug————*/

            }


        }



        /*走到这一步，一直没有返回说明没有使能变迁，于是返回真（是端点）*/
        return true;
    }

//    public HashSet<String> calcVarSetToMapInBinding(Transition transition) {
//        HashSet<String> varSet = new HashSet<String>();
//
//        // TODO: 三重循环，优化一下
//        ArrayList<Arc> inputArcs = transition.getInputArcs();// TODO: 首先考虑变迁的输入弧会不会变？
//        for (int i = 0; i < inputArcs.size(); i++) {
//            Arc arc = inputArcs.get(i);
//
//            // 输入弧中的变量
//            ArrayList<String[]> tuplesInArcLabel = arc.getTuplesInArcLabel();
//            for (int j = 0; j < tuplesInArcLabel.size(); j++) {
//                String tuple = tuplesInArcLabel.get(j)[0];
//                String[] variablesInTuple = CommonStringMethods.stringToStringArray(tuple);
//                for (int k = 0; k < variablesInTuple.length; k++) {
//                    varSet.add(variablesInTuple[k]);//重复元素并不会添进去。能用addAll？
//                }
//            }
//
//            // 前集虚库所的变量
//            if (arc.getSource() instanceof VirtualPlace) {
//                varSet.add(arc.getSource().getName());//重复元素并不会添进去
//            }
//
//        }
//
//        return  varSet;
//    }

    public HashSet<String> calcVarSetToMapInBinding(Transition transition) {
        HashSet<String> varSet = new HashSet<String>();

        /*shur*/

        // TODO: 三重循环，优化一下
        ArrayList<Arc> inputArcs = transition.getInputArcs();// TODO: 首先考虑变迁的输入弧会不会变？petriNet在整个过程中都是不变的。
        for (int i = 0; i < inputArcs.size(); i++) {
            Arc arc = inputArcs.get(i);

            // 输入弧中的变量
            ArrayList<String[]> tuplesInArcLabel = arc.getTuplesInArcLabel();
            for (int j = 0; j < tuplesInArcLabel.size(); j++) {
                String tuple = tuplesInArcLabel.get(j)[0];
                String[] variablesInTuple = CommonStringMethods.stringToStringArray(tuple);
                for (int k = 0; k < variablesInTuple.length; k++) {
                    varSet.add(variablesInTuple[k]);//重复元素并不会添进去。能用addAll？
                }
            }

            // 前集虚库所的变量
            if (arc.getSource() instanceof VirtualPlace) {
                varSet.add(arc.getSource().getName());//重复元素并不会添进去
            }

        }

        /*————debug————*/
//        System.out.println("绑定中需要" + varSet.size() + "组映射");
        /*————debug————*/

        return  varSet;
    }

    /*————debug————*/

    /*这种做法也是有隐患的，只是按位置对应添加约束，没有按token对应添加*/
//    public HashMap<String, HashSet<String>> constraintMapping(Transition transition) {
//        HashMap<String, HashSet<String>> result = new HashMap<>();
//
//
//        /*每个位置所有可能的变量。此顺序表的size即为元数*/
//        ArrayList<HashSet<String>> possibleVarsAtEachLocation = new ArrayList<>();
//
//        /*每个位置所有可能的常量。此顺序表的size即为元数*/
//        ArrayList<HashSet<String>> possibleConsAtEachLocation = new ArrayList<>();
//
//
//
//        ArrayList<Arc> inputArcs = transition.getInputArcs();
//
//        /*必须在这里先求出元数？*/
//        int arity = 0;
//        for (int i = 0; i < inputArcs.size(); i++) {//这样求元数依赖于输入弧必须有标签元组，存在隐患
//            if (inputArcs.get(i).getTuplesInArcLabel().size() != 0) {
//                arity = CommonStringMethods.stringToStringArray(inputArcs.get(i).getTuplesInArcLabel().get(0)[0]).length;
//                break;
//            }
//        }
//
//        for (int i = 0; i < arity; i++) {
//            possibleVarsAtEachLocation.add(new HashSet<>());
//            possibleConsAtEachLocation.add(new HashSet<>());
//        }
//
//
//        for (int i = 0; i < inputArcs.size(); i++) {
//            Arc arc = inputArcs.get(i);
//
//            // 输入弧中的变量
//            /*找出输入弧中所有n元组（里面包含形参）*/
//            ArrayList<String[]> tuplesInArcLabel = arc.getTuplesInArcLabel();
//            for (int j = 0; j < tuplesInArcLabel.size(); j++) {
//                String tuple = tuplesInArcLabel.get(j)[0];
//                String[] variablesInTuple = CommonStringMethods.stringToStringArray(tuple);//到这里变量应该是有序的
//                for (int k = 0; k < variablesInTuple.length; k++) {//variablesInTuple.length即为元数！
//
//
//                    possibleVarsAtEachLocation.get(k).add(variablesInTuple[k]);
//                }
//            }
//
//            // 前集库所的常量
//            /*找出前集库所中所有n元组的token（里面包含实参）*/
//            if (arc.getSource() instanceof RealPlace) {// TODO: 这样不按标识算会不会有问题？肯定有问题。。
//                RealPlace place = (RealPlace) arc.getSource();
//                ArrayList<Token> tokens = place.getTokens();
//                for (int j = 0; j < tokens.size(); j++) {
//                    ArrayList<String> cons = tokens.get(j).getConstants();
//                    for (int k = 0; k < cons.size(); k++) {//cons.size()即为元数！
//                        possibleConsAtEachLocation.get(k).add(cons.get(k));
//                    }
//                }
//            }
//
//
//        }
//
//        for (int i = 0; i < possibleVarsAtEachLocation.size(); i++) {
//            HashSet<String> possibleVarsAtThisLocation = possibleVarsAtEachLocation.get(i);
//            HashSet<String> possibleConsAtThisLocation = possibleConsAtEachLocation.get(i);
//
//            Iterator<String> varIterator = possibleVarsAtThisLocation.iterator();
//            while (varIterator.hasNext()) {//迭代变量
//                String var = varIterator.next();
//                result.put(var, possibleConsAtThisLocation);
//            }
//
//        }
//
//        return result;
//    }

    /*这种做法也是有隐患的，只是按位置对应添加约束，没有按token对应添加*/
//    public HashMap<String, HashSet<String>> constraintMapping(Transition transition, CTNode node) {
//        HashMap<String, HashSet<String>> result = new HashMap<>();
//
//
//        /*每个位置所有可能的变量。此顺序表的size即为元数*/
//        ArrayList<HashSet<String>> possibleVarsAtEachLocation = new ArrayList<>();
//
//        /*每个位置所有可能的常量。此顺序表的size即为元数*/
//        ArrayList<HashSet<String>> possibleConsAtEachLocation = new ArrayList<>();
//
//        /*存储用来实化的绑定*/
//        HashMap<String, String> bindingForInstantiation = new HashMap<>();
//
//
//
//        ArrayList<Arc> inputArcs = transition.getInputArcs();
//
//        /*必须在这里先求出元数？*/
//        // TODO: 这种求法只适用于所有前集库所的元数都相等的情况吧。。。
//        int arity = 0;
//        for (int i = 0; i < inputArcs.size(); i++) {//这样求元数依赖于输入弧必须有标签元组，存在隐患
//            if (inputArcs.get(i).getTuplesInArcLabel().size() != 0) {
//                arity = CommonStringMethods.stringToStringArray(inputArcs.get(i).getTuplesInArcLabel().get(0)[0]).length;
//                break;
//            }
//        }
//
//
//
//        /*初始化*/
//        /*但这样没有考虑前集库所实化的绑定*/
//        for (int i = 0; i < arity; i++) {
//            possibleVarsAtEachLocation.add(new HashSet<>());
//            possibleConsAtEachLocation.add(new HashSet<>());
//        }
//
//
//
//
//
//        for (int i = 0; i < inputArcs.size(); i++) {
//            Arc arc = inputArcs.get(i);
//
//            // 输入弧中的变量
//            /*找出输入弧中所有n元组（里面包含形参）*/
//            /*虚弧中的应该也会加进去*/
//            /*但是没有考虑用来实化的那个绑定*/
//            ArrayList<String[]> tuplesInArcLabel = arc.getTuplesInArcLabel();
//            for (int j = 0; j < tuplesInArcLabel.size(); j++) {
//                String tuple = tuplesInArcLabel.get(j)[0];
//                String[] variablesInTuple = CommonStringMethods.stringToStringArray(tuple);//到这里变量应该是有序的
//                for (int k = 0; k < variablesInTuple.length; k++) {//variablesInTuple.length即为元数！
//
//
//                    possibleVarsAtEachLocation.get(k).add(variablesInTuple[k]);
//                }
//            }
//
//
//            /*按照标识计算*/
//            /*找出当前标识下前集库所（原本就是实库所）中所有n元组的token（里面包含实参）*/
//            HashMap<String, ArrayList<Token>> M = node.getMarking().getM();
//            if (arc.getSource() instanceof RealPlace) {
//                RealPlace place = (RealPlace) arc.getSource();
//                for (String var : M.keySet()) {
//                    if (var.equals(place.getName())) {
//                        ArrayList<Token> tokens = M.get(var);
//                        for (int j = 0; j < tokens.size(); j++) {
//                            ArrayList<String> cons = tokens.get(j).getConstants();
//                            for (int k = 0; k < arity; k++) {//这里依赖于计算出的元数。没问题吗？
//                                possibleConsAtEachLocation.get(k).add(cons.get(k));
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            if (arc.getSource() instanceof VirtualPlace) {
//                VirtualPlace prePlace = (VirtualPlace) arc.getSource();
//
//                /*利用本结点的绑定。这样会不会有问题？*/
//                HashMap<String, String> bindingOfThisNode = node.getBindingMap();
//                for (String var : bindingOfThisNode.keySet()) {
//                    /*本结点中的绑定使前集虚库所实化。此时实化后的前集库所中可能有token*/
//                    if (var.equals(prePlace.getName())) {
//                        String prePlaceAfterInstantiation = bindingOfThisNode.get(var);
//                        ArrayList<Token> tokens = M.get(prePlaceAfterInstantiation);
//                        for (int j = 0; j < tokens.size(); j++) {
//                            ArrayList<String> cons = tokens.get(j).getConstants();
//                            for (int k = 0; k < arity; k++) {//这里依赖于计算出的元数。没问题吗？
//                                possibleConsAtEachLocation.get(k).add(cons.get(k));
//                            }
//                        }
//
//                        /*处理用来实化的绑定*/
//                        /*要注意本方法中是有位置顺序的*/
//                        bindingForInstantiation.put(var, prePlaceAfterInstantiation);
//
//
//                    }
//                }
//            }
//
//
//
//        }
//
//
//
//
//
//        for (int i = 0; i < possibleVarsAtEachLocation.size(); i++) {
//            HashSet<String> possibleVarsAtThisLocation = possibleVarsAtEachLocation.get(i);
//            HashSet<String> possibleConsAtThisLocation = possibleConsAtEachLocation.get(i);
//
//            Iterator<String> varIterator = possibleVarsAtThisLocation.iterator();
//            while (varIterator.hasNext()) {//迭代变量
//                String var = varIterator.next();
//                result.put(var, possibleConsAtThisLocation);
//            }
//
//        }
//
//        /*处理用来实化库所的绑定*/
//        for (String var : bindingForInstantiation.keySet()) {
//            if (result.containsKey(var)) {
//                result.get(var).add(bindingForInstantiation.get(var));
//            } else {
//                HashSet<String> cons = new HashSet<>();
//                cons.add(bindingForInstantiation.get(var));
//                result.put(var, cons);
//            }
//        }
//
//
//        return result;
//    }

    public HashMap<String, HashSet<String>> constraintMapping(Transition transition, CTNode node) {
        HashMap<String, HashSet<String>> result = new HashMap<>();

        ArrayList<HashMap<String, HashSet<String>>> possibleMappingOfAllPlaces = new ArrayList<>();



        ArrayList<Arc> inputArcs = transition.getInputArcs();

//        /*必须在这里先求出元数？*/
//        // TODO: 这种求法只适用于所有前集库所的元数都相等的情况吧。。。
//        int arity = 0;
//        for (int i = 0; i < inputArcs.size(); i++) {//这样求元数依赖于输入弧必须有标签元组，存在隐患
//            if (inputArcs.get(i).getTuplesInArcLabel().size() != 0) {
//                arity = CommonStringMethods.stringToStringArray(inputArcs.get(i).getTuplesInArcLabel().get(0)[0]).length;
//                break;
//            }
//        }



        for (int i = 0; i < inputArcs.size(); i++) {

            HashMap<String, HashSet<String>> possibleMappingOfOnePlace = new HashMap<>();

            Arc arc = inputArcs.get(i);

            /*每个位置所有可能的变量。此顺序表的size即为元数*/
            ArrayList<HashSet<String>> possibleVarsAtEachLocation = new ArrayList<>();

            /*每个位置所有可能的常量。此顺序表的size即为元数*/
            ArrayList<HashSet<String>> possibleConsAtEachLocation = new ArrayList<>();

            /*存储用来实化的绑定*/
            HashMap<String, String> bindingForInstantiation = new HashMap<>();


            // 输入弧中的变量
            /*找出输入弧中所有n元组（里面包含形参）*/
            /*虚弧中的应该也会加进去*/
            /*但是没有考虑用来实化的那个绑定*/
            ArrayList<String[]> tuplesInArcLabel = arc.getTuplesInArcLabel();

            int arityOfOnePlace = CommonStringMethods.stringToStringArray(tuplesInArcLabel.get(0)[0]).length;//每个前集库所有一个元数

            /*初始化*/
            /*但这样没有考虑前集库所实化的绑定*/
            for (int j = 0; j < arityOfOnePlace; j++) {
                possibleVarsAtEachLocation.add(new HashSet<>());
                possibleConsAtEachLocation.add(new HashSet<>());
            }

            for (int j = 0; j < tuplesInArcLabel.size(); j++) {
                String tuple = tuplesInArcLabel.get(j)[0];
                String[] variablesInTuple = CommonStringMethods.stringToStringArray(tuple);//到这里变量应该是有序的
                for (int k = 0; k < variablesInTuple.length; k++) {//variablesInTuple.length等于元数！


                    possibleVarsAtEachLocation.get(k).add(variablesInTuple[k]);
                }
            }


            /*按照标识计算*/
            HashMap<String, ArrayList<Token>> M = node.getMarking().getM();

            /*找出当前标识下前集库所（原本就是实库所）中所有n元组的token（里面包含实参）*/
            if (arc.getSource() instanceof RealPlace) {
                RealPlace place = (RealPlace) arc.getSource();
                for (String var : M.keySet()) {
                    if (var.equals(place.getName())) {
                        ArrayList<Token> tokens = M.get(var);
                        for (int j = 0; j < tokens.size(); j++) {
                            ArrayList<String> cons = tokens.get(j).getConstants();
                            for (int k = 0; k < cons.size(); k++) {//cons.size()等于元数！
                                possibleConsAtEachLocation.get(k).add(cons.get(k));
                            }
                        }
                    }
                }
            }

            /*找出当前标识下前集库所（原本为虚库所）中所有n元组的token（里面包含实参）*/
            if (arc.getSource() instanceof VirtualPlace) {
                VirtualPlace prePlace = (VirtualPlace) arc.getSource();

                /*利用本结点的绑定。这样会不会有问题？*/
                HashMap<String, String> bindingOfThisNode = node.getBindingMap();
                for (String var : bindingOfThisNode.keySet()) {
                    /*本结点中的绑定使前集虚库所实化。此时实化后的前集库所中可能有token*/
                    if (var.equals(prePlace.getName())) {
                        String prePlaceAfterInstantiation = bindingOfThisNode.get(var);
                        ArrayList<Token> tokens = M.get(prePlaceAfterInstantiation);
                        for (int j = 0; j < tokens.size(); j++) {
                            ArrayList<String> cons = tokens.get(j).getConstants();
                            for (int k = 0; k < cons.size(); k++) {//cons.size()等于元数！
                                possibleConsAtEachLocation.get(k).add(cons.get(k));
                            }
                        }

                        /*处理用来实化的绑定*/
                        /*要注意本方法中是有位置顺序的。但是绑定就不存在位置顺序了吧？*/
                        bindingForInstantiation.put(var, prePlaceAfterInstantiation);

                    }
                }
            }

            for (int j = 0; j < possibleVarsAtEachLocation.size(); j++) {
                HashSet<String> possibleVarsAtThisLocation = possibleVarsAtEachLocation.get(j);
                HashSet<String> possibleConsAtThisLocation = possibleConsAtEachLocation.get(j);

                Iterator<String> varIterator = possibleVarsAtThisLocation.iterator();
                while (varIterator.hasNext()) {//迭代变量
                    String var = varIterator.next();
                    possibleMappingOfOnePlace.put(var, possibleConsAtThisLocation);
                }
            }

            /*处理用来实化库所的绑定*/
            for (String var : bindingForInstantiation.keySet()) {
                if (possibleMappingOfOnePlace.containsKey(var)) {
                    possibleMappingOfOnePlace.get(var).add(bindingForInstantiation.get(var));
                } else {
                    HashSet<String> cons = new HashSet<>();
                    cons.add(bindingForInstantiation.get(var));
                    possibleMappingOfOnePlace.put(var, cons);
                }
            }

            possibleMappingOfAllPlaces.add(possibleMappingOfOnePlace);

        }

        for (int i = 0; i < possibleMappingOfAllPlaces.size(); i++) {
            HashMap<String, HashSet<String>> possibleMappingOfOnePlace = possibleMappingOfAllPlaces.get(i);

            for (String var : possibleMappingOfOnePlace.keySet()) {
                HashSet<String> possibleCons = possibleMappingOfOnePlace.get(var);
                if (result.containsKey(var)) {
                    HashSet<String> origPossibleCons = result.get(var);

                    //求交集
                    HashSet<String> newPossibleCons = new HashSet<>();

                    Iterator<String> conIterInOPC = origPossibleCons.iterator();
                    while (conIterInOPC.hasNext()) {
                        String con = conIterInOPC.next();

                        Iterator<String> conIterInPC = origPossibleCons.iterator();
                        while (conIterInPC.hasNext()) {
                            String conToCompareWith = conIterInPC.next();
                            if (con.equals(conToCompareWith)) {
                                newPossibleCons.add(con);
                            }
                        }
                    }

                    result.replace(var, newPossibleCons);

                } else {
                    result.put(var, possibleCons);
                }
            }

        }



//        for (int i = 0; i < possibleVarsAtEachLocation.size(); i++) {
//            HashSet<String> possibleVarsAtThisLocation = possibleVarsAtEachLocation.get(i);
//            HashSet<String> possibleConsAtThisLocation = possibleConsAtEachLocation.get(i);
//
//            Iterator<String> varIterator = possibleVarsAtThisLocation.iterator();
//            while (varIterator.hasNext()) {//迭代变量
//                String var = varIterator.next();
//                result.put(var, possibleConsAtThisLocation);
//            }
//
//        }
//
//        /*处理用来实化库所的绑定*/
//        for (String var : bindingForInstantiation.keySet()) {
//            if (result.containsKey(var)) {
//                result.get(var).add(bindingForInstantiation.get(var));
//            } else {
//                HashSet<String> cons = new HashSet<>();
//                cons.add(bindingForInstantiation.get(var));
//                result.put(var, cons);
//            }
//        }


        return result;
    }

    public boolean bindingSatisfyConstraint(HashMap<String, String> bindingMap, HashMap<String, HashSet<String>> mappingConstraint) {
        for (String varInTheBinding : bindingMap.keySet()) {
            String correspondingCon = bindingMap.get(varInTheBinding);
            HashSet<String> conConstraints = mappingConstraint.get(varInTheBinding);

            /*————debug————*/
            if (conConstraints == null) {
                System.out.println(varInTheBinding + "相应的常量的约束为空");//变量相应的常量的约束为空
            }
            if (correspondingCon == null) {
                System.out.println(varInTheBinding + "相应的常量为空");
            }
            /*————debug————*/

            if (!conConstraints.contains(correspondingCon)) {



                return false;
            }
        }

        return true;
    }

    /*————debug————*/


//    /*或者直接返回ArrayList<HashMap<String, String>[]>？*/
//    public HashMap<String, String> generateBinding(HashSet<String> varSet, ArrayList<String> constants) {
//        HashMap<String, String> res = new HashMap<String, String>();
//
//        ArrayList<ArrayList<String>> allPermutation = Permutation.generateArrayListForEqualNum(constants, varSet.size());//生成所有指定数量常量的排列
//        Permutation.freshGlobalVar();//有必要吗？
//
////        for (int i = 0; i < allPermutation.size(); i++) {//对常量的迭代
////            ArrayList<String> oneTerm = allPermutation.get(i);
////
////            // TODO: 这里的映射建立没问题吧？
////            Iterator<String> varIterator = varSet.iterator();//HashSet的访问比较麻烦
////            int conIndex = 0;
////            while (varIterator.hasNext()) {//对变量的迭代
////                res.put(varIterator.next(), oneTerm.get(conIndex++));
////            }
////
////        }
//
//        Iterator<String> varIterator = varSet.iterator();//HashSet的访问比较麻烦
//        while (varIterator.hasNext()) {//迭代变量
//            String var = varIterator.next();
//            for (int i = 0; i < allPermutation.size(); i++) {//迭代排列
//                ArrayList<String> oneTerm = allPermutation.get(i);
//                for (int j = 0; j < oneTerm.size(); j++) {//迭代常量
//                    res.put(var, oneTerm.get(j));
//                }
//            }
//        }
//
//        return res;
//    }

    /*只需要相同索引建立映射就好*/
    public HashMap<String, String> generateBinding(HashSet<String> varSet, ArrayList<String> constants) {
        HashMap<String, String> res = new HashMap<String, String>();

        Iterator<String> varIterator = varSet.iterator();//HashSet的访问比较麻烦
        int conIndex = 0;
        while (varIterator.hasNext()) {//迭代变量
            String var = varIterator.next();
            String con = constants.get(conIndex++);
            res.put(var, con);
        }

        return res;
    }

    /*利用绑定重新计算矩阵*/
    public int[][] recalcMatrixWithBinding(int[][] origMatrix, Transition transition, HashMap<String, String> bindingMap) {
//        int[][] curMatrix = origMatrix.clone();//clone大法好。吗？这样对原对象有影响？


        /*————debug————*/

        /*不用克隆方法*/
        int[][] curMatrix = new int[origMatrix.length][];
        for (int i = 0; i < curMatrix.length; i++) {
            curMatrix[i] = new int[origMatrix[0].length];
            for (int j = 0; j < origMatrix[0].length; j++) {
                curMatrix[i][j] = origMatrix[i][j];
            }
        }

        for (int i = 0; i < curMatrix[0].length; i++) {
            System.out.print(curMatrix[transition.getId()][i] + " ");
        }
        System.out.println();
        /*————debug————*/

        //这样做的话，如果绑定中的常量既是要实化的库所、又是实库所中的token会不会出错？首先存在这种情况吗？
        for (String var : bindingMap.keySet()) {//遍历绑定。每一个映射最多改一个位置
            for (int i = 0; i < petriNet.getVirtualPlaces().size(); i++) {//遍历虚库所
                if (var.equals(petriNet.getVirtualPlaces().get(i).getName())) {//找到绑定中的变量
                    ArrayList<String> constants = petriNet.getConstants();
                    int origColumnIndex = constants.size() + i;//在原输入矩阵中的列号
                    for (int j = 0; j < constants.size(); j++) {
                        if (bindingMap.get(var).equals(constants.get(j))) {//找到绑定中的常量

                            /*————debug————*/
                            System.out.println("绑定是：" + var + "->" + bindingMap.get(var));
                            System.out.println("行改动位置的列名是：" + constants.get(j));
                            System.out.println("行改动位置的编号是：" + j);
                            /*————debug————*/

                            int curColumnIndex = j;//在新输入矩阵中的列号
                            int rowIndex = transition.getId();
                            curMatrix[rowIndex][curColumnIndex] = curMatrix[rowIndex][origColumnIndex];
                            curMatrix[rowIndex][origColumnIndex] = -1;

                            /*————debug————*/
                            System.out.println("在原输入矩阵中的列：" + origColumnIndex);
                            System.out.println("在新输入矩阵中的列：" + curColumnIndex);
                            /*————debug————*/

                        }
                    }
                }

            }

        }

        /*————debug————*/
        for (int i = 0; i < curMatrix[0].length; i++) {
            System.out.print(curMatrix[transition.getId()][i] + " ");
        }
        System.out.println();
        /*————debug————*/

        return curMatrix;
    }

    /*利用γ重新计算矩阵*/
    // TODO: 应该利用γ而不是绑定
    public int[][] recalcMatrix(int[][] origMatrix, Transition transition, HashMap<String, ArrayList<String>> constraintFunction) {
//        int[][] curMatrix = origMatrix.clone();//clone大法好。吗？这样对原对象有影响？


        /*————debug————*/

        /*不用克隆方法*/
        int[][] curMatrix = new int[origMatrix.length][];
        for (int i = 0; i < curMatrix.length; i++) {
            curMatrix[i] = new int[origMatrix[0].length];
            for (int j = 0; j < origMatrix[0].length; j++) {
                curMatrix[i][j] = origMatrix[i][j];
            }
        }

        System.out.print("行改变前是这样的：");
        for (int i = 0; i < curMatrix[0].length; i++) {
            System.out.print(curMatrix[transition.getId()][i] + " ");
        }
        System.out.println();
        /*————debug————*/

//        //这样做的话，如果绑定中的常量既是要实化的库所、又是实库所中的token会不会出错？首先存在这种情况吗？
//        for (String var : bindingMap.keySet()) {//遍历绑定。每一个映射最多改一个位置
//            for (int i = 0; i < petriNet.getVirtualPlaces().size(); i++) {//遍历虚库所
//                if (var.equals(petriNet.getVirtualPlaces().get(i).getName())) {//找到绑定中的变量
//                    ArrayList<String> constants = petriNet.getConstants();
//                    int origColumnIndex = constants.size() + i;//在原输入矩阵中的列号
//                    for (int j = 0; j < constants.size(); j++) {
//                        if (bindingMap.get(var).equals(constants.get(j))) {//找到绑定中的常量
//
//                            /*————debug————*/
//                            System.out.println("绑定是：" + var + "->" + bindingMap.get(var));
//                            System.out.println("行改动位置的列名是：" + constants.get(j));
//                            System.out.println("行改动位置的编号是：" + j);
//                            /*————debug————*/
//
//                            int curColumnIndex = j;//在新输入矩阵中的列号
//                            int rowIndex = transition.getId();
//                            curMatrix[rowIndex][curColumnIndex] = curMatrix[rowIndex][origColumnIndex];
//                            curMatrix[rowIndex][origColumnIndex] = -1;
//
//                            /*————debug————*/
//                            System.out.println("在原输入矩阵中的列：" + origColumnIndex);
//                            System.out.println("在新输入矩阵中的列：" + curColumnIndex);
//                            /*————debug————*/
//
//                        }
//                    }
//                }
//
//            }
//
//        }

        //利用γ
        for (String var : constraintFunction.keySet()) {//遍历约束函数中的变量。注意每一个映射最多改一个位置
            for (int i = 0; i < petriNet.getVirtualPlaces().size(); i++) {//遍历虚库所
                if (var.equals(petriNet.getVirtualPlaces().get(i).getName()) && (constraintFunction.get(var).size() != 0)) {//找到约束中的变量
                    ArrayList<String> consOfPN = petriNet.getConstants();
                    int origColumnIndex = consOfPN.size() + i;//变量在原输入矩阵中的列号
                    ArrayList<String> consToMap = constraintFunction.get(var);
                    for (int j = 0; j < consToMap.size() - 1; j++) {
                        for (int k = 0; k < consOfPN.size(); k++) {
                            if (consToMap.get(j).equals(consOfPN.get(k))) {
                                /*————debug————*/
                                System.out.println("行改动位置的列名是：" + consOfPN.get(k));
                                System.out.println("行改动位置的编号是：" + k);
                                /*————debug————*/

                                int curColumnIndex = k;//在新输入矩阵中的列号
                                int rowIndex = transition.getId();
                                curMatrix[rowIndex][curColumnIndex] = curMatrix[rowIndex][origColumnIndex];
//                                curMatrix[rowIndex][origColumnIndex] = -1;

                                /*————debug————*/
                                System.out.println("在原输入矩阵中的列：" + origColumnIndex);
                                System.out.println("在新输入矩阵中的列：" + curColumnIndex);
                                /*————debug————*/
                            }
                        }
                    }

                    /*针对最后一个元素的特殊处理，要把变量列上的元素置为-1*/
                    for (int k = 0; k < consOfPN.size(); k++) {
                        if (consToMap.get(consToMap.size() - 1).equals(consOfPN.get(k))) {
                            /*————debug————*/
                            System.out.println("行改动位置的列名是：" + consOfPN.get(k));
                            System.out.println("行改动位置的编号是：" + k);
                            /*————debug————*/

                            int curColumnIndex = k;//在新输入矩阵中的列号
                            int rowIndex = transition.getId();
                            curMatrix[rowIndex][curColumnIndex] = curMatrix[rowIndex][origColumnIndex];
                            curMatrix[rowIndex][origColumnIndex] = -1;

                            /*————debug————*/
                            System.out.println("在原输入矩阵中的列：" + origColumnIndex);
                            System.out.println("在新输入矩阵中的列：" + curColumnIndex);
                            /*————debug————*/
                        }
                    }

//                    for (int j = 0; j < consOfPN.size(); j++) {
//                        if (constraintFunction.get(var).contains(consOfPN.get(j))) {//找到绑定中的常量
//
//                            /*————debug————*/
////                            System.out.println("绑定是：" + var + "->" + bindingMap.get(var));
//                            System.out.println("行改动位置的列名是：" + consOfPN.get(j));
//                            System.out.println("行改动位置的编号是：" + j);
//                            /*————debug————*/
//
//                            int curColumnIndex = j;//在新输入矩阵中的列号
//                            int rowIndex = transition.getId();
//                            curMatrix[rowIndex][curColumnIndex] = curMatrix[rowIndex][origColumnIndex];
//                            curMatrix[rowIndex][origColumnIndex] = -1;
//
//                            /*————debug————*/
//                            System.out.println("在原输入矩阵中的列：" + origColumnIndex);
//                            System.out.println("在新输入矩阵中的列：" + curColumnIndex);
//                            /*————debug————*/
//
//                        }
//                    }
                }

            }

        }

        /*————debug————*/
        System.out.print("行改变后是这样的：");
        for (int i = 0; i < curMatrix[0].length; i++) {
            System.out.print(curMatrix[transition.getId()][i] + " ");
        }
        System.out.println();
        /*————debug————*/

        return curMatrix;
    }

//    public HashMap<ArrayList<String>, Integer> integrateTokens(RealPlace place) {
////        ArrayList<String[]> tokensAfterIntegration = new ArrayList<String[]>();
//
//        // TODO: 这么复杂的键合适吗？
//        HashMap<ArrayList<String>, Integer> tokensAfterIntegration = new HashMap<ArrayList<String>, Integer>();
//
//        ArrayList<Token> allTokens = place.getTokens();
//        int tokenNum = allTokens.size();
//
//        int[] isClassified = new int[tokenNum];//各位置自动初始化为0。0表示未分类，1表示已分类
//
//        for (int i = 0; i < tokenNum; i++) {
//            if (isClassified[i] == 1) {
//                continue;
//            }
//
//            /*下面都是第一次访问到某种类型token的时候要做的事*/
//            Token token = allTokens.get(i);
//            int theTokenNum = 1;//记录此种类型token的个数
//            tokensAfterIntegration.put(token.getConstants(), 1);//在返回的token列表中加入这种token
//            for (int j = i + 1; j < tokenNum; j++) {//遍历token列表中此token后面的token
//                if (isClassified[j] == 0) {
//                    Token tokenToCompareWith = allTokens.get(j);
//                    if (token.equals(tokenToCompareWith)) {
//                        tokensAfterIntegration.replace(token.getConstants(), ++theTokenNum);
//                        isClassified[j] = 1;
//                    }
//                }
//            }
//        }
//
//
//
//        return tokensAfterIntegration;
//    }

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

    /*注意此方法是计算绑定后的行，返回的行中元素为实参*/
    public HashMap<String, HashMap<Token, Integer>> calcRowAfterBinding(int[] row, HashMap<String, String> bindingMap) {
        HashMap<String, HashMap<Token, Integer>> rowAfterBinding = new HashMap<String, HashMap<Token, Integer>>();

        String[] columnName = new String[row.length];//矩阵列名
        ArrayList<String> constants = petriNet.getConstants();
        for (int i = 0; i < constants.size(); i++) {

            /*————debug————*/
            System.out.println("constants.get(" + i + ") is " + constants.get(i) + " while row is " + row[i]);
            /*————debug————*/

            columnName[i] = constants.get(i);
        }
        ArrayList<VirtualPlace> virtualPlaces = petriNet.getVirtualPlaces();
        for (int i = 0; i < virtualPlaces.size(); i++) {
            columnName[i + constants.size()] = virtualPlaces.get(i).getName();
        }

        ArrayList<Arc> arcs = petriNet.getArcs();
        for (int i = 0; i < row.length; i++) {//行中每个元素
            if (row[i] == -1) {
                rowAfterBinding.put(columnName[i], new HashMap<Token, Integer>());//没有弧也要占位
                System.out.println("绑定后重计算行时，空列为" + columnName[i]);

                continue;
            }

            ArrayList<String[]> tuplesInArcLabel = arcs.get(row[i]).getTuplesInArcLabel();
            HashMap<ArrayList<String>, Integer> tuplesAfterMapping = formalToActualInTuple(tuplesInArcLabel, bindingMap);//弧标签经映射后得到的元组
            HashMap<Token, Integer> tuplesToToken = new HashMap<>();
            for (ArrayList<String> tupleAfterMapping : tuplesAfterMapping.keySet()) {
                Token tupleToToken = new Token(tupleAfterMapping);
                int tupleNum = tuplesAfterMapping.get(tupleAfterMapping);
                tuplesToToken.put(tupleToToken, tupleNum);
            }

            rowAfterBinding.put(columnName[i], tuplesToToken);

            /*————debug————*/
            /*注意这里的改变并不指实化的改变，而是将元组中的形参改变为（token中的）实参*/
            System.out.println("绑定后重计算行时，改变的列为" + columnName[i] + "，这是因为" + row[i]);
            for (int j = 0; j < tuplesInArcLabel.size(); j++) {
                System.out.print(tuplesInArcLabel.get(j)[0]);
            }
            System.out.println();
            /*————debug————*/

        }

        return rowAfterBinding;
    }



    /*将弧上元组由形参形式转化为实参形式，并且把存储形式由AL转为HM*/
    public HashMap<ArrayList<String>, Integer> formalToActualInTuple(ArrayList<String[]> tuplesInArcLabelAL, HashMap<String, String> bindingMap) {
        HashMap<ArrayList<String>, Integer> tuplesInArcLabelHM = new HashMap<ArrayList<String>, Integer>();

        for (int i = 0; i < tuplesInArcLabelAL.size(); i++) {
            ArrayList<String> varsInTuple = CommonStringMethods.stringToArrayList(tuplesInArcLabelAL.get(i)[0]);//获取元组中的变量
            for (int j = 0; j < varsInTuple.size(); j++) {
                String con = bindingMap.get(varsInTuple.get(j));// TODO: 这里会出问题，bindingMap中是所有的绑定情况。已解决
                varsInTuple.set(j, con);//用实参替换形参
            }
            tuplesInArcLabelHM.put(varsInTuple, Integer.valueOf(tuplesInArcLabelAL.get(i)[1]));
        }


        return tuplesInArcLabelHM;
    }


    public boolean MarkingSatisfyRow(HashMap<String, HashMap<Token, Integer>> MAfterIntegration, HashMap<String, HashMap<Token, Integer>> inputMatrixRow) {

        for (String column : inputMatrixRow.keySet()) {//遍历行中所有位置
            if (inputMatrixRow.get(column).size() == 0) {//该列为空，不需要标识来满足
                continue;
            }

            HashMap<Token, Integer> tokensInRow = inputMatrixRow.get(column);
            HashMap<Token, Integer> tokensInMarking = MAfterIntegration.get(column);

            for (Token token : tokensInRow.keySet()) {
                if (tokensInMarking.containsKey(token)) {
                    int tupleNum = tokensInRow.get(token);//重写的token方法能够作用到这里的get方法吗？应该可以吧
                    int tokenNum = tokensInMarking.get(token);
                    if (tokenNum < tupleNum) {
                        return false;//只要有一个元组无法满足则直接返回假
                    }
                } else {

                    /*————debug————*/
                    System.out.print("当前标识下" + column + "中没有此类token，token为：");
                    ArrayList<String> constants = token.getConstants();
                    for (int i = 0; i < constants.size(); i++) {
                        System.out.print(constants.get(i) + ",");
                    }
                    System.out.println();
                    /*————debug————*/

                    return false;//如果标识中没有此类token则直接返回假
                }
            }
        }

        /*走到这一步还没返回，说明所有弧标签均被满足*/
        return true;
    }

    /*此方法暂未使用*/
    public boolean tokensSatisfyTuples(HashMap<ArrayList<String>, Integer> tokensAfterIntegration, HashMap<ArrayList<String>, Integer> tuplesAfterMapping) {
//        boolean flag = true;

        for (ArrayList<String> tuple: tuplesAfterMapping.keySet()) {
            boolean hasTokenToMatchTuple = false;
            for (ArrayList<String> token: tokensAfterIntegration.keySet()) {
                if (alsEquals(tuple, token)) {//找到匹配元组的token
                    if (tokensAfterIntegration.get(token) < tuplesAfterMapping.get(tuple)) {//token不满足元组
                        return false;//有匹配元组的token，但数量不满足则直接返回
                    }
                    hasTokenToMatchTuple = true;
                }
            }
            if (!hasTokenToMatchTuple) {
                return false;//没有匹配元组的token直接返回不满足
            }
        }



        return true;
    }


    public boolean alsEquals(ArrayList<String> tuple, ArrayList<String> token) {
//        boolean flag = true;

        for (int i = 0; i < tuple.size(); i++) {//保证元组中所有元素均在token中
            if (!token.contains(tuple.get(i))) {
                return false;
            }
        }

        for (int i = 0; i < token.size(); i++) {//保证token中所有元素均在元组中。有必要吗？
            if (!tuple.contains(token.get(i))) {
                return false;
            }
        }

        return true;
    }

//    public HashMap<String, HashMap<Token, Integer>> integrateTokens(Marking curMarking)
    public void addChildren(CTNode node, CTNode rootNode) {//注意形参实参的问题

        /*————debug————*/
        System.out.println("添加节点要展示的内容");
        /*————debug————*/

        /*变迁*/
        if (!node.equals(rootNode)) {//当结点不是整棵树的根结点时
            node.add(new DefaultMutableTreeNode("Fired transition"));//由此变迁得到此标识
            node.add(new DefaultMutableTreeNode(petriNet.getTransitions().get(node.getTransitionId()).getName()));
        }

        /*标识*/
        node.add(new DefaultMutableTreeNode("Marking"));
//        HashMap<String, ArrayList<Token>> marking = node.getMarking().getM();
        HashMap<String, HashMap<Token, Integer>> marking = integrateTokens(node.getMarking());

        int emptyColumn = 0;
        for (String column : marking.keySet()) {
            if (marking.get(column).size() == 0) {//库所中无标识则跳过
                emptyColumn++;
                continue;
            }

            String placeName = column;
            String allTokens = CommonStringMethods.HashMapToStringOfToken(marking.get(column));
            String placeWithTokens = column + "(" + allTokens + ")";
            node.add(new DefaultMutableTreeNode(placeWithTokens));
        }
        if (emptyColumn == marking.size()) {
            node.add(new DefaultMutableTreeNode("There are no tokens in any place"));//这样说合适吗？
        }



        /*库所集*/
        node.add(new DefaultMutableTreeNode("Place set"));
        ArrayList<String> placeSet = node.getPlaceSet();
        StringBuilder placeSetBuilder = new StringBuilder("(");
        for (int i = 0; i < placeSet.size() - 1; i++) {
            placeSetBuilder.append(placeSet.get(i) + ",");
        }
        placeSetBuilder.append(placeSet.get(placeSet.size() - 1) + ")");
        String placeSetString = placeSetBuilder.toString();
        node.add(new DefaultMutableTreeNode(placeSetString));

        /*γ函数*/
        node.add(new DefaultMutableTreeNode("γ function"));
        HashMap<String, ArrayList<String>> constraintFunction = node.getConstraintFunction();
        if (constraintFunction.size() == 0) {//不存在时也要输出null
            node.add(new DefaultMutableTreeNode("null"));
        } else {
            for (String var : constraintFunction.keySet()) {
                ArrayList<String> cons = constraintFunction.get(var);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < cons.size() - 1; i++) {
                    stringBuilder.append(cons.get(i) + ",");
                }
                if (cons.size() != 0) {//会有等于0的情况吗？？？会，断开连接会删除变量
                    stringBuilder.append(cons.get(cons.size() - 1));
                    String oneMapping = var + "->(" + stringBuilder + ")";
                    node.add(new DefaultMutableTreeNode(oneMapping));
                } else {
                    /*断接后γ函数变为空的情况*/
                    if (constraintFunction.size() == 1) {
                        node.add(new DefaultMutableTreeNode("null"));
                    }
                }

            }
        }

    }

    public boolean isTransitionEnabled(int transitionId, CTNode node) {
        Marking curMarking = node.getMarking();//变迁能否发生要根据此时的标识来判断。也就是说此时M已知，后面计算的是可转移到的标识情况
        HashMap<String, ArrayList<String>> constraintFunction = node.getConstraintFunction();//本结点的γ函数


        Transition transition = petriNet.getTransitions().get(transitionId);

        /*如果前集库所中有虚库所，且虚库所未实化，则该变迁一定不可发生？*/
        /*————debug————*/
        boolean areAllInstantiated = true;//标记所有前集虚库所是否都已实化
        ArrayList<Arc> inputArcs = transition.getInputArcs();
        for (int i = 0; i < inputArcs.size(); i++) {
            if (inputArcs.get(i).getSource() instanceof VirtualPlace) {
                VirtualPlace prePlace = (VirtualPlace) inputArcs.get(i).getSource();

//                HashMap<String, ArrayList<String>> constraintFunction = node.getConstraintFunction();//本节点的γ函数

                boolean isInstantiated = false;//标记某一个前集虚库所是否实化
                for (String var : constraintFunction.keySet()) {
                    if (var.equals(prePlace.getName()) && constraintFunction.get(var).size() != 0 && constraintFunction.get(var) != null) {// TODO: 这里的处理严谨吗？

                        isInstantiated = true;
                        break;

                    }
                }

                /*只要有一个前集库所未实化则变迁不可发生*/
                if (!isInstantiated) {

                    areAllInstantiated = false;
                    break;

                }

            }
        }
        if (!areAllInstantiated) {

            return false;

        }
        /*————debug————*/

        /*————debug————*/
        if (node.getUserObject().equals("C5") && transitionId == 2) {
            System.out.println("C5和t3经过了前集库所连接的考验");
        }
        /*————debug————*/

        HashSet<String> varSet = calcVarSetToMapInBinding(transition);//得到需要映射的变量集，注意这其中的变量顺序并不确定

        /*————debug————*/
        if (node.getUserObject().equals("C5") && transitionId == 2) {
            System.out.println("C5和t3变量集大小为" + varSet.size());
        }
        /*————debug————*/

//        ArrayList<ArrayList<String>> allPermutation = Permutation.generateArrayListForEqualNum(petriNet.getConstants(), varSet.size());//生成所有指定数量常量的排列

        /*根据当前结点的γ，把用来实化的绑定直接存进去。退一步，不存绑定，存用来实化的绑定的常量？*/
        ArrayList<String> relatedConstants = calcConListToMapInBinding(transition, node);

        ArrayList<ArrayList<String>> allPermutation = Permutation.generateArrayListForEqualNum(relatedConstants, varSet.size());

        Permutation.freshGlobalVar();//有必要吗？

        /*————debug————*/
        HashMap<String, HashSet<String>> mappingConstraint = constraintMapping(transition, node);
        /*————debug————*/

        /*————debug————*/
//        if (node.getUserObject().equals("C5") && transitionId == 2) {
//            System.out.println("C5和t3约束集大小为" + mappingConstraint.size());
//            for (String var : mappingConstraint.keySet()) {
//                System.out.println("变量是" + var);
//                HashSet<String> possibleCons = mappingConstraint.get(var);
//                System.out.print(var + "可能对应的变量有" + possibleCons.size() + "个，分别是：");
//                Iterator<String> conIterator = possibleCons.iterator();
//                while (conIterator.hasNext()) {//迭代变量
//                    System.out.print(conIterator.next() + " ");
//                }
//                System.out.println();
//            }
//        }

        /*————debug————*/

        /*尝试所有绑定的组合*/
        /*只要有一个绑定使变迁使能，即可返回真（变迁是使能的）*/
        for (int i = 0; i < allPermutation.size(); i++) {
            HashMap<String, String> bindingMap = generateBinding(varSet, allPermutation.get(i));

            /*————debug————*/
            if (node.getUserObject().equals("C5") && transitionId == 2) {
                System.out.println("C5和t3生成绑定大小为" + bindingMap.size());
            }
            /*————debug————*/

            /*————debug————*/
            if (!bindingSatisfyConstraint(bindingMap, mappingConstraint)) {

                /*————debug————*/
                if (node.getUserObject().equals("C5") && transitionId == 2) {
                    for (String var : bindingMap.keySet()) {
                        System.out.println(var + " -> " + bindingMap.get((var)));
                        HashSet<String> possibleCons = mappingConstraint.get(var);
                        System.out.print(var + "可能对应的变量有" + possibleCons.size() + "个，分别是：");
                        Iterator<String> conIterator = possibleCons.iterator();
                        while (conIterator.hasNext()) {//迭代变量
                            System.out.print(conIterator.next() + " ");
                        }
                        System.out.println();
                    }

                }
                /*————debug————*/

                continue;
            }
            /*————debug————*/

            /*————debug————*/
            if (node.getUserObject().equals("C5") && transitionId == 2) {
                System.out.println("C5和t3生成了满足约束的绑定");
            }
            /*————debug————*/


            //根据绑定修改输入矩阵
            int[][] curInputMatrix = recalcMatrix(petriNet.getInputMatrix(), transition, constraintFunction);


            /*先看守卫函数*/
            // TODO:
            if (!judgeGuardFunc(transition.getGuardFunction(), bindingMap)) {
                continue;
            }


            /*再看当前标识是否满足输入矩阵*/

            /*整合标识*/
            HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(curMarking);

            /*输入矩阵行*/
            int[] row = curInputMatrix[transitionId];
            HashMap<String, HashMap<Token, Integer>> inputMatrixRow = calcRowAfterBinding(row, bindingMap);

            /*————debug————*/
            if (node.getUserObject().equals("C5") && transitionId == 2) {
                System.out.println("C5和t3走到最后一步标识的考验");
            }
            /*————debug————*/

            /*比较*/
            if (MarkingSatisfyRow(MAfterIntegration, inputMatrixRow)) {
                /*只要有一个绑定使变迁使能，即可返回真（变迁是使能的）*/
                return true;
            }



        }


        /*走到这一步，一直没有返回说明没有使能变迁，于是返回假（变迁非使能）*/
        return false;
    }



    //实为生成所有子节点（后续状态）
    public ArrayList<CTNode> enableTransition(int transitionId, CTNode node) {
        HashMap<String, ArrayList<String>> origConstraintFunction = node.getConstraintFunction();//本结点的γ函数

        /*————debug————*/
        System.out.println("enableTransition被调用");
        /*————debug————*/

        Marking curMarking = node.getMarking();//变迁能否发生以及发生后产生的新标识要根据此时的标识来计算


        Transition transition = petriNet.getTransitions().get(transitionId);
        HashSet<String> varSet = calcVarSetToMapInBinding(transition);//得到需要映射的变量集，注意这其中的变量顺序并不确定

//        ArrayList<ArrayList<String>> allPermutation = Permutation.generateArrayListForEqualNum(petriNet.getConstants(), varSet.size());//生成所有指定数量常量的排列

        ArrayList<String> relatedConstants = calcConListToMapInBinding(transition, node);

        ArrayList<ArrayList<String>> allPermutation = Permutation.generateArrayListForEqualNum(relatedConstants, varSet.size());

        Permutation.freshGlobalVar();//有必要吗？

        /*————debug————*/
        HashMap<String, HashSet<String>> mappingConstraint = constraintMapping(transition, node);
        /*————debug————*/

        ArrayList<CTNode> newNodes = new ArrayList<>();

        /*尝试所有绑定的组合*/
        for (int i = 0; i < allPermutation.size(); i++) {
            HashMap<String, String> bindingMap = generateBinding(varSet, allPermutation.get(i));

            /*————debug————*/
            if (!bindingSatisfyConstraint(bindingMap, mappingConstraint)) {
                continue;
            }
            /*————debug————*/

            //根据绑定修改输入矩阵
            int[][] curInputMatrix = recalcMatrix(petriNet.getInputMatrix(), transition, origConstraintFunction);


            /*先看守卫函数*/
            // TODO:
            if (!judgeGuardFunc(transition.getGuardFunction(), bindingMap)) {
                continue;
            }


            /*再看当前标识是否满足输入矩阵*/

            /*整合标识*/
            HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(curMarking);

            /*输入矩阵行*/
            int[] inputRow = curInputMatrix[transitionId];
            HashMap<String, HashMap<Token, Integer>> inputMatrixRow = calcRowAfterBinding(inputRow, bindingMap);

            /*比较。若满足则计算新结点，不满足则无操作*/
            if (MarkingSatisfyRow(MAfterIntegration, inputMatrixRow)) {
                /*到这里算是找到了node配置下使变迁i使能的某绑定，于是可以创建一个新结点*/

                /*初始化新结点的库所集*/
//                ArrayList<String> placeSet = (ArrayList<String>) node.getPlaceSet().clone();//这里不用克隆会不会有问题？又牵扯到形参实参。可能用了也有问题！
                ArrayList<String> origPlaceSet = node.getPlaceSet();
                ArrayList<String> placeSet = new ArrayList<>();
                placeSet.addAll(origPlaceSet);
//                for (int j = 0; j < origPlaceSet.size(); j++) {
//                    placeSet.add(origPlaceSet.get(j));
//                }

                /*初始化新结点的γ函数*/
//                HashMap<String, ArrayList<String>> constraintFunction = (HashMap<String, ArrayList<String>>) node.getConstraintFunction().clone();
//                HashMap<String, ArrayList<String>> origConstraintFunction = node.getConstraintFunction();
                HashMap<String, ArrayList<String>> constraintFunction = new HashMap<>();
                for (String var : origConstraintFunction.keySet()) {
                    ArrayList<String> origCons = origConstraintFunction.get(var);
                    ArrayList<String> cons = new ArrayList<>();
                    cons.addAll(origCons);
                    constraintFunction.put(var, cons);
                }

                // TODO: 用HashSet存库所集？
                /*遍历后继库所，看是否有实化的虚库所，更新库所集*/
                /*另一种思路是算出来变迁发生后的标识，然后哪列有元素则将该列名放入库所集。有误，因为库所中未必有token*/
                ArrayList<Arc> outputArcs = transition.getOutputArcs();
                for (int j = 0; j < outputArcs.size(); j++) {

                    /*————debug————*/
                    System.out.println("输出弧有" + j + "个");
                    /*————debug————*/

                    Place postPlace = (Place) outputArcs.get(j).getTarget();
                    if (postPlace instanceof VirtualPlace) {//当后集库所为虚库所时
                        String var = postPlace.getName();
                        String newPlace = bindingMap.get(var);//newPlace是常量。要不要考虑绑定中没有这个键？
                        if (!placeSet.contains(newPlace)) {//如果用HashSet存库所集就可以省去这里的判断
                            placeSet.add(newPlace);

                            /*————debug————*/
                            System.out.println("newPlace(变量)为" + newPlace);
                            /*————debug————*/

                            // TODO: 更新γ的代码提前到这里。有问题吗？有。应该放在if条件之外，避免实化库所已存在导致γ函数不变的情况

                            /*对ρ的解析*/
                            /*注意解析ρ的位置。因为只有ρ才能带来γ集的变化，所以ρ相关操作在γ前面*/
//                            ArrayList<String[]> linkFunction = transition.getLinkFunction();
//                            for (int k = 0; k < linkFunction.size(); k++) {
//                                if (linkFunction.get(k)[1].equals(var)) {
//                                    if (linkFunction.get(k)[0].equals("true")) {// TODO: 布尔表达式的解析
//                                        String oper = linkFunction.get(k)[2];
//                                        if (oper.equals("+")) {
//                                            if (!constraintFunction.containsKey(var)) {//γ中还没有关于此变量的映射
//                                                ArrayList<String> cons = new ArrayList<>();
//                                                cons.add(newPlace);
//
//                                                /*————debug————*/
//                                                System.out.println("γ中加入第一个变量：" + newPlace);
//                                                System.out.println("linkFunction.size() is " + linkFunction.size());
//                                                /*————debug————*/
//
//                                                constraintFunction.put(var, cons);
//                                            } else {//γ中已有关于此变量的映射
//                                                ArrayList<String> cons = constraintFunction.get(var);
//                                                cons.add(newPlace);
//
//                                                /*————debug————*/
//                                                System.out.println("γ中新添变量：" + newPlace);
//                                                System.out.println("linkFunction.size() is " + linkFunction.size());
//                                                /*————debug————*/
//
//                                                constraintFunction.replace(var, cons);
//                                            }
//                                        } else if (oper.equals("-")) {//安全起见，用else if
//                                            /*如果是减，说明之前γ中一定有关于此变量的映射。这里加判断是为了安全*/
//                                            if (constraintFunction.containsKey(var)) {
//                                                ArrayList<String> cons = new ArrayList<>();
//                                                cons.remove(newPlace);
//                                                constraintFunction.put(var, cons);
//                                            }
//                                        }
//
//                                    }
//                                }
//                            }


                        }

                        /*对ρ的解析*/
                        /*注意解析ρ的位置。因为只有ρ才能带来γ集的变化，所以ρ相关操作在γ前面*/
                        ArrayList<String[]> linkFunction = transition.getLinkFunction();

                        /*————debug————*/
                        System.out.println("考察变迁" + (transitionId + 1) + "的连接函数。连接函数共" + linkFunction.size() + "个");
                        /*————debug————*/

                        for (int k = 0; k < linkFunction.size(); k++) {

                            /*————debug————*/
                            System.out.println("输出连接函数开始");
                            for (int l = 0; l < 3; l++) {
                                System.out.print(linkFunction.get(k)[l] + " ");
                            }
                            System.out.println("输出连接函数结束");
                            /*————debug————*/

                            if (linkFunction.get(k)[1].equals(var)) {
                                if (linkFunction.get(k)[0].equals("true")) {// TODO: 布尔表达式的解析
                                    String oper = linkFunction.get(k)[2];
                                    if (oper.equals("+")) {
                                        if (!constraintFunction.containsKey(var)) {//γ中还没有关于此变量的映射
                                            ArrayList<String> cons = new ArrayList<>();
                                            cons.add(newPlace);

                                            /*————debug————*/
                                            System.out.println("γ中加入第一个变量：" + newPlace);
                                            System.out.println("linkFunction.size() is " + linkFunction.size());
                                            /*————debug————*/

                                            constraintFunction.put(var, cons);
                                        } else {//γ中已有关于此变量的映射
                                            ArrayList<String> cons = constraintFunction.get(var);
                                            cons.add(newPlace);

                                            /*————debug————*/
                                            System.out.println("γ中新添变量：" + newPlace);
                                            System.out.println("linkFunction.size() is " + linkFunction.size());
                                            /*————debug————*/

                                            constraintFunction.replace(var, cons);
                                        }
                                    } else if (oper.equals("-")) {//安全起见，用else if

                                        /*————debug————*/
                                        System.out.println("判断t4的ρ函数");
                                        /*————debug————*/

                                        /*如果是减，说明之前γ中一定有关于此变量的映射。这里加判断是为了安全*/
                                        if (constraintFunction.containsKey(var)) {

                                            /*————debug————*/
                                            System.out.println("γ函数映射中的变量" + var);
                                            /*————debug————*/

//                                            ArrayList<String> cons = new ArrayList<>();
//                                            cons.remove(newPlace);
//                                            constraintFunction.replace(var, cons);

                                            ArrayList<String> cons = constraintFunction.get(var);
                                            cons.remove(newPlace);
                                            constraintFunction.replace(var, cons);
                                        }
                                    }

                                }
                            }
                        }


                    }
                }

                /*计算新结点的标识*/

                //根据绑定修改输出矩阵

                /*————debug————*/
                System.out.println("修改输出矩阵");
                int[][] debugOM = petriNet.getOutputMatrix();
                int debugColumnNum = debugOM[0].length;
                for (int j = 0; j < debugColumnNum; j++) {
                    System.out.print(debugOM[transitionId][j] + " ");
                }
                System.out.println();
                /*————debug————*/

                int[][] curOutputMatrix = recalcMatrixWithBinding(petriNet.getOutputMatrix(), transition, bindingMap);

                /*————debug————*/
                System.out.println("对petriNet的影响");
                int[][] debugOM2 = petriNet.getOutputMatrix();
                int debugColumnNum2 = debugOM2[0].length;
                for (int j = 0; j < debugColumnNum2; j++) {
                    System.out.print(debugOM2[transitionId][j] + " ");
                }
                System.out.println();
                /*————debug————*/

                //输出矩阵行
                int[] outputRow = curOutputMatrix[transitionId];
                HashMap<String, HashMap<Token, Integer>> outputMatrixRow = calcRowAfterBinding(outputRow, bindingMap);

                Marking newMarking = computeNewMarking(MAfterIntegration, inputMatrixRow, outputMatrixRow);

                /*计算连接函数对γ的影响。和库所集的更新一起进行了*/

                /*到这里，新生成结点的三个维度都计算出来了*/

                /*找旧*/
                /*遍历配置树中从根节点到新结点的路径上的所有结点，如果有相同的则将新结点标为旧*/
                /*这里可以采用JTree的getPath方法获取所有结点集吗？*/

                /*这里要创建结点了*/
                /*id的name暂时无法赋值。想想怎么优化？标签也暂时置空。还是置为新？*/
                CTNode newNode = new CTNode(-1, "", node, newMarking, constraintFunction, placeSet, "New");

                /*————debug————*/
                for (String var : constraintFunction.keySet()) {
                    ArrayList<String> cons = constraintFunction.get(var);
                    System.out.print("\n");
                    for (int j = 0; j < cons.size(); j++) {
                        System.out.print(cons.get(j) + " ");
                    }
                    System.out.print("\n");
                }

                for (String var : bindingMap.keySet()) {
                    System.out.println("绑定是" + var + "->" +bindingMap.get(var));
                }

                /*————debug————*/

                /*把绑定存进去*/
                newNode.setBindingMap(bindingMap);

                /*加入要返回的结点集*/
                newNodes.add(newNode);


//                /*在这里找旧还是在外面找旧呢？在这里找旧还要把可达集传进来*/
//                if (isDuplicatedNode(newNode, reachabilitySet)) {// TODO: 与可达集中的结点比没问题吗？按说应该和路径上的结点比
//                    newNode.setLable("Old");//将标签置为旧
//                    duplicateNodeNumber++;
//
//                }

            }

        }

        return newNodes;

    }

    public Marking computeNewMarking(HashMap<String, HashMap<Token, Integer>> MAfterIntegration, HashMap<String, HashMap<Token, Integer>> inputMatrixRow, HashMap<String, HashMap<Token, Integer>> outputMatrixRow) {
        HashMap<String, HashMap<Token, Integer>> integratedM = new HashMap<>();//是初始化为空还是克隆好？
        HashMap<String, ArrayList<Token>> M;//为了得到这种形式，还要拆分集成好的标识。。这样合理吗？

        for (String column : inputMatrixRow.keySet()) {//遍历行中所有位置。

            HashMap<Token, Integer> inputTokensInRow = inputMatrixRow.get(column);
            HashMap<Token, Integer> outputTokensInRow = outputMatrixRow.get(column);
            HashMap<Token, Integer> tokensInMarking = MAfterIntegration.get(column);

            HashMap<Token, Integer> newTokensInMarking = new HashMap<Token, Integer>();

            /*减输入矩阵行，加输出矩阵行*/

            /*————debug————*/
            /*调用tokensInMarking的任何方法都报空指针异常*/
//            System.out.println(MAfterIntegration.size());//输出3，对吗？不对啊
//            for (String columnD : MAfterIntegration.keySet()) {
//                System.out.println(columnD);//输出“D1 S1 R1”。注意顺序
//            }
//            System.out.println(column);//输出R
//            System.out.println(inputMatrixRow.size());//输出4
//            System.out.println(outputMatrixRow.size());//输出4
//
//            System.out.println(tokensInMarking.size());
//
//            if (tokensInMarking.keySet() == null) {
//                System.out.println("keySet为空");
//            }
//
//            if (tokensInMarking.keySet().size() == 0){
//                System.out.println("token为空");
//            } else {
//                System.out.println("token不为空");
//            }
            /*————debug————*/

            for (Token token : tokensInMarking.keySet()) {//这里逻辑存在问题，都是基于之前存在的标识进行处理



                int tokenNum = tokensInMarking.get(token);

                if (inputTokensInRow.containsKey(token)) {
                    int inputTupleNum = inputTokensInRow.get(token);//重写的token方法能够作用到这里的get方法吗？应该可以吧
                    if (tokenNum >= inputTupleNum) {//这步检查有必要吗？按说是一定成立的，不然变迁无法发生。
                        tokenNum -= inputTupleNum;
                    }
                }

                if (outputTokensInRow.containsKey(token)) {
                    int outputTupleNum = inputTokensInRow.get(token);//重写的token方法能够作用到这里的get方法吗？应该可以吧
                    tokenNum += outputTupleNum;
                }

                newTokensInMarking.put(token, tokenNum);
            }

            /*不用处理输入矩阵，因为如果变迁可发生，则输入矩阵中的token是标识中token的子集*/
            /*标识中之前没有的token，直接按输出矩阵中的情况加入newTokensInMarking*/
            for (Token token : outputTokensInRow.keySet()) {
                if (!tokensInMarking.containsKey(token)) {
                    newTokensInMarking.put(token, outputTokensInRow.get(token));
                }
            }

//            for (Token token : inputTokensInRow.keySet()) {//处理输入矩阵（以及输出矩阵）。一样啊，矩阵中没有，标识中有的token不就忽视了？
//
//                int tokenNumAfterMinus = 0;
//
//                int inputTupleNum = inputTokensInRow.get(token);
//
//                if (tokensInMarking.containsKey(token)) {
//                    int tokenNum = tokensInMarking.get(token);//重写的token方法能够作用到这里的get方法吗？应该可以吧
//                    if (tokenNum >= inputTupleNum) {//这步检查有必要吗？按说是一定成立的，不然变迁无法发生。
//                        tokenNumAfterMinus = tokenNum - inputTupleNum;
//                    }
//                }
//
//                newTokensInMarking.put(token, tokenNumAfterMinus);
//            }

            integratedM.put(column, newTokensInMarking);

            /*————debug————*/
            if (newTokensInMarking.size() != 0) {
                System.out.println("标识生成的方法中库所是" + column);
            }
            /*————debug————*/

        }

        M = splitTokens(integratedM);

        return new Marking(M);
    }

    /*本方法是为computeNewMarking方法服务的*/
    public HashMap<String, ArrayList<Token>> splitTokens(HashMap<String, HashMap<Token, Integer>> integratedM) {
        HashMap<String, ArrayList<Token>> M = new HashMap<>();

        for (String column : integratedM.keySet()) {
            HashMap<Token, Integer> tokensWithNum = integratedM.get(column);
            ArrayList<Token> tokens = new ArrayList<>();

            if (!(tokensWithNum.size() == 0)) {// TODO: 这附近对于空数据的处理完善吗？
                for (Token token : tokensWithNum.keySet()) {
                    int theTokenNum = tokensWithNum.get(token);
                    for (int i = 0; i < theTokenNum; i++) {
                        tokens.add(token);
                    }
                }
            }

            M.put(column, tokens);
        }

        return M;
    }

    public boolean isDuplicatedNode(CTNode node, ArrayList<CTNode> reachabilitySet) {

        /*————debug————*/
        System.out.println("判断结点是否是重复结点，此时可达集的大小为" + reachabilitySet.size());
        /*————debug————*/

        for (int i = 0; i < reachabilitySet.size(); i++) {
            CTNode nodeToCompareWith = reachabilitySet.get(i);
            if (nodeEqual(node, nodeToCompareWith)) {
                return true;//只要有一个相等即可返回真
            }
        }

        /*————debug————*/
        System.out.println("此结点不是重复结点");
        /*————debug————*/

        return false;
    }

    public boolean nodeEqual(CTNode node, CTNode nodeToCompareWith) {

        /*————debug————*/
        if (nodeToCompareWith.getUserObject().equals("C4") && node.getParentNode().getUserObject().equals("C2")) {
            System.out.println("进入C6/C7与C4的比较");
        }
        if (nodeToCompareWith.getUserObject().equals("C4")) {
            System.out.println("进入与C4的比较，其双亲结点为" + node.getParentNode().getUserObject());
        }
        /*————debug————*/

        /*比库所集*/
        /*注意库所集中的常量顺序是无序的*/
        ArrayList<String> placeSet = node.getPlaceSet();
        ArrayList<String> placeSetToCompareWith = nodeToCompareWith.getPlaceSet();

        for (int i = 0; i < placeSet.size(); i++) {
            if (!placeSetToCompareWith.contains(placeSet.get(i))) {

                /*————debug————*/
                System.out.println("不相等是因为1");
                System.out.println("具体来说，placeSet.get(i)为" + placeSet.get(i));
                for (int j = 0; j < placeSetToCompareWith.size(); j++) {
                    System.out.print(" " + placeSetToCompareWith.get(j));
                }
                System.out.println("与之比较的结点是" + nodeToCompareWith.getUserObject());
                /*————debug————*/

                return false;//有一个不相等则直接返回
            }
        }
        for (int i = 0; i < placeSetToCompareWith.size(); i++) {
            if (!placeSet.contains(placeSetToCompareWith.get(i))) {

                /*————debug————*/
                System.out.println("不相等是因为2");
                /*————debug————*/

                return false;//有一个不相等则直接返回
            }
        }

        /*比γ函数*/
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

        /*比标识*/
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

        /*————debug————*/
        System.out.println("发现相同结点");
        /*————debug————*/

        return true;

    }

    // TODO: 简单
    public boolean judgeGuardFunc(String guardFunc, HashMap<String, String> bindingMap) {
        int totalNum = 0;
        ArrayList<String> boolExprs = CommonStringMethods.splitGuardFunc(guardFunc);

        /*针对守卫函数为空的情况*/
        if (boolExprs.size() == 0) {
            return true;
        }


        for (int i = 0; i < boolExprs.size(); i++) {
            String boolExpr = boolExprs.get(i);
            String var = boolExpr.substring(0, 1);
            String con = boolExpr.substring(2, 4);
            if (bindingMap.get(var).equals(con)) {//这里不用equals，用contains也行？
                totalNum++;
            }

        }

        if (totalNum !=0 ) {
            return true;
        } else {
            return false;
        }

    }

    /*V-0.2*/
    public ArrayList<String> calcConListToMapInBinding(Transition transition, CTNode node) {
        ArrayList<String> conList = new ArrayList<>();

        Marking curMarking = node.getMarking();//当前结点的标识
        HashMap<String, String> bindingOfCurNode = node.getBindingMap();//当前结点的绑定

        HashMap<String, HashMap<Token, Integer>> MAfterIntegration = integrateTokens(curMarking);

        ArrayList<Arc> inputArcs = transition.getInputArcs();
        for (int i = 0; i < inputArcs.size(); i++) {
            Arc inputArc = inputArcs.get(i);


            /*前集库所token中的常量*/

            /*这样没有空指针隐患吧？*/
            Place prePlace = null;
            HashMap<Token, Integer> tokensAfterIntegration = null;


            if (inputArc.getSource() instanceof RealPlace) {//前集库所为实库所的情况
                prePlace = (RealPlace) inputArc.getSource();
                tokensAfterIntegration = MAfterIntegration.get(prePlace.getName());

                /*————debug————*/
                if (tokensAfterIntegration == null) {
                    System.out.println("集成后token集为空且前集库所为实库所");
                }
                /*————debug————*/

            } else if (inputArc.getSource() instanceof VirtualPlace) {//前集库所为虚库所的情况
                prePlace = (VirtualPlace) inputArc.getSource();

                String realPlaceAfterBinding = null;
                if (bindingOfCurNode.containsKey(prePlace.getName())) {//安全起见加一下判断。这里利用绑定而不是γ对不对？
                    realPlaceAfterBinding = bindingOfCurNode.get(prePlace.getName());
                }

                tokensAfterIntegration = MAfterIntegration.get(realPlaceAfterBinding);

                /*前集虚库所实化的绑定中的常量*/
                /*这里怎么能对呢？？？*/
//                if (!conList.contains(prePlace.getName())) {
//                    conList.add(prePlace.getName());
//
//                    /*————debug————*/
//                    if (node.getUserObject().equals("C5") && transition.getId() == 2) {
//                        System.out.println("C5和t3加入常量" + prePlace.getName() + "用于库所实化");
//                    }
//                    /*————debug————*/
//
//                }

                if (!conList.contains(realPlaceAfterBinding)) {
                    conList.add(realPlaceAfterBinding);

                    /*————debug————*/
                    if (node.getUserObject().equals("C5") && transition.getId() == 2) {
                        System.out.println("C5和t3加入常量" + realPlaceAfterBinding + "用于库所实化");
                    }
                    /*————debug————*/

                }

                /*————debug————*/
                if (tokensAfterIntegration == null) {
                    System.out.println("集成后token集为空且前集库所为虚库所");
                }
                /*————debug————*/

            }

            /*————debug————*/
            if (tokensAfterIntegration == null) {
                System.out.println("集成后token集为空");
            }
            /*————debug————*/

            for (Token token : tokensAfterIntegration.keySet()) {
                ArrayList<String> constants = token.getConstants();//此时常量是有序的

                for (int j = 0; j < constants.size(); j++) {
                    String con = constants.get(j);
                    if (!conList.contains(con)) {
                        conList.add(con);

                        /*————debug————*/
                        if (node.getUserObject().equals("C5") && transition.getId() == 2) {
                            System.out.println("C5和t3加入常量" + con + "用于满足token");
                        }
                        /*————debug————*/

                    }
                }
            }



        }

        /*————debug————*/
        if (node.getUserObject().equals("C5") && transition.getId() == 2) {
            System.out.print("C5和t3常量集为");
            for (int i = 0; i < conList.size(); i++) {
                System.out.print(conList.get(i) + ",");
            }
            System.out.println();
        }
        /*————debug————*/

        return  conList;
    }

}

